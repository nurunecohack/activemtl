package com.nurun.activemtl.controller;

import android.location.Location;

import com.nurun.activemtl.callback.GetEventStatCallback;
import com.nurun.activemtl.callback.GetEventsRequestCallbacks;
import com.nurun.activemtl.model.EventType;

public interface EventController {

    void findClosestEvents(GetEventsRequestCallbacks callbacks, double latitude, double longitude, int distanceInKm);

    void findClosestEvents(GetEventsRequestCallbacks callbacks, double latitude, double longitude);

    void canceltasks();

    void addSuggestedEvent(String name, String description, String fileUri, double[] latLong, EventType eventType, String category);

    void getChallengeForMe(GetEventStatCallback callback);

    void getAlertForMe(GetEventStatCallback callback);

    void getIdeaForMe(GetEventStatCallback callback);

    void getAlertForDistrict(GetEventStatCallback callback, Location location);

    void getIdeaForDistrict(GetEventStatCallback callback, Location location);

    void getChallengeForDistrict(GetEventStatCallback callback, Location location);

    void getAlertForCity(GetEventStatCallback callback);

    void getIdeaForCity(GetEventStatCallback callback);

    void getChallengeForCity(GetEventStatCallback callback);
}
