package com.nurun.activemtl;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PreferenceHelper {

    private static final String KEY_PREF_LOGIN = "login_key";
    public static final String KEY_PREF_RADIUS = "radius_key";
    private static final String KEY_PREF_CHECKED_IN = "checked_in";
    private static final String KEY_PREF_FOUND = "found";
    private static final String KEY_USER_ID = "userId";

    public static void saveLogin(Context context, String login) {
        getPreferences(context).edit().putString(KEY_PREF_LOGIN, login).commit();
    }

    public static String getLogin(Context context) {
        return getPreferences(context).getString(KEY_PREF_LOGIN, "");
    }

    private static SharedPreferences getPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static int getRadius(Context context) {
        return Integer.parseInt(getPreferences(context).getString(KEY_PREF_RADIUS, "50"));
    }

    public static void clearCache(Context context) {
        getPreferences(context).edit().clear().commit();
    }

    public static boolean isCheckedIn(Context context) {
        return getPreferences(context).getBoolean(KEY_PREF_CHECKED_IN, false);
    }

    public static boolean checkIn(Context context) {
        return getPreferences(context).edit().putBoolean(KEY_PREF_CHECKED_IN, true).commit();
    }

    public static boolean leaveCourt(Context context) {
        return getPreferences(context).edit().putBoolean(KEY_PREF_CHECKED_IN, false).commit();
    }

    public boolean isLocationFound(Context context) {
        return getPreferences(context).getBoolean(KEY_PREF_FOUND, false);
    }

    public boolean locationFound(Context context) {
        return getPreferences(context).edit().putBoolean(KEY_PREF_FOUND, true).commit();
    }

    public static String getUserId(Context context) {
        return getPreferences(context).getString(KEY_USER_ID, "andouane.bird");
    }

    public static void setUserId(Context context, String id) {
        getPreferences(context).edit().putString(KEY_USER_ID, id).commit();
    }
}
