package com.nurun.activemtl.http;

import com.nurun.activemtl.model.parse.Event;

public interface SuggestCourtRequestCallbacks {

    public void onSuggestCourtFail(RuntimeException runtimeException);

    public void onSuggestCourtSuccess(Event court);


}
