package com.nurun.activemtl.model.parse;

import android.location.Location;
import android.util.Log;

import com.nurun.activemtl.model.EventType;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;

@ParseClassName("Event")
public class Event extends ParseObject {

    public static final String LOCATION = "location";
    private static final String TITLE = "title";
    private static final String DESCRIPTION = "description";
    private static final String PICTURE = "picture";
    public static final String EVENT_TYPE = "eventType";
    
    public void setTitle(String title) {
        put(TITLE, title);
    }

    public void setDescription(String description) {
        put(DESCRIPTION, description);
    }

    public void setGeolocation(double latitude, double longitude) {
        ParseGeoPoint geopoint = new ParseGeoPoint(latitude, longitude);
        put(LOCATION, geopoint);
    }

    public String getTitle() {
        return getString(TITLE);
    }

    public String getDescription() {
        return getString(DESCRIPTION);
    }

    public ParseGeoPoint getGeolocation() {
        return getParseGeoPoint(LOCATION);
    }

    public String getEventId() {
        return getObjectId();
    }

    public String getPictureUrl() {
        ParseFile parseFile = getParseFile(PICTURE);
        if (parseFile == null) {
            return null;
        }
        return parseFile.getUrl();
    }

    public void saveWithPicture() {
        try {
            getParseFile(PICTURE).save();
            save();
        } catch (ParseException e) {
            Log.e(getClass().getSimpleName(), e.getMessage(), e);
            throw new RuntimeException(e);
        }

    }

    public void setPicture(byte[] data) {
        put(PICTURE, new ParseFile(data));
    }

    public double getDistance(Location currentLocation) {
        return getGeolocation().distanceInKilometersTo(new ParseGeoPoint(currentLocation.getLatitude(), currentLocation.getLongitude())) * 1000;
    }

    public double[] getLatLng() {
        return new double[] { getGeolocation().getLatitude(), getGeolocation().getLongitude() };
    }

    public EventType getEventType(){
        return EventType.valueOf(getString(EVENT_TYPE));
    }

}
