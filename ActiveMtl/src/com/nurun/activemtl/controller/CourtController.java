package com.nurun.activemtl.controller;

import com.nurun.activemtl.http.GetCourtsRequestCallbacks;

public interface CourtController {

    void findClosestCourt(GetCourtsRequestCallbacks callbacks, double latitude, double longitude, int distanceInKm);

    void findClosestCourt(GetCourtsRequestCallbacks callbacks, double latitude, double longitude);

    void canceltasks();

    void addSuggestedCourt(String name, String fileUri, double[] latLong, String[] address);
}
