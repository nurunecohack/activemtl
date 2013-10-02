package com.nurun.activemtl.http;

import com.nurun.activemtl.model.Court;

public interface AddCourtRequestCallbacks {

    void onAddCourtSuccess(Court court);

    void onAddCourtFail(RuntimeException runtimeException);
    
}
