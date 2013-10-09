package com.nurun.activemtl.callback;

import com.nurun.activemtl.model.parse.Event;

public interface AddPlayerToCourtRequestCallbacks {

    void onAddPlayerSuccess(Event court);

    void onAddPlayerFail(RuntimeException runtimeException);

}
