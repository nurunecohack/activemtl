package com.nurun.activemtl.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nurun.activemtl.R;
import com.nurun.activemtl.ui.fragment.ProfileFragment.Area;

public class StatFragment extends Fragment {

    private static final String EXTRA_AREA = "EXTRA_AREA";

    public static Fragment newInstance(Area area) {
        StatFragment statFragment = new StatFragment();
        Bundle bundle= new Bundle();
        bundle.putSerializable(EXTRA_AREA, area);
        statFragment.setArguments(bundle);
        return statFragment;
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.stat_fragment, null);
        return view;
    }
}
