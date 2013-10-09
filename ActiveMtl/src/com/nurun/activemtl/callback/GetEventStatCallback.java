package com.nurun.activemtl.callback;


public interface GetEventStatCallback {

    void onEventStatComplete(int count);

    void onEventStatFailed(RuntimeException runtimeException);

}
