package com.nurun.activemtl.http;

import com.nurun.activemtl.model.Court;

public interface DeletePlayerToCourtRequestCallbacks {

    public void onDeletePlayerSuccess(Court court);

    public void onDeletePlayerFail(RuntimeException runtimeException);

}
