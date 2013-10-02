package com.nurun.activemtl.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class GeofencingService extends Service {

    private final IBinder binder = new LocalBinder();

    public class LocalBinder extends Binder {
        GeofencingService getService() {
            return GeofencingService.this;
        }
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return binder;
    }

}
