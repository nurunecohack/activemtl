package com.nurun.activemtl;

import java.io.IOException;
import java.util.Properties;

import android.content.Context;
import android.content.res.Resources.NotFoundException;
import android.text.TextUtils;

import com.nurun.activemtl.model.EventType;

public class ActiveMtlConfiguration {

    private final Properties properties = new Properties();
    private static ActiveMtlConfiguration faCConfiguration;

    private ActiveMtlConfiguration(Context context) throws NotFoundException, IOException {
        properties.load(context.getResources().openRawResource(R.raw.config));
    }

    public static ActiveMtlConfiguration getInstance(Context context) {
        if (faCConfiguration == null) {
            try {
                faCConfiguration = new ActiveMtlConfiguration(context);
            } catch (Exception e) {
                throw new RuntimeException("Invalid config file");
            }
        }
        return faCConfiguration;
    }

    public String getHomeListUrl() {
        return getBaseUrl() + properties.getProperty("homelist.url");
    }

    public String getListUrl(EventType eventType, double lat, double lon) {
        switch (eventType) {
        case Challenge:
            return getBaseUrl() + String.format(properties.getProperty("challengelist.url"), lat, lon);
        case Idea:
            return getBaseUrl() + String.format(properties.getProperty("idealist.url"), lat, lon);
        case Alert:
            return getBaseUrl() + String.format(properties.getProperty("issuelist.url"), lat, lon);
        default:
            return getHomeListUrl();
        }
    }

    public String getDetailUrl(Context context, String id) {
        String url = getBaseUrl() + String.format(properties.getProperty("detail.url"), id);
        String userId = PreferenceHelper.getUserId(context);
        if (TextUtils.isEmpty(userId)) {
            return url;
        }
        return url += "?user=" + userId;
    }

    private String getBaseUrl() {
        return properties.getProperty("base.url");
    }

    public String getListUrl(EventType eventType) {
        switch (eventType) {
        case Challenge:
            return getBaseUrl() + properties.getProperty("challengelist.default.url.default");
        case Idea:
            return getBaseUrl() + properties.getProperty("idealist.default.url");
        case Alert:
            return getBaseUrl() + properties.getProperty("issuelist.default.url");
        default:
            return getHomeListUrl();
        }
    }

    public String getFacebookProfilePictureUrl(Context context) {
        return String.format(properties.getProperty("profile.picture.facebook.url"), PreferenceHelper.getUserId(context));
    }

    public int getDistrictRadius() {
        return Integer.parseInt(properties.getProperty("district.radius"));
    }

    public String getGooleProfilePictureUrl(Context context) {
        return String.format(properties.getProperty("profile.picture.google.url"), PreferenceHelper.getUserId(context));
    }
}
