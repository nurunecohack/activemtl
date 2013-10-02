package com.nurun.activemtl.http;

import com.nurun.activemtl.model.Court;

public interface AddPlayerToCourtRequestCallbacks {

    void onAddPlayerSuccess(Court court);

    void onAddPlayerFail(RuntimeException runtimeException);

}
