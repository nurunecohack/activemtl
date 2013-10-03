package com.nurun.activemtl;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Application;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.nurun.activemtl.controller.EventController;
import com.nurun.activemtl.controller.PlayerController;
import com.nurun.activemtl.controller.parse.ParseEventController;
import com.nurun.activemtl.controller.parse.ParsePlayerController;
import com.nurun.activemtl.data.ActiveMtlDatabaseHelper;
import com.nurun.activemtl.model.parse.ParseEvent;
import com.nurun.activemtl.receiver.LocationBroadcastReceiver;
import com.parse.Parse;
import com.parse.ParseObject;

public class ActiveMtlApplication extends Application {

    private static final String CLIENT_KEY = "NaBMEGW8KLwkaIFhzABiRJOzFYhpq7JZakj4heKc";
    private static final String APP_ID = "u5ku5ZMBw0KtOQRXv3xU82WU5GgEx9v8C5mLThkH";
    public static final String LOCATION_CLIENT = "LOCATION_CLIENT";
    public static final String COURT_CONTROLLER = "COURT_CONTROLLER";
    public static final String GSON = "GSON";
    public static final String PLAYER_CONTROLLER = "PLAYER_CONTROLLER";

    private LocationClient locationClient;
    private EventController courtController;
    private PlayerController playerController;

    @Override
    public void onCreate() {
        super.onCreate();
        ActiveMtlDatabaseHelper.init(this);
        ParseObject.registerSubclass(ParseEvent.class);
        Parse.initialize(this, APP_ID, CLIENT_KEY);

        int googlePlayServicesAvailable = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());
        if (ConnectionResult.SUCCESS == googlePlayServicesAvailable) {
            locationClient = new LocationClient(getApplicationContext(), connectionCallbacks, onConnectionFailedListener);
            locationClient.connect();
        }
        AccountManager accountManager = AccountManager.get(this);
        Account[] accountsByType = accountManager.getAccountsByType("com.google");
        if (accountsByType.length > 0) {
            Account account = accountsByType[0];
            PreferenceHelper.saveLogin(this, account.name);
            Log.i(getClass().getSimpleName(), "Login retrieved : " + PreferenceHelper.getLogin(this));
        }
        courtController = new ParseEventController(getApplicationContext());
        playerController = new ParsePlayerController(getApplicationContext());
    }

    private GooglePlayServicesClient.ConnectionCallbacks connectionCallbacks = new GooglePlayServicesClient.ConnectionCallbacks() {
        @Override
        public void onConnected(Bundle bundle) {
            Log.i(getClass().getSimpleName(), "Connected at GooglePlayService");
            LocationRequest request = LocationRequest.create();
            request.setInterval(60000).setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            Log.i(getClass().getSimpleName(), "Trying to locate user");
            locationClient.requestLocationUpdates(request, onLocationChangeListener);
        }

        @Override
        public void onDisconnected() {
            Log.i(getClass().getSimpleName(), "Disconnected at GooglePlayService");
        }
    };

    private GooglePlayServicesClient.OnConnectionFailedListener onConnectionFailedListener = new GooglePlayServicesClient.OnConnectionFailedListener() {
        @Override
        public void onConnectionFailed(ConnectionResult connectionResult) {
            Log.i(getClass().getSimpleName(), "Connection failed at GooglePlayService");
        }
    };

    private LocationListener onLocationChangeListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            Log.i(getClass().getSimpleName(), "User Located lat " + location.getLatitude() + " lng " + location.getLongitude());
            sendBroadcast(LocationBroadcastReceiver.newIntent(location));
        }
    };

    @Override
    public Object getSystemService(String name) {
        if (LOCATION_CLIENT.equals(name)) {
            return locationClient;
        } else if (COURT_CONTROLLER.equals(name)) {
            return courtController;
        } else if (PLAYER_CONTROLLER.equals(name)) {
            return playerController;
        }
        return super.getSystemService(name);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        ActiveMtlDatabaseHelper.getInstance(this).close();
    }

}
