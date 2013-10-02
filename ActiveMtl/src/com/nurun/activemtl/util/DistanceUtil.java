package com.nurun.activemtl.util;

import java.text.DecimalFormat;

import android.location.Location;

import com.parse.ParseGeoPoint;

public class DistanceUtil {

    public static String formatDistance(double distance) {
        if (distance > 1000) {
            double dist = distance / 1000;
            return String.format("%s km", format(dist));
        }
        return String.format("%s m", format(distance));
    }

    private static String format(double distance) {
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(distance);
    }

    public static String computeDistance(ParseGeoPoint parseGeoPoint, double otherLatitude, double otherLongitude) {
        ParseGeoPoint destination = new ParseGeoPoint(otherLatitude, otherLongitude);
        double dist = parseGeoPoint.distanceInKilometersTo(destination);
        if (dist < 1) {
            dist = dist * 1000;
            return String.format("%s m", format(dist));
        } else {
            return String.format("%s km", format(dist));
        }
    }

    public static String computeDistance(Location lastLocation, double latitude, double longitude) {
        Location location = new Location("Court");
        location.setLatitude(latitude);
        location.setLongitude(longitude);
        return formatDistance((double) location.distanceTo(lastLocation));
    }

}
