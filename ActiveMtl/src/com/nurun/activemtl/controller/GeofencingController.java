package com.nurun.activemtl.controller;

import java.util.ArrayList;
import java.util.List;

import android.app.PendingIntent;
import android.content.Context;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationClient.OnAddGeofencesResultListener;
import com.google.android.gms.location.LocationClient.OnRemoveGeofencesResultListener;
import com.nurun.activemtl.ActiveMtlApplication;
import com.nurun.activemtl.PreferenceHelper;
import com.nurun.activemtl.model.Court;
import com.nurun.activemtl.model.CourtList;
import com.nurun.activemtl.receiver.GeofenceTransitionReceiver;

public class GeofencingController {
    private LocationClient locationClient;
    private final Context context;
    private float radius;

    public GeofencingController(Context applicationContext) {
        locationClient = (LocationClient) applicationContext.getSystemService(ActiveMtlApplication.LOCATION_CLIENT);
        this.context = applicationContext;
        radius = PreferenceHelper.getRadius(applicationContext);
    }

    public void addGeofences(CourtList courts) {
        removeGeofences();
        List<Geofence> geofences = new ArrayList<Geofence>();
        for (Court c : courts) {
            Geofence newGeofence = new Geofence.Builder().setRequestId(c.getCourtId())
                    .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT)
                    .setCircularRegion(c.getLatLng()[0], c.getLatLng()[1], radius).setExpirationDuration(Geofence.NEVER_EXPIRE).build();
            geofences.add(newGeofence);
        }

        if (geofences.size() > 0) {
            locationClient.addGeofences(geofences, GeofenceTransitionReceiver.newPendingIntent(context), onAddGeofencesResultListener);
        }
    }

    public void removeGeofences() {
        locationClient.removeGeofences(GeofenceTransitionReceiver.newPendingIntent(context), onRemoveGeofencesResultListener);
    }

    private OnRemoveGeofencesResultListener onRemoveGeofencesResultListener = new OnRemoveGeofencesResultListener() {

        @Override
        public void onRemoveGeofencesByRequestIdsResult(int arg0, String[] arg1) {
            Log.i(getClass().getSimpleName(), "Geofences removed : " + arg1.length);
        }

        @Override
        public void onRemoveGeofencesByPendingIntentResult(int arg0, PendingIntent arg1) {
            Log.i(getClass().getSimpleName(), "Geofences removed by pendingIntent");
        }
    };

    private OnAddGeofencesResultListener onAddGeofencesResultListener = new OnAddGeofencesResultListener() {

        @Override
        public void onAddGeofencesResult(int arg0, String[] arg1) {
            Log.i(getClass().getSimpleName(), "Geofences added : " + arg1.length);
        }
    };

}
