package com.nurun.activemtl.controller.parse;

import java.util.Arrays;

import android.content.Context;
import android.os.AsyncTask;

import com.nurun.activemtl.data.EventDbAdapter;
import com.nurun.activemtl.model.Court;

public class PersistCourtTask extends AsyncTask<Court, Void, Void> {

    private Context context;

    public PersistCourtTask(Context context) {
        this.context = context;
    }

    @Override
    protected Void doInBackground(Court... elements) {
        EventDbAdapter.getInstance(context).insertOrUpdate(Arrays.asList(elements));
        return null;
    }

}
