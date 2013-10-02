package com.nurun.activemtl.model;

import java.io.Serializable;

import android.location.Location;

public interface Court extends Serializable {

	String getCourtId();
	
    String getTitle();

    Integer getPlayerCount();

    String getPictureUrl();

    String getAddress();

    String getSuggestedBy();

    double getDistance(Location currentLocation);

    double[] getLatLng();

}
