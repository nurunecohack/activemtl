package com.nurun.activemtl.util;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;

import com.nurun.activemtl.R;
import com.nurun.activemtl.model.EventType;
import com.nurun.activemtl.ui.DetailActivity;
import com.nurun.activemtl.ui.HomeActivity;
import com.nurun.activemtl.ui.fragment.EventListFragment;
import com.nurun.activemtl.ui.fragment.FormFragment;
import com.nurun.activemtl.ui.fragment.HomeFragment;
import com.nurun.activemtl.ui.fragment.LoginFragment;
import com.nurun.activemtl.ui.fragment.ProfileFragment;

public class NavigationUtil {

    public enum NextScreen {
        Profile, Suggest_Idea, Suggest_Challenge, Suggest_Alert
    }

    public static void goToHome(Context context) {
        context.startActivity(HomeActivity.newIntent(context));
    }

    public static Fragment getFragment(int position, boolean isLoggedIn) {
        switch (position) {
        case 1:
            return EventListFragment.newFragment(EventType.Challenge);
        case 2:
            return EventListFragment.newFragment(EventType.Alert);
        case 3:
            return EventListFragment.newFragment(EventType.Idea);
        case 4:
            return isLoggedIn ? ProfileFragment.newFragment() : LoginFragment.newFragment(NextScreen.Profile);
        default:
            return HomeFragment.newFragment();
        }
    }

    public static void goToDetail(Context context, String eventId) {
        context.startActivity(DetailActivity.newIntent(context, eventId));
    }

    public static Fragment getFormFragment(View v, boolean isLoggedIn) {
        EventType eventType = getEventTypeFromView(v);
        if (isLoggedIn) {
            return FormFragment.newFragment(eventType);
        } else {
            return LoginFragment.newFragment(getNextScreenFromEventType(eventType));
        }
    }

    private static EventType getEventTypeFromView(View v) {
        switch (v.getId()) {
        case R.id.viewSubmitIdea:
            return EventType.Idea;
        case R.id.viewSubmitChallenge:
            return EventType.Challenge;
        case R.id.viewSubmitAlert:
            return EventType.Alert;
        default:
            throw new IllegalStateException("Wrong click");
        }
    }

    private static NextScreen getNextScreenFromEventType(EventType eventType) {
        switch (eventType) {
        case Idea:
            return NextScreen.Suggest_Idea;
        case Challenge:
            return NextScreen.Suggest_Challenge;
        case Alert:
            return NextScreen.Suggest_Alert;
        default:
            throw new IllegalStateException("Wrong click");
        }
    }

    public static Fragment getNextFragment(NextScreen nextScreen) {
        switch (nextScreen) {
        case Profile:
            return ProfileFragment.newFragment();
        case Suggest_Alert:
            return FormFragment.newFragment(EventType.Alert);
        case Suggest_Challenge:
            return FormFragment.newFragment(EventType.Challenge);
        case Suggest_Idea:
            return FormFragment.newFragment(EventType.Idea);
        default:
            throw new IllegalStateException("Wrong click");
        }
    }

    public static void gotoNextFragment(FragmentManager fragmentManager, NextScreen nextScreen) {
        Fragment nextFragment = NavigationUtil.getNextFragment(nextScreen);
        fragmentManager.beginTransaction().replace(getContainerId(nextScreen), nextFragment).commit();
        
    }

    private static int getContainerId(NextScreen nextScreen) {
        if (NextScreen.Profile == nextScreen) {
            return R.id.content_frame;
        }
        return R.id.suggestion_frame;
    }

    public static void goToFormFragment(FragmentManager fragmentManager, View v, boolean isLoggedIn) {
        Fragment fragment = getFormFragment(v, isLoggedIn);
        fragmentManager.beginTransaction().replace(R.id.suggestion_frame, fragment).commit();
    }
}
