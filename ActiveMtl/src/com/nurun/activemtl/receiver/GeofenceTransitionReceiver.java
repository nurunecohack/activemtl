package com.nurun.activemtl.receiver;

import java.util.List;

import android.app.Notification;
import android.app.Notification.Builder;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.LocationClient;
import com.nurun.activemtl.FaCApplication;
import com.nurun.activemtl.PreferenceHelper;
import com.nurun.activemtl.R;
import com.nurun.activemtl.controller.PlayerController;
import com.nurun.activemtl.http.AddPlayerToCourtRequestCallbacks;
import com.nurun.activemtl.http.DeletePlayerToCourtRequestCallbacks;
import com.nurun.activemtl.model.Court;
import com.nurun.activemtl.ui.MainActivity;

public class GeofenceTransitionReceiver extends BroadcastReceiver {

    private static final String GEOFENCE_TRANSITION_ACTION_NAME = "com.bball.court.action.GEOFENCE_TRANSITION";
    private PlayerController playerController;
    private int mId = 145214;

    @Override
    public void onReceive(Context context, Intent intent) {
        playerController = (PlayerController) context.getApplicationContext().getSystemService(FaCApplication.PLAYER_CONTROLLER);
        List<Geofence> geofences = LocationClient.getTriggeringGeofences(intent);
        int transition = LocationClient.getGeofenceTransition(intent);
        Log.i(GeofenceTransitionReceiver.class.getSimpleName(), "onReceive transition : " + (transition == Geofence.GEOFENCE_TRANSITION_ENTER ? "Enter" : "Exit"));
        String title = "";
        String content = "";
        Builder builder = new Notification.Builder(context).setSmallIcon(R.drawable.player_logo)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.player_logo)).setDefaults(Notification.DEFAULT_ALL)
                .setContentIntent(MainActivity.newPendingIntent(context, mId)).setAutoCancel(true);
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (transition == Geofence.GEOFENCE_TRANSITION_ENTER && !PreferenceHelper.isCheckedIn(context)) {
            title = context.getString(R.string.entering_court_zone);
            content = context.getString(R.string.you_just_entered_a_court);
            for (Geofence geofence : geofences) {
                playerController.checkInCourt(geofence.getRequestId(), addPlayerCallback);
            }
            PreferenceHelper.checkIn(context);
            builder.setContentTitle(title).setContentText(content);
            mNotificationManager.notify(mId, builder.build());
        } else if (transition == Geofence.GEOFENCE_TRANSITION_EXIT && PreferenceHelper.isCheckedIn(context)) {
            title = context.getString(R.string.leaving_court_zone);
            content = context.getString(R.string.you_just_leaved_a_court);
            for (Geofence geofence : geofences) {
                playerController.leaveCourt(geofence.getRequestId(), deletePlayerCallback);
            }
            PreferenceHelper.leaveCourt(context);
            builder.setContentTitle(title).setContentText(content);
            mNotificationManager.notify(mId, builder.build());
        }
        // mId allows you to update the notification later on.
    }

    public static PendingIntent newPendingIntent(Context context) {
        Intent intent = new Intent(GEOFENCE_TRANSITION_ACTION_NAME);
        return PendingIntent.getBroadcast(context, -1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private DeletePlayerToCourtRequestCallbacks deletePlayerCallback = new DeletePlayerToCourtRequestCallbacks() {

        @Override
        public void onDeletePlayerSuccess(Court court) {
            Log.i(GeofenceTransitionReceiver.class.getSimpleName(), "New player count : " + court.getPlayerCount());
        }

        @Override
        public void onDeletePlayerFail(RuntimeException runtimeException) {
            Log.e(GeofenceTransitionReceiver.class.getSimpleName(), runtimeException.getMessage());
        }
    };

    private AddPlayerToCourtRequestCallbacks addPlayerCallback = new AddPlayerToCourtRequestCallbacks() {

        @Override
        public void onAddPlayerSuccess(Court court) {
            Log.i(GeofenceTransitionReceiver.class.getSimpleName(), "New player count : " + court.getPlayerCount());
        }

        @Override
        public void onAddPlayerFail(RuntimeException runtimeException) {
            Log.e(GeofenceTransitionReceiver.class.getSimpleName(), runtimeException.getMessage());
        }
    };

}
