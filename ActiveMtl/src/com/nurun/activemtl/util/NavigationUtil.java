package com.nurun.activemtl.util;

import android.content.Context;
import android.support.v4.app.Fragment;

import com.nurun.activemtl.model.EventType;
import com.nurun.activemtl.ui.HomeActivity;
import com.nurun.activemtl.ui.fragment.ActiveMapFragment;
import com.nurun.activemtl.ui.fragment.EventFragment;

public class NavigationUtil {

    public static void goToHome(Context context) {
        context.startActivity(HomeActivity.newIntent(context));
    }

    public static Fragment getFragment(int position) {
        switch (position) {
        case 1:
            return EventFragment.newFragment(EventType.CHALLENGE);
        case 2:
            return EventFragment.newFragment(EventType.ISSUE);
        case 3:
            return EventFragment.newFragment(EventType.IDEA);
        default:
            return ActiveMapFragment.newFragment();
        }
    }
}
