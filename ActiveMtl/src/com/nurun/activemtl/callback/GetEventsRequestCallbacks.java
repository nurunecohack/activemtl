package com.nurun.activemtl.callback;

import com.nurun.activemtl.model.EventList;

public interface GetEventsRequestCallbacks {

    void onGetEventsRequestComplete(EventList courts);

    void onGetEventsRequestFailed(RuntimeException runtimeException);

}
