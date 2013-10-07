package com.nurun.activemtl.util;

import android.content.Context;
import android.support.v4.app.Fragment;

import com.nurun.activemtl.model.EventType;
import com.nurun.activemtl.ui.DetailActivity;
import com.nurun.activemtl.ui.HomeActivity;
import com.nurun.activemtl.ui.fragment.ActiveMapFragment;
import com.nurun.activemtl.ui.fragment.EventListFragment;
import com.nurun.activemtl.ui.fragment.ProfileFragment;

public class NavigationUtil {

    public static void goToHome(Context context) {
        context.startActivity(HomeActivity.newIntent(context));
    }

    public static Fragment getFragment(int position) {
        switch (position) {
        case 1:
            return EventListFragment.newFragment(EventType.CHALLENGE);
        case 2:
            return EventListFragment.newFragment(EventType.ISSUE);
        case 3:
            return EventListFragment.newFragment(EventType.IDEA);
        case 4:
            return ProfileFragment.newFragment();
        default:
            return ActiveMapFragment.newFragment();
        }
    }

    public static void goToDetail(Context context, String eventId) {
        context.startActivity(DetailActivity.newIntent(context, eventId));
    }
}
