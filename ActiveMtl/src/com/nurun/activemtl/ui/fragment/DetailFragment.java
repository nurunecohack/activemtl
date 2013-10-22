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

@SuppressLint("JavascriptInterface")
public class DetailFragment extends Fragment {

    private static final String ITEM_ID = "ITEM_ID";
    private WebView webview;

    public static DetailFragment newInstance(String id) {
        DetailFragment fragment = new DetailFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ITEM_ID, id);
        fragment.setArguments(bundle);
        return fragment;
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	webview = (WebView) inflater.inflate(R.layout.event_fragment, container, false);
        WebSettings settings = webview.getSettings();
        settings.setJavaScriptEnabled(true);
        webview.addJavascriptInterface(this, "ActiveMTL");
        webview.setWebChromeClient(new WebChromeClient());
        webview.setWebViewClient(new WebViewClient());
        webview.loadUrl(ActiveMtlConfiguration.getInstance(getActivity()).getDetailUrl(getActivity(), getArguments().getString(ITEM_ID)));
        return webview;
    }
}