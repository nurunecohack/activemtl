package com.nurun.activemtl.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.nurun.activemtl.ActiveMtlConfiguration;
import com.nurun.activemtl.R;

public class CourtDetailFragment extends Fragment {

    private static final String ITEM_ID = "ITEM_ID";

    public static CourtDetailFragment newInstance(String id) {
        CourtDetailFragment f = new CourtDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ITEM_ID, id);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        WebView webView = (WebView) inflater.inflate(R.layout.court_detail_fragment, container, false);
        webView.loadUrl(ActiveMtlConfiguration.getInstance(getActivity()).getDetailUrl(getArguments().getString(ITEM_ID)));
        return webView;
    }
}