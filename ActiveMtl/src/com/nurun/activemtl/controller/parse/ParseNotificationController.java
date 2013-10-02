package com.nurun.activemtl.controller.parse;

import android.content.Context;

import com.nurun.activemtl.controller.NotificationController;
import com.nurun.activemtl.ui.DetailActivity;
import com.parse.ParseInstallation;
import com.parse.PushService;

public class ParseNotificationController implements NotificationController {

    @Override
    public void sendGeofenceTransitionEnterNotification() {
    }

    public void enablePushNotifications(Context context) {
        PushService.setDefaultPushCallback(context, DetailActivity.class);
        ParseInstallation.getCurrentInstallation().saveInBackground();
    }

}
