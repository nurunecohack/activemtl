package com.nurun.activemtl.ui.fragment;

import android.support.v4.app.Fragment;


public class FragmentFactory {

    public static Fragment getFragment(int position) {
        switch (position) {
        case 0:
            return ActiveMapFragment.newFragment();
        case 4:
            return PreferencesFragment.newFragment();
        default:
            return MainFragment.newFragment();
        }
    }

}
