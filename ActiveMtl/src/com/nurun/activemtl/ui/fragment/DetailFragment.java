package com.nurun.activemtl.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.nurun.activemtl.ActiveMtlConfiguration;
import com.nurun.activemtl.R;

public class DetailFragment extends Fragment {

    private static final String ITEM_ID = "ITEM_ID";

    public static DetailFragment newInstance(String id) {
        DetailFragment fragment = new DetailFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ITEM_ID, id);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        WebView webView = (WebView) inflater.inflate(R.layout.event_fragment, container, false);
        webView.loadUrl(ActiveMtlConfiguration.getInstance(getActivity()).getDetailUrl(getArguments().getString(ITEM_ID)));
        return webView;
    }
}