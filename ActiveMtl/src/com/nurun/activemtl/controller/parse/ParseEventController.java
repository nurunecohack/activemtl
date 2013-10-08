package com.nurun.activemtl.controller.parse;

import java.io.ByteArrayOutputStream;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.AsyncTask;

import com.nurun.activemtl.controller.EventController;
import com.nurun.activemtl.http.GetEventsRequestCallbacks;
import com.nurun.activemtl.model.Event;
import com.nurun.activemtl.model.parse.ParseEvent;
import com.nurun.activemtl.model.parse.ParseEventList;
import com.nurun.activemtl.util.BitmapUtil;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;

public class ParseEventController implements EventController {

    protected static final int RESULT_LIMIT = 30;
    protected static final String LOCATION = "location";
    private ParseQuery<ParseEvent> query;
    private Context context;

    public ParseEventController(Context context) {
        this.context = context;
    }

    @Override
    public void findClosestEvents(GetEventsRequestCallbacks callback, double latitude, double longitude) {
        findClosestEvents(callback, latitude, longitude, 10);
    }

    @Override
    public void findClosestEvents(final GetEventsRequestCallbacks callback, final double latitude, final double longitude, int distanceInKm) {
        ParseGeoPoint userLocation = new ParseGeoPoint(latitude, longitude);
        query = getQuery().whereNear(LOCATION, userLocation);
        query.include("post");
        query.setCachePolicy(ParseQuery.CachePolicy.NETWORK_ELSE_CACHE);
        query.setMaxCacheAge(1000 * 60 * 5);
        query.findInBackground(new FindCallback<ParseEvent>() {
            @Override
            public void done(List<ParseEvent> courts, ParseException exception) {
                if (exception == null) {
                    new PersistEventTask(context).execute(courts.toArray(new Event[courts.size()]));
                    if (callback != null) {
                        callback.onGetEventsRequestComplete(new ParseEventList(courts));
                    }
                } else {
                    callback.onGetEventsRequestFailed(new RuntimeException(exception));
                }
            }
        });
    }

    private ParseQuery<ParseEvent> getQuery() {
        query = ParseQuery.getQuery(ParseEvent.class);
        query.setLimit(RESULT_LIMIT);
        return query;
    }

    @Override
    public void canceltasks() {
        if (query != null) {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... params) {
                    query.cancel();
                    return null;
                }
            };

        }
    }

    @Override
    public void addSuggestedEvent(String name, String fileUri, double[] latLong, String[] address) {
        ParseEvent parseCourt = new ParseEvent();
        parseCourt.setAddress(address[0]);
        parseCourt.setCity(address[1]);
        parseCourt.setCountry(address[2]);
        parseCourt.setGeolocation(latLong[0], latLong[1]);
        parseCourt.setPicture(getBytes(fileUri));
        parseCourt.setTitle(name);
        parseCourt.saveWithPicture();
    }

    private byte[] getBytes(String fileUri) {
        Bitmap resizedBitmap = BitmapUtil.getResizedBitmap(Uri.parse(fileUri));
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        resizedBitmap.compress(CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }

}
