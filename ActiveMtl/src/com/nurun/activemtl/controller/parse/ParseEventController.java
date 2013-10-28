package com.nurun.activemtl.controller.parse;

import java.io.ByteArrayOutputStream;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;

import com.nurun.activemtl.ActiveMtlConfiguration;
import com.nurun.activemtl.callback.GetEventStatCallback;
import com.nurun.activemtl.callback.GetEventsRequestCallbacks;
import com.nurun.activemtl.controller.EventController;
import com.nurun.activemtl.model.EventType;
import com.nurun.activemtl.model.parse.Event;
import com.nurun.activemtl.model.parse.ParseEventList;
import com.nurun.activemtl.util.BitmapUtil;
import com.parse.CountCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class ParseEventController implements EventController {

	protected static final int RESULT_LIMIT = 30;
	private ParseQuery<Event> query;
	private Context context;

	public ParseEventController(Context context) {
		this.context = context;
	}

	@Override
	public void findClosestEvents(GetEventsRequestCallbacks callback,
			double latitude, double longitude) {
		findClosestEvents(callback, latitude, longitude, 10);
	}

	@Override
	public void findClosestEvents(final GetEventsRequestCallbacks callback,
			final double latitude, final double longitude, int distanceInKm) {
		ParseGeoPoint userLocation = new ParseGeoPoint(latitude, longitude);
		query = getQuery().whereNear(Event.LOCATION, userLocation);
		query.include("post");
		query.setCachePolicy(ParseQuery.CachePolicy.NETWORK_ELSE_CACHE);
		query.setMaxCacheAge(1000 * 60 * 5);
		query.findInBackground(new FindCallback<Event>() {
			@Override
			public void done(List<Event> events, ParseException exception) {
				if (exception == null) {
					new PersistEventTask(context).execute(events
							.toArray(new Event[events.size()]));
					callback.onGetEventsRequestComplete(new ParseEventList(
							events));
				} else {
					callback.onGetEventsRequestFailed(new RuntimeException(
							exception));
				}
			}
		});
	}

	private ParseQuery<Event> getQuery() {
		query = ParseQuery.getQuery(Event.class);
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
	public void addSuggestedEvent(String name, String description,
			String fileUri, double[] latLong, EventType eventType,
			String category) {
		Event event = new Event();
		event.setGeolocation(latLong[0], latLong[1]);
		event.setPicture(getBytes(fileUri));
		event.setTitle(name);
		event.setEventType(eventType);
		event.setDefaultInfos(ParseUser.getCurrentUser());
		event.setCategory(category);
		event.setDescription(description);
		event.saveWithPicture();
	}

	private byte[] getBytes(String fileUri) {
		Bitmap resizedBitmap = BitmapUtil.getResizedBitmap(Uri.parse(fileUri));
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		resizedBitmap.compress(CompressFormat.PNG, 0, stream);
		return stream.toByteArray();
	}

	@Override
	public void getChallengeForCity(final GetEventStatCallback callback) {
		getEventForCity(callback, EventType.Challenge);
	}

	@Override
	public void getIdeaForCity(final GetEventStatCallback callback) {
		getEventForCity(callback, EventType.Idea);
	}

	@Override
	public void getAlertForCity(final GetEventStatCallback callback) {
		getEventForCity(callback, EventType.Alert);
	}

	@Override
	public void getChallengeForDistrict(final GetEventStatCallback callback,
			Location location) {
		getEventForDistrict(callback, EventType.Challenge, location);
	}

	@Override
	public void getIdeaForDistrict(final GetEventStatCallback callback,
			Location location) {
		getEventForDistrict(callback, EventType.Idea, location);
	}

	@Override
	public void getAlertForDistrict(final GetEventStatCallback callback,
			Location location) {
		getEventForDistrict(callback, EventType.Alert, location);
	}

	@Override
	public void getMyChallenge(final GetEventStatCallback callback) {
		getEventForMe(callback, EventType.Challenge);
	}

	@Override
	public void getMyIdea(final GetEventStatCallback callback) {
		getEventForMe(callback, EventType.Idea);
	}

	@Override
	public void getMyAlert(final GetEventStatCallback callback) {
		getEventForMe(callback, EventType.Alert);
	}

	private void getEventForMe(final GetEventStatCallback callback,
			EventType eventType) {
		query = getQuery().whereEqualTo(Event.EVENT_TYPE, eventType)
				.whereEqualTo(Event.CREATED_BY, ParseUser.getCurrentUser());
		query.countInBackground(new CountCallback() {
			public void done(int count, ParseException e) {
				if (e == null) {
					callback.onEventStatComplete(count);
				} else {
					callback.onEventStatFailed(new RuntimeException(e));
				}
			}
		});
	}

	private void getEventForDistrict(final GetEventStatCallback callback,
			EventType eventType, Location location) {
		query = getQuery().whereEqualTo(Event.EVENT_TYPE, eventType)
				.whereWithinKilometers(
						Event.LOCATION,
						new ParseGeoPoint(location.getLatitude(), location
								.getLongitude()),
						ActiveMtlConfiguration.getInstance(context)
								.getDistrictRadius());
		query.countInBackground(new CountCallback() {
			public void done(int count, ParseException e) {
				if (e == null) {
					callback.onEventStatComplete(count);
				} else {
					callback.onEventStatFailed(new RuntimeException(e));
				}
			}
		});
	}

	private void getEventForCity(final GetEventStatCallback callback,
			EventType eventType) {
		query = getQuery().whereEqualTo(Event.EVENT_TYPE, eventType);
		query.countInBackground(new CountCallback() {
			public void done(int count, ParseException e) {
				if (e == null) {
					callback.onEventStatComplete(count);
				} else {
					callback.onEventStatFailed(new RuntimeException(e));
				}
			}
		});
	}
}
