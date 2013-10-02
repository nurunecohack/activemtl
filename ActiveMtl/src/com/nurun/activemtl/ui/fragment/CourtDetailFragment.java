package com.nurun.activemtl.ui.fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.location.LocationClient;
import com.nurun.activemtl.FaCApplication;
import com.nurun.activemtl.R;
import com.nurun.activemtl.data.CourtCursorReader;
import com.nurun.activemtl.util.DistanceUtil;
import com.squareup.picasso.Picasso;

public class CourtDetailFragment extends Fragment {
    private ImageView imageView;
    private TextView textName;
    private TextView textDistance;
    private TextView textPlayerCount;
    private TextView textAdress;
    private TextView textSuggestedBy;

    private static final String EXTRA_NAME = "EXTRA_NAME";
    private static final String EXTRA_PICTUREURL = "EXTRA_PICTUREURL";
    private static final String EXTRA_PLAYERCOUNT = "EXTRA_PLAYERCOUNT";
    private static String EXTRA_ADDRESS = "EXTRA_ADDRESS";
    private static final String EXTRA_SUGGESTED_BY = "EXTRA_SUGGESTED_BY";
    private static final String EXTRA_LONGITUDE = "EXTRA_LONGITUDE";
    private static final String EXTRA_LATITUDE = "EXTRA_LATITUDE";
    private LocationClient locationClient;

    public static CourtDetailFragment newInstance(Cursor courtCursor) {
        CourtDetailFragment f = new CourtDetailFragment();
        Bundle args = new Bundle();
        args.putString(EXTRA_NAME, CourtCursorReader.getName(courtCursor));
        args.putString(EXTRA_ADDRESS, CourtCursorReader.getAddress(courtCursor));
        args.putInt(EXTRA_PLAYERCOUNT, CourtCursorReader.getPlayerCount(courtCursor));
        args.putString(EXTRA_PICTUREURL, CourtCursorReader.getPictureUrl(courtCursor));
        args.putString(EXTRA_SUGGESTED_BY, CourtCursorReader.getSuggestedBy(courtCursor));
        args.putDouble(EXTRA_LATITUDE, CourtCursorReader.getLatitude(courtCursor));
        args.putDouble(EXTRA_LONGITUDE, CourtCursorReader.getLongitude(courtCursor));
        f.setArguments(args);
        return f;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        locationClient = (LocationClient) getActivity().getApplicationContext().getSystemService(FaCApplication.LOCATION_CLIENT);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.court_detail_fragment, container, false);
        imageView = (ImageView) rootView.findViewById(R.id.imageView1);
        textName = (TextView) rootView.findViewById(R.id.court_name);
        textDistance = (TextView) rootView.findViewById(R.id.court_distance);
        textPlayerCount = (TextView) rootView.findViewById(R.id.player_count);
        textAdress = (TextView) rootView.findViewById(R.id.address);
        textSuggestedBy = (TextView) rootView.findViewById(R.id.suggestedBy);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        Picasso.with(getActivity()).load(getArguments().getString(EXTRA_PICTUREURL)).placeholder(R.drawable.basketball_court).into(imageView);
        textName.setText(getArguments().getString(EXTRA_NAME));
        textDistance.setText(DistanceUtil.computeDistance(locationClient.getLastLocation(), getArguments().getDouble(EXTRA_LATITUDE),
                getArguments().getDouble(EXTRA_LONGITUDE)));
        textPlayerCount.setText("" + getArguments().getInt(EXTRA_PLAYERCOUNT));
        textAdress.setText(getArguments().getString(EXTRA_ADDRESS));
        textSuggestedBy.setText(String.format(getString(R.string.discovered_by), getArguments().getString(EXTRA_SUGGESTED_BY)));
    }

}