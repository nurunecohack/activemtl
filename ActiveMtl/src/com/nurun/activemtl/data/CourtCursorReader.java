package com.nurun.activemtl.data;

import android.database.Cursor;

public class CourtCursorReader {

    public static Object getCourtId(Cursor cursor) {
        return CursorReader.getString(cursor, CourtDbAdapter.KEY_ID);
    }
    
    public static String getName(Cursor cursor) {
        return CursorReader.getString(cursor, CourtDbAdapter.KEY_NAME);
    }

    public static String getAddress(Cursor cursor) {
        return CursorReader.getString(cursor, CourtDbAdapter.KEY_ADDRESS);
    }

    public static String getPictureUrl(Cursor cursor) {
        return CursorReader.getString(cursor, CourtDbAdapter.KEY_PICTURE_URL);
    }

    public static int getPlayerCount(Cursor cursor) {
        return CursorReader.getInt(cursor, CourtDbAdapter.KEY_PLAYER_COUNT);
    }

    public static String getSuggestedBy(Cursor cursor) {
        return CursorReader.getString(cursor, CourtDbAdapter.KEY_SUGGESTED_BY);
    }

    public static Object getId(Cursor cursor) {
        return CursorReader.getId(cursor);
    }

    public static double getLatitude(Cursor cursor) {
        return CursorReader.getDouble(cursor, CourtDbAdapter.KEY_LATITUDE);
    }
    
    public static double getLongitude(Cursor cursor) {
        return CursorReader.getDouble(cursor, CourtDbAdapter.KEY_LONGITUDE);
    }

}
