package com.nurun.activemtl.ui.fragment;

import android.app.ActivityOptions;
import android.app.Fragment;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.LocationSource.OnLocationChangedListener;
import com.nurun.activemtl.ActiveMtlApplication;
import com.nurun.activemtl.R;
import com.nurun.activemtl.controller.CourtController;
import com.nurun.activemtl.controller.GeofencingController;
import com.nurun.activemtl.http.GetCourtsRequestCallbacks;
import com.nurun.activemtl.model.Court;
import com.nurun.activemtl.model.CourtList;
import com.nurun.activemtl.receiver.LocationBroadcastReceiver;
import com.nurun.activemtl.ui.CourtAdapter;
import com.nurun.activemtl.ui.DetailActivity;

public class CourtsListFragment extends Fragment {

    private LocationClient locationClient;
    private GridView gridView;
    private CourtController courtController;
    private TextView progressText;
    private GeofencingController geofencingController;
    private LocationBroadcastReceiver locationBroadcastReceiver;
    protected boolean positionFound = false;

    public static Fragment newFragment() {
        return new CourtsListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        courtController = (CourtController) getActivity().getApplicationContext().getSystemService(ActiveMtlApplication.COURT_CONTROLLER);
        locationClient = (LocationClient) getActivity().getApplicationContext().getSystemService(ActiveMtlApplication.LOCATION_CLIENT);
        geofencingController = new GeofencingController(getActivity().getApplicationContext());
        if (locationClient.isConnected() && locationClient.getLastLocation() != null) {
            Location lastLocation = locationClient.getLastLocation();
            positionFound = true;
            double latitude = lastLocation.getLatitude();
            double longitude = lastLocation.getLongitude();
            courtController.findClosestCourt(getCourtsRequestCallbacks, latitude, longitude);
        } else {
            locationBroadcastReceiver = new LocationBroadcastReceiver(onLocationChangedListener);
            getActivity().registerReceiver(locationBroadcastReceiver, LocationBroadcastReceiver.newIntentFilter());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.courts_fragment, container, false);
        gridView = (GridView) rootView.findViewById(R.id.gridview);
        gridView.setOnItemClickListener(onItemClickListener);
        TextView textView = new TextView(container.getContext());
        textView.setText(R.string.no_result);
        gridView.setEmptyView(textView);
        progressText = (TextView) rootView.findViewById(R.id.progress_text);
        return rootView;
    }

    @Override
    public void onStop() {
        super.onStop();
        courtController.canceltasks();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (locationBroadcastReceiver != null) {
            getActivity().unregisterReceiver(locationBroadcastReceiver);
            locationBroadcastReceiver = null;
        }
    }

    private GetCourtsRequestCallbacks getCourtsRequestCallbacks = new GetCourtsRequestCallbacks() {

        @Override
        public void onGetCourtsRequestFailed(RuntimeException runtimeException) {
            Toast.makeText(getActivity(), "An unexpected error occured", Toast.LENGTH_LONG).show();
            gridView.setVisibility(View.VISIBLE);
            getView().findViewById(R.id.progress).setVisibility(View.GONE);
            progressText.setVisibility(View.GONE);
        }

        @Override
        public void onGetCourtsRequestComplete(CourtList courtList) {
            Location location = new Location("");
            location.setLatitude(locationClient.getLastLocation().getLatitude());
            location.setLongitude(locationClient.getLastLocation().getLongitude());
            gridView.setAdapter(new CourtAdapter(getActivity(), courtList, location));
            gridView.setVisibility(View.VISIBLE);
            getView().findViewById(R.id.progress).setVisibility(View.GONE);
            progressText.setVisibility(View.GONE);
            geofencingController.addGeofences(courtList);
            positionFound = true;
            if (locationBroadcastReceiver != null) {
                getActivity().unregisterReceiver(locationBroadcastReceiver);
                locationBroadcastReceiver = null;
            }
        }
    };

    private OnLocationChangedListener onLocationChangedListener = new OnLocationChangedListener() {
        @Override
        public void onLocationChanged(Location location) {
            if (!positionFound) {
                courtController.findClosestCourt(getCourtsRequestCallbacks, location.getLatitude(), location.getLongitude());
            }
        }
    };

    private OnItemClickListener onItemClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
            Court court = (Court) ((CourtAdapter) gridView.getAdapter()).getItem(position);
            ActivityOptions opts = ActivityOptions.makeScaleUpAnimation(view, 0, 0, view.getWidth(), view.getHeight());
            startActivity(DetailActivity.newIntent(getActivity(), court.getCourtId()), opts.toBundle());
        }
    };
}
