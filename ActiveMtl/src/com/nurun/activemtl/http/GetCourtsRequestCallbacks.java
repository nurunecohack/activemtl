package com.nurun.activemtl.http;

import com.nurun.activemtl.model.CourtList;

public interface GetCourtsRequestCallbacks {

    void onGetCourtsRequestComplete(CourtList courts);

    void onGetCourtsRequestFailed(RuntimeException runtimeException);

}
