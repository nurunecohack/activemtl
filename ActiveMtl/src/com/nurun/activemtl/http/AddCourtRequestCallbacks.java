package com.nurun.activemtl.http;

import com.nurun.activemtl.model.Event;

public interface AddCourtRequestCallbacks {

    void onAddCourtSuccess(Event court);

    void onAddCourtFail(RuntimeException runtimeException);
    
}
