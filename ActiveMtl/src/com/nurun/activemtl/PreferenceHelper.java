package com.nurun.activemtl;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

public class PreferenceHelper {

    private static final String KEY_USER_NAME = "userName_key";
    private static final String KEY_PREF_LOGIN = "login_key";
    public static final String KEY_PREF_RADIUS = "radius_key";
    private static final String KEY_PREF_CHECKED_IN = "checked_in";
    private static final String KEY_PREF_FOUND = "found";
    private static final String KEY_USER_ID = "userId";
    private static final String KEY_SOCIAL_MEDIA = "social_media";

    // private static final String KEY_PROFILE_PICTURE = "profile_picture";
    // private static final String KEY_SMALL_PROFILE_PICTURE = "small_profile_picture";

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
        return getPreferences(context).getString(KEY_USER_ID, null);
    }

    public static void setUserId(Context context, String id) {
        getPreferences(context).edit().putString(KEY_USER_ID, id).commit();
    }

    public static void setUserName(Context context, String name) {
        getPreferences(context).edit().putString(KEY_USER_NAME, name).commit();
    }

    public static String getUserName(Context context) {
        return getPreferences(context).getString(KEY_USER_NAME, null);
    }

    public static void clearUserInfos(Context context) {
        getPreferences(context).edit().remove(KEY_USER_NAME).remove(KEY_USER_ID).remove(KEY_SOCIAL_MEDIA).commit();
    }

    public static SocialMediaConnection getSocialMediaConnection(Context context) {
        return SocialMediaConnection.valueOf(getPreferences(context).getString(KEY_SOCIAL_MEDIA, null));
    }

    public static void setSocialMediaConnection(Context context, SocialMediaConnection media) {
        getPreferences(context).edit().putString(KEY_SOCIAL_MEDIA, media.name()).commit();
    }

    public static boolean isLoggedIn(Context context) {
        return !TextUtils.isEmpty(getUserId(context));
    }

    /*
     * public static void setProfilePictureUrl(Context context, String profilePictureUrl) {
     * getPreferences(context).edit().putString(KEY_PROFILE_PICTURE, profilePictureUrl).commit(); }
     */

    /*
     * public static void setSmallProfilePictureUrl(Context context, String smallGooleProfilePictureUrl) {
     * getPreferences(context).edit().putString(KEY_SMALL_PROFILE_PICTURE, smallGooleProfilePictureUrl).commit(); }
     */
}
