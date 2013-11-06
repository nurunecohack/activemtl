package com.nurun.activemtl.ui.fragment;

import android.annotation.SuppressLint;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.nurun.activemtl.ActiveMtlConfiguration;
import com.nurun.activemtl.R;
import com.nurun.activemtl.util.NavigationUtil;

public class ActiveWebViewFragment extends Fragment {

    private boolean listShown = true;
    private WebView webview;
    private float initialPosition;
    private int OFFSET;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final ViewGroup view = (ViewGroup) inflater.inflate(R.layout.webview, container);
        webview = (WebView) view.findViewById(R.id.webview);
        WebSettings settings = webview.getSettings();
        settings.setJavaScriptEnabled(true);
        webview.addJavascriptInterface(this, "ActiveMTL");
        webview.setWebChromeClient(new WebChromeClient());
        webview.setWebViewClient(new WebViewClient());
        view.findViewById(R.id.headerView).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listShown) {
                    slideWebview();
                }
            }
        });
        webview.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP && !listShown) {
                    slideWebview();
                }
                return false;
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        webview.loadUrl(ActiveMtlConfiguration.getInstance(getActivity()).getHomeListUrl());
        OFFSET = getActivity().getResources().getInteger(R.integer.offset);
        Log.i(getClass().getSimpleName(), "Offset = " + OFFSET);
    }

    @JavascriptInterface
    public void openDetail(String id) {
        NavigationUtil.goToDetail(getActivity(), id);
    }

    private void slideWebview() {
        final TranslateAnimation anim;
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        final Point size = new Point();
        display.getSize(size);
        if (listShown) {
            anim = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0, Animation.ABSOLUTE,
                    OFFSET);
            anim.setAnimationListener(new AnimationListener() {

                @Override
                public void onAnimationStart(Animation animation) {
                    initialPosition = webview.getY();
                    Log.i("POSITION", "Down start : " + webview.getY());
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    // fix flicking
                    // Source : http://stackoverflow.com/questions/9387711/android-animation-flicker
                    TranslateAnimation anim = new TranslateAnimation(0.0f, 0.0f, 0.0f, 0.0f);
                    anim.setDuration(1);
                    webview.startAnimation(anim);
                    // headerView.setVisibility(View.GONE);
                    webview.setY(initialPosition + OFFSET);
                    Log.i("POSITION", "Down stop : " + webview.getY());
                }
            });
            anim.setInterpolator(new OvershootInterpolator(1.15f));
        } else {
            // anim = new TranslateAnimation(0.0f, 0.0f, 750.0f + headerView.getHeight(), 0.0f +
            // headerView.getHeight());
            anim = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0, Animation.ABSOLUTE,
                    -OFFSET);
            anim.setAnimationListener(new AnimationListener() {

                @Override
                public void onAnimationStart(Animation animation) {
                    Log.i("POSITION", "Up  start : " + webview.getY());
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    // headerView.setVisibility(View.VISIBLE);
                    // fix flicking
                    // Source : http://stackoverflow.com/questions/9387711/android-animation-flicker
                    TranslateAnimation anim = new TranslateAnimation(0.0f, 0.0f, 0.0f, 0.0f);
                    anim.setDuration(1);
                    webview.startAnimation(anim);
                    webview.setY(initialPosition);
                    Log.i("POSITION", "Up stop : " + webview.getY());
                }
            });
            anim.setInterpolator(new OvershootInterpolator(1.15f));

        }
        anim.setDuration(750);
        webview.startAnimation(anim);

        listShown = !listShown;
    }
}
