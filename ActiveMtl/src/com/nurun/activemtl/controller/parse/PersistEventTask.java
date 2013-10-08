package com.nurun.activemtl.controller.parse;

import java.util.Arrays;

import android.content.Context;
import android.os.AsyncTask;

import com.nurun.activemtl.data.EventDbAdapter;
import com.nurun.activemtl.model.parse.Event;

public class PersistEventTask extends AsyncTask<Event, Void, Void> {

    private Context context;

    public PersistEventTask(Context context) {
        this.context = context;
    }

    @Override
    protected Void doInBackground(Event... elements) {
        EventDbAdapter.getInstance(context).insertOrUpdate(Arrays.asList(elements));
        return null;
    }

}
