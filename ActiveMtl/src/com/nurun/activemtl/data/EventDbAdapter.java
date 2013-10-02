package com.nurun.activemtl.data;

import java.util.HashMap;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.provider.BaseColumns;
import android.util.Log;

import com.nurun.activemtl.model.Court;

public class EventDbAdapter extends DbAdapter<Court> {

    public static final String KEY_ID = Court.class.getSimpleName() + "Id";
    public static final String KEY_NAME = Court.class.getSimpleName() + "Name";
    public static final String VIEW_NAME = Court.class.getSimpleName() + "View";
    public static final String TABLE_NAME = Court.class.getSimpleName() + "Table";
    public static final String KEY_ADDRESS = Court.class.getSimpleName() + "Address";
    public static final String KEY_LATITUDE = Court.class.getSimpleName() + "Latitude";
    public static final String KEY_LONGITUDE = Court.class.getSimpleName() + "Longitude";
    public static final String KEY_PICTURE_URL = Court.class.getSimpleName() + "PictureUrl";
    public static final String KEY_PLAYER_COUNT = Court.class.getSimpleName() + "PlayerCount";
    public static final String KEY_SUGGESTED_BY = Court.class.getSimpleName() + "SuggestedBy";

    private static EventDbAdapter instance;

    private EventDbAdapter(Context context) {
        super(context);
    }

    public static EventDbAdapter getInstance(Context context) {
        if (instance == null) {
            instance = new EventDbAdapter(context);
        }
        return instance;
    }

    @Override
    protected ContentValues getValues(Court court) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_ID, court.getCourtId());
        initialValues.put(KEY_NAME, court.getName());
        initialValues.put(KEY_ADDRESS, court.getAddress());
        initialValues.put(KEY_LATITUDE, court.getLatLng()[0]);
        initialValues.put(KEY_LONGITUDE, court.getLatLng()[1]);
        initialValues.put(KEY_PICTURE_URL, court.getPictureUrl());
        initialValues.put(KEY_PLAYER_COUNT, court.getPlayerCount());
        initialValues.put(KEY_SUGGESTED_BY, court.getSuggestedBy());
        return initialValues;
    }

    @Override
    protected String getTableName() {
        return TABLE_NAME;
    }

    public static Map<String, String> getAttributs() {
        Map<String, String> attributs = new HashMap<String, String>();
        attributs.put(KEY_ID, "text not null");
        attributs.put(KEY_NAME, "text not null");
        attributs.put(KEY_ADDRESS, "text not null");
        attributs.put(KEY_LATITUDE, "int not null");
        attributs.put(KEY_LONGITUDE, "int not null");
        attributs.put(KEY_PICTURE_URL, "text");
        attributs.put(KEY_PLAYER_COUNT, "int");
        attributs.put(KEY_SUGGESTED_BY, "text");
        return attributs;
    }

    @Override
    protected String getExistsColumn() {
        return KEY_ID;
    }

    public Cursor listOrderByDistance() {
        Cursor cursor = new QueryBuilder(getTableView()).columns(BaseColumns._ID, EventDbAdapter.KEY_ID, EventDbAdapter.KEY_NAME, EventDbAdapter.KEY_ADDRESS,
                EventDbAdapter.KEY_PICTURE_URL, EventDbAdapter.KEY_PLAYER_COUNT, EventDbAdapter.KEY_SUGGESTED_BY, EventDbAdapter.KEY_LATITUDE,
                EventDbAdapter.KEY_LONGITUDE).request(mDb);
        return cursor;
    }

    private String getTableView() {
        return EventDbAdapter.TABLE_NAME;
    }

    public Cursor search(String search) {
        Cursor cursor = new QueryBuilder(getTableName()).wherelike(KEY_PLAYER_COUNT, search).or().wherelike(KEY_NAME, search).or()
                .wherelike(KEY_PICTURE_URL, search).request(mDb);
        Log.i(getClass().getSimpleName(), cursor.getCount() + " billets pour la recherche " + search);
        return cursor;
    }

    @Override
    public long insert(Court element) {
        throw new RuntimeException("Do not use this method, use insertOrUpdate instead");
    }
}
