package com.nurun.activemtl.http;

import com.nurun.activemtl.model.parse.Event;

public interface DeletePlayerToCourtRequestCallbacks {

    public void onDeletePlayerSuccess(Event court);

    public void onDeletePlayerFail(RuntimeException runtimeException);

}
