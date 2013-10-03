package com.nurun.activemtl.ui.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.nurun.activemtl.ActiveMtlConfiguration;
import com.nurun.activemtl.R;
import com.nurun.activemtl.model.EventType;

public class EventFragment extends Fragment {

    private static final String EXTRA_EVENT_TYPE = "EXTRA_EVENT_TYPE";

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        WebView webview = (WebView) inflater.inflate(R.layout.event_fragment, container, false);
        webview.loadUrl(ActiveMtlConfiguration.getInstance(getActivity()).getHomeListUrl());
        WebSettings settings = webview.getSettings();
        settings.setJavaScriptEnabled(true);
        webview.setWebChromeClient(new WebChromeClient());
        webview.setWebViewClient(new WebViewClient());
        return webview;
    }

    public static Fragment newFragment(EventType eventType) {
        EventFragment fragment = new EventFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_EVENT_TYPE, eventType);
        fragment.setArguments(bundle);
        return fragment;
    }
}
