package com.nurun.activemtl.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;

import com.google.android.gms.maps.LocationSource.OnLocationChangedListener;

public class LocationBroadcastReceiver extends BroadcastReceiver {
    
    private static final String EXTRA_LOCATION = "EXTRA_LOCATION";
    private OnLocationChangedListener onLocationChangeListener;

    public LocationBroadcastReceiver(OnLocationChangedListener onLocationChangeListener) {
        this.onLocationChangeListener = onLocationChangeListener;
    }
    
    @Override
    public void onReceive(Context context, Intent intent) {
        onLocationChangeListener.onLocationChanged((Location)intent.getParcelableExtra(EXTRA_LOCATION));
    }

    public static Intent newIntent(Location location) {
        return new Intent(LocationBroadcastReceiver.class.getSimpleName()).putExtra(EXTRA_LOCATION, location);
    }

    public static IntentFilter newIntentFilter() {
        return new IntentFilter(LocationBroadcastReceiver.class.getSimpleName());
    }

}
