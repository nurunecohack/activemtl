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
import com.nurun.activemtl.ActiveMtlApplication;
import com.nurun.activemtl.R;
import com.nurun.activemtl.controller.EventController;
import com.nurun.activemtl.controller.GeofencingController;
import com.nurun.activemtl.http.GetEventsRequestCallbacks;
import com.nurun.activemtl.model.Event;
import com.nurun.activemtl.model.EventList;
import com.nurun.activemtl.util.NavigationUtil;

public class ActiveMapFragment extends SupportMapFragment {

    private boolean mapInitialized = false;
    private Map<Marker, Event> eventByMarker = new HashMap<Marker, Event>();
    private EventController eventController;

    private GeofencingController geofencingController;
    private String playerString;
    protected EventList events;

    public static ActiveMapFragment newFragment() {
        return new ActiveMapFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        eventController = (EventController) getActivity().getApplicationContext().getSystemService(ActiveMtlApplication.COURT_CONTROLLER);;
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
        eventController.canceltasks();
    }

    private void setUpMap() {
        // Set listeners for marker events. See the bottom of this class for their behavior.
        LocationClient locationClient = (LocationClient) getActivity().getApplicationContext().getSystemService(ActiveMtlApplication.LOCATION_CLIENT);
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
            Event event = eventByMarker.get(marker);
            NavigationUtil.goToDetail(getActivity(), event.getEventId());
        }
    };

    protected GetEventsRequestCallbacks getEventsRequestCallbacks = new GetEventsRequestCallbacks() {

        @Override
        public void onGetEventsRequestFailed(RuntimeException runtimeException) {
        }

        @Override
        public void onGetEventsRequestComplete(EventList eventList) {
            events = eventList;
            for (Event event : eventList) {
                MarkerOptions marker = new MarkerOptions();
                marker.position(new LatLng(event.getLatLng()[0], event.getLatLng()[1]));
                marker.draggable(false);
                marker.title(event.getTitle());
                int playerCount = event.getPlayerCount();
                marker.snippet(String.format(playerString, playerCount));
                marker.icon(getIcon(playerCount));
                eventByMarker.put(getMap().addMarker(marker), event);
            }
            geofencingController.addGeofences(events);
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
                eventController.findClosestEvents(getEventsRequestCallbacks, latitude, longitude);
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
