package com.nurun.activemtl.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import com.nurun.activemtl.PreferenceHelper;

public class ActiveMtlDatabaseHelper extends SQLiteOpenHelper {
    protected static ActiveMtlDatabaseHelper instance;
    protected static final int DATABASE_VERSION = 1;
    private static String DATABASE_NAME = "activeMtl.db";
    private Context context;

    protected ActiveMtlDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    public static void init(Context context) {
        if (instance == null) {
            instance = new ActiveMtlDatabaseHelper(context);
        }
    }

    public static ActiveMtlDatabaseHelper getInstance(Context context) {
        if (instance == null) {
            throw new IllegalStateException("Database need to be initialized");
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(getClass().getSimpleName(), "Creating database");
        for (String requests : getCreateRequests()) {
            Log.i(getClass().getSimpleName(), "Creating table " + requests);
            db.execSQL(requests);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        deleteTables(db, oldVersion, newVersion);
        deleteViews(db);
        clearCache();
        onCreate(db);
    }

    private void clearCache() {
        PreferenceHelper.clearCache(context);
    }

    private void deleteViews(SQLiteDatabase db) {
        Cursor listView = db.rawQuery("SELECT name FROM sqlite_master WHERE type = ?", new String[] { "view" });
        Log.i(getClass().getSimpleName(), "Droping " + listView.getCount() + " views");
        while (listView.moveToNext()) {
            try {
                db.execSQL("DROP View IF EXISTS " + CursorReader.getString(listView, "name"));
            } catch (Exception e) {
                Log.w(getClass().getSimpleName(), e.getMessage(), e);
            }
        }
        listView.close();
    }

    private Cursor deleteTables(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i(getClass().getSimpleName(), " Upgrading database from version " + oldVersion + " to " + newVersion);
        Cursor listTables = db.rawQuery("SELECT name FROM sqlite_master WHERE type = ?", new String[] { "table" });
        Log.i(getClass().getSimpleName(), "Droping " + listTables.getCount() + " tables");
        while (listTables.moveToNext()) {
            try {
                db.execSQL("DROP TABLE IF EXISTS " + CursorReader.getString(listTables, "name"));
            } catch (Exception e) {
                Log.w(getClass().getSimpleName(), e.getMessage(), e);
            }
        }
        listTables.close();
        return listTables;
    }

    private static String createTable(String tableName, Map<String, String> columns) {
        String createTable = "create table " + tableName + " (" + BaseColumns._ID + " integer primary key autoincrement";
        for (Entry<String, String> entry : columns.entrySet()) {
            createTable += ", " + entry.getKey() + " " + entry.getValue();
        }
        createTable += ")";
        return createTable;
    }

    protected List<String> getCreateRequests() {
        List<String> requests = new ArrayList<String>();
        requests.add(createTable(EventDbAdapter.TABLE_NAME, EventDbAdapter.getAttributs()));
        return requests;
    }
}
