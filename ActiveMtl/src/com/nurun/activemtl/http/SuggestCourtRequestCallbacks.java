package com.nurun.activemtl.http;

import com.nurun.activemtl.model.Court;

public interface SuggestCourtRequestCallbacks {

    public void onSuggestCourtFail(RuntimeException runtimeException);

    public void onSuggestCourtSuccess(Court court);


}
