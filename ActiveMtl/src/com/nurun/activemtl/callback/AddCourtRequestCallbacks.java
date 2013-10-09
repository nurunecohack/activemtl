package com.nurun.activemtl.callback;

import com.nurun.activemtl.model.parse.Event;

public interface AddCourtRequestCallbacks {

    void onAddCourtSuccess(Event court);

    void onAddCourtFail(RuntimeException runtimeException);
    
}
