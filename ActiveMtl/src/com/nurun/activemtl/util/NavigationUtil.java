package com.nurun.activemtl.util;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.nurun.activemtl.PreferenceHelper;
import com.nurun.activemtl.R;
import com.nurun.activemtl.model.EventType;
import com.nurun.activemtl.ui.DetailActivity;
import com.nurun.activemtl.ui.HomeActivity;
import com.nurun.activemtl.ui.LoginActivity;
import com.nurun.activemtl.ui.fragment.EventListFragment;
import com.nurun.activemtl.ui.fragment.ExplainFragment;
import com.nurun.activemtl.ui.fragment.FormFragment;
import com.nurun.activemtl.ui.fragment.HomeFragment;
import com.nurun.activemtl.ui.fragment.ProfileFragment;
import com.nurun.activemtl.ui.fragment.SuggestionFragment;

public class NavigationUtil {

	public enum NextScreen {
		Profile, Suggest_Idea, Suggest_Challenge, Suggest_Alert
	}

	public static final int requestLoginCode = 654;

	public static void goToHome(Context context) {
		context.startActivity(HomeActivity.newIntent(context));
	}

	public static Fragment getFragment(int position) {
		switch (position) {
		case 0:
			return HomeFragment.newFragment();
		case 1:
			return EventListFragment.newFragment(EventType.Challenge);
		case 2:
			return EventListFragment.newFragment(EventType.Alert);
		case 3:
			return EventListFragment.newFragment(EventType.Idea);
		default:
			throw new IllegalStateException("Not yet implemented");
		}
	}

	public static EventType getEventType(int position) {
		switch (position) {
		case 1:
			return EventType.Challenge;
		case 2:
			return EventType.Alert;
		case 3:
			return EventType.Idea;
		default:
			return null;
		}
	}

	public static void goToDetail(Context context, String eventId) {
		context.startActivity(DetailActivity.newIntent(context, eventId));
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

	public static void goToFormFragment(FragmentActivity activity,
			FragmentManager fragmentManager, EventType eventType) {
		if (PreferenceHelper.isLoggedIn(activity)) {
			if (eventType == EventType.Alert) {
				goToFragment(fragmentManager, R.id.suggestion_frame,
						ExplainFragment.newFragment(eventType));
			} else if (eventType == EventType.Challenge) {
				goToFragment(fragmentManager, R.id.suggestion_frame,
						SuggestionFragment.newFragment());
			} else {
				goToFragment(fragmentManager, R.id.suggestion_frame,
						FormFragment.newFragment(eventType));
			}
		} else {
			activity.startActivityForResult(LoginActivity.newIntent(activity),
					requestLoginCode);
		}
	}

	public static void goToProfile(Activity activity,
			FragmentManager fragmentManager) {
		if (PreferenceHelper.isLoggedIn(activity)) {
			goToFragment(fragmentManager, R.id.content_frame,
					ProfileFragment.newFragment());
		} else {
			activity.startActivityForResult(LoginActivity.newIntent(activity),
					requestLoginCode);
		}
	}

	public static void handleMenuClick(HomeActivity activity, int position) {
		FragmentManager fragmentManager = activity.getSupportFragmentManager();
		if (position == 4) {
			NavigationUtil.goToProfile(activity, fragmentManager);
		} else {
			Fragment fragment = getFragment(position);
			goToFragment(fragmentManager, R.id.content_frame, fragment,
					getEventType(position));
		}
	}

	public static void goToFragment(FragmentManager fragmentManager,
			int frameId, Fragment fragment, EventType eventType) {
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		Fragment mapFragment = fragmentManager
				.findFragmentById(R.id.map_fragment);
		if (mapFragment != null) {
			transaction.remove(mapFragment);
		}
		Fragment listFragment = fragmentManager
				.findFragmentById(R.id.list_fragment);
		if (listFragment != null) {
			transaction.remove(listFragment);
		}
		if (eventType == null) {
			transaction.replace(frameId, fragment).commit();
		} else {
			transaction.replace(frameId, fragment, eventType.name()).commit();
		}
	}

	public static Fragment getFragment(int position, boolean isLoggedIn) {
		switch (position) {
		case 0:
			return HomeFragment.newFragment();
		case 1:
			return EventListFragment.newFragment(EventType.Challenge);
		case 2:
			return EventListFragment.newFragment(EventType.Alert);
		case 3:
			return EventListFragment.newFragment(EventType.Idea);
		default:
			throw new IllegalStateException("Not yet implemented");
		}
	}

	public static void handleMenuClick(FragmentActivity activity, int position) {
		FragmentManager fragmentManager = activity.getSupportFragmentManager();
		if (position == 4) {
			NavigationUtil.goToProfile(activity, fragmentManager);
		} else {
			Fragment fragment = getFragment(position,
					PreferenceHelper.isLoggedIn(activity));
			goToFragment(fragmentManager, R.id.content_frame, fragment);
		}
	}

	private static void goToFragment(FragmentManager fragmentManager,
			int contentFrame, Fragment newFragment) {
		goToFragment(fragmentManager, contentFrame, newFragment, null);
	}

}
