package com.nurun.activemtl.ui.fragment;

import java.util.HashMap;
import java.util.Map;

import android.location.Location;
import android.os.Bundle;

import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationChangeListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.nurun.activemtl.FaCApplication;
import com.nurun.activemtl.R;
import com.nurun.activemtl.controller.CourtController;
import com.nurun.activemtl.controller.GeofencingController;
import com.nurun.activemtl.http.GetCourtsRequestCallbacks;
import com.nurun.activemtl.model.Court;
import com.nurun.activemtl.model.CourtList;
import com.nurun.activemtl.ui.DetailActivity;

public class ActiveMapFragment extends SupportMapFragment {

    private boolean mapInitialized = false;
    private Map<Marker, Court> courtByMarker = new HashMap<Marker, Court>();
    private CourtController courtController;

    private GeofencingController geofencingController;
    private String playerString;
    protected CourtList courts;

    public static ActiveMapFragment newFragment() {
        return new ActiveMapFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        courtController = (CourtController) getActivity().getApplicationContext().getSystemService(FaCApplication.COURT_CONTROLLER);;
        geofencingController = new GeofencingController(getActivity().getApplicationContext());
        playerString = getString(R.string.players);
    }

    @Override
    public void onStart() {
        super.onStart();
        setUpMap();
    }

    @Override
    public void onStop() {
        super.onStop();
        courtController.canceltasks();
    }

    private void setUpMap() {
        // Set listeners for marker events. See the bottom of this class for their behavior.
        LocationClient locationClient = (LocationClient) getActivity().getApplicationContext().getSystemService(FaCApplication.LOCATION_CLIENT);
        if (locationClient.isConnected()) {
            Location location = locationClient.getLastLocation();
            if (location != null) {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                getMap().moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 10));
            }
        }
        getMap().setOnMyLocationChangeListener(onMyLocationChangeListener);
        getMap().setOnInfoWindowClickListener(onInfoWindowClickListener);
        getMap().setMyLocationEnabled(true);
    }

    private OnInfoWindowClickListener onInfoWindowClickListener = new OnInfoWindowClickListener() {
        @Override
        public void onInfoWindowClick(Marker marker) {
            Court court = courtByMarker.get(marker);
            startActivity(DetailActivity.newIntent(getActivity(), court.getCourtId()));
        }
    };

    protected GetCourtsRequestCallbacks getCourtsRequestCallbacks = new GetCourtsRequestCallbacks() {

        @Override
        public void onGetCourtsRequestFailed(RuntimeException runtimeException) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onGetCourtsRequestComplete(CourtList courtList) {
            courts = courtList;
            for (Court court : courtList) {
                MarkerOptions marker = new MarkerOptions();
                marker.position(new LatLng(court.getLatLng()[0], court.getLatLng()[1]));
                marker.draggable(false);
                marker.title(court.getName());
                int playerCount = court.getPlayerCount();
                marker.snippet(String.format(playerString, playerCount));
                marker.icon(getIcon(playerCount));
                courtByMarker.put(getMap().addMarker(marker), court);
            }
            geofencingController.addGeofences(courts);
        }
    };

    private OnMyLocationChangeListener onMyLocationChangeListener = new OnMyLocationChangeListener() {
        @Override
        public void onMyLocationChange(Location location) {
            if (!mapInitialized) {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 10);
                getMap().animateCamera(cameraUpdate);
                mapInitialized = true;
                courtController.findClosestCourt(getCourtsRequestCallbacks, latitude, longitude);
            }
        }
    };

    private BitmapDescriptor getIcon(int playerCount) {
        switch (playerCount) {
        case 0:
            return BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED);
        case 1:
        case 2:
        case 3:
            return BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE);
        case 4:
        case 5:
        case 6:
            return BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW);
        default:
            return BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN);
        }
    }

}
