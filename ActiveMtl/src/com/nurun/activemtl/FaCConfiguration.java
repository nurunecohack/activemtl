package com.nurun.activemtl;

import java.io.IOException;
import java.util.Locale;
import java.util.Properties;

import android.content.Context;
import android.content.res.Resources.NotFoundException;

public class FaCConfiguration {

    private final Properties properties = new Properties();
    private static FaCConfiguration faCConfiguration;

    private FaCConfiguration(Context context) throws NotFoundException, IOException {
        properties.load(context.getResources().openRawResource(R.raw.config));
    }

    public static FaCConfiguration getInstance(Context context) {
        if (faCConfiguration == null) {
            try {
                faCConfiguration = new FaCConfiguration(context);
            } catch (Exception e) {
                throw new RuntimeException("Invalid config file");
            }
        }
        return faCConfiguration;
    }

    public int getTimeOut() {
        return Integer.parseInt(properties.getProperty("service.timeout"));
    }

    public long getSplashScreenDuration() {
        return Long.parseLong(properties.getProperty("splash.duration"));
    }

    /*********/
    /** Play */
    /*********/

    private String getPlayServerUrl() {
        return properties.getProperty("server.base.play.url");
    }

    public String getPlayLoginUrl() {
        return getPlayServerUrl() + properties.getProperty("service.login");
    }

    public String getPlayClosestCourtUrl(double latitude, double longitude, int radius) {
        String property = String.format(Locale.US, properties.getProperty("service.play.closestCourt"), latitude, longitude, radius);
        return getPlayServerUrl() + property;
    }

    public String getPlayPostPlayerToCourtUrl(String courtId, String playerEmail) {
        String property = String.format(properties.getProperty("service.play.addPlayer"), courtId, playerEmail);
        return getPlayServerUrl() + property;
    }

    public String getPlayDeletePlayerToCourtUrl(String courtId, String playerEmail) {
        return getPlayPostPlayerToCourtUrl(courtId, playerEmail);
    }

    public String getPlayDeleteCourtUrl(String courtId) {
        return getPlayServerUrl() + String.format(properties.getProperty("service.play.delete.court"), courtId);
    }

    public String getPlaySuggestCourtUrl() {
        return getPlayServerUrl() + properties.getProperty("service.play.suggest.court");
    }

    /***********/
    /** Google */
    /***********/

    private String getCourtEndpoint() {
        return properties.getProperty("service.google.endpoint.court");
    }

    private String getGoogleServerUrl() {
        return properties.getProperty("server.base.google.url");
    }

    public String getGoogleSuggestCourtUrl() {
        return getGoogleServerUrl() + getCourtEndpoint() + properties.getProperty("service.google.court");
    }

    public String getGoogleClosestCourtUrl(double latitude, double longitude, int radius) {
        String property = String.format(Locale.US, properties.getProperty("service.google.closest.court"), latitude, longitude, radius);
        return getGoogleServerUrl() + getCourtEndpoint() + property;
    }
    
    public String getGoogleDeleteCourtUrl(String courtId) {
        return getGoogleServerUrl() + getCourtEndpoint() + String.format(properties.getProperty("service.google.delete.court"), courtId);
    }

}
