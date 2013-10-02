package com.nurun.activemtl.data;

import android.database.Cursor;
import android.provider.BaseColumns;

public class CursorReader {

    public static Long getId(Cursor cursor) {
        return getLong(cursor, BaseColumns._ID);
    }

    public static String getString(Cursor cursor, String columnName) {
        if (cursor == null || cursor.getCount() == 0) {
            throw new IllegalArgumentException("cursor cannot be null or empty");
        }
        return cursor.getString(cursor.getColumnIndex(columnName));
    }

    public static Integer getInt(Cursor cursor, String columnName) {
        if (cursor == null || cursor.getCount() == 0) {
            throw new IllegalArgumentException("cursor cannot be null or empty");
        }
        if (cursor.isNull(cursor.getColumnIndex(columnName))) {
            return null;
        }
        return cursor.getInt(cursor.getColumnIndex(columnName));
    }

    public static Boolean getBoolean(Cursor cursor, String columnName) {
        if (getInt(cursor, columnName) == null) {
            return null;
        }
        return getInt(cursor, columnName) == 1;
    }

    public static Float getFloat(Cursor cursor, String columnName) {
        if (cursor == null || cursor.getCount() == 0) {
            throw new IllegalArgumentException("cursor cannot be null or empty");
        }
        if (cursor.isNull(cursor.getColumnIndex(columnName))) {
            return null;
        }
        return cursor.getFloat(cursor.getColumnIndex(columnName));
    }
    
    public static Double getDouble(Cursor cursor, String columnName) {
        if (cursor == null || cursor.getCount() == 0) {
            throw new IllegalArgumentException("cursor cannot be null or empty");
        }
        if (cursor.isNull(cursor.getColumnIndex(columnName))) {
            return null;
        }
        return cursor.getDouble(cursor.getColumnIndex(columnName));
    }

    public static Long getLong(Cursor cursor, String columnName) {
        if (cursor == null || cursor.getCount() == 0) {
            throw new IllegalArgumentException("cursor cannot be null or empty");
        }
        if (cursor.isNull(cursor.getColumnIndex(columnName))) {
            return null;
        }
        return cursor.getLong(cursor.getColumnIndex(columnName));
    }
}
