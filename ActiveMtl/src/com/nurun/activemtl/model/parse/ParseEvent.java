package com.nurun.activemtl.model.parse;

import java.util.Random;

import android.location.Location;
import android.util.Log;

import com.nurun.activemtl.model.Court;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;

@ParseClassName("Event")
public class ParseEvent extends ParseObject implements Court {

    private static final long serialVersionUID = -1882774815573887129L;
    private static final String ADDRESS = "address";
    private static final String CITY = "city";
    private static final String COUNTRY = "country";
    private static final String LOCATION = "location";
    private static final String TITLE = "title";
    private static final String DESCRIPTION = "description";
    private static final String SUGGESTED_BY = "suggestedBy";
    private static final String PICTURE = "thumbnail";

    private int playerCount = new Random().nextInt(11);

    public ParseEvent() {
    }

    public void setSuggestedBy(String suggestedBy) {
        put(SUGGESTED_BY, suggestedBy);
    }

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

    public void setCity(String city) {
        put(CITY, city);
    }

    public String getCity() {
        return getString(CITY);
    }

    public void setCountry(String country) {
        put(COUNTRY, country);
    }

    public String getCountry() {
        return getString(COUNTRY);
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

    public boolean isSuggested() {
        return getParseUser(SUGGESTED_BY) != null;
    }

    @Override
    public String getCourtId() {
        return getObjectId();
    }

    public Integer getPlayerCount() {
        return playerCount;
    }

    public String getAddress() {
        return getString(ADDRESS);
    }

    @Override
    public String getPictureUrl() {
        ParseFile parseFile = getParseFile(PICTURE);
        if (parseFile == null) {
            return null;
        }
        return parseFile.getUrl();
    }

    @Override
    public String getSuggestedBy() {
        return getString(SUGGESTED_BY);
    }

    @Override
    public double getDistance(Location currentLocation) {
        return getGeolocation().distanceInKilometersTo(new ParseGeoPoint(currentLocation.getLatitude(), currentLocation.getLongitude())) * 1000;
    }

    @Override
    public double[] getLatLng() {
        return new double[] { getGeolocation().getLatitude(), getGeolocation().getLongitude() };
    }

    public void setAddress(String adress) {
        put(ADDRESS, adress);
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
}
