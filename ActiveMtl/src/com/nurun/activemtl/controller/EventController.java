package com.nurun.activemtl.controller;

import com.nurun.activemtl.http.GetEventsRequestCallbacks;

public interface EventController {

    void findClosestEvents(GetEventsRequestCallbacks callbacks, double latitude, double longitude, int distanceInKm);

    void findClosestEvents(GetEventsRequestCallbacks callbacks, double latitude, double longitude);

    void canceltasks();

    void addSuggestedEvent(String name, String fileUri, double[] latLong);
}
