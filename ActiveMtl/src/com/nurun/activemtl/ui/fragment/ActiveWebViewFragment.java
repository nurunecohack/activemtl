package com.nurun.activemtl.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.nurun.activemtl.R;
import com.nurun.activemtl.ui.DetailActivity;

public class ActiveWebViewFragment extends Fragment {

    private boolean listShown = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final LinearLayout view = (LinearLayout) inflater.inflate(R.layout.webview, container);
        final WebView webview = (WebView) view.findViewById(R.id.webview);
        webview.loadUrl("http://www.google.ca");
        WebSettings settings = webview.getSettings();
        settings.setJavaScriptEnabled(true);
        webview.addJavascriptInterface(this, "Android");
        webview.setWebChromeClient(new WebChromeClient());
        webview.setWebViewClient(new WebViewClient());
        View headerView = view.findViewById(R.id.headerView);
        headerView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (listShown) {
                    TranslateAnimation anim = new TranslateAnimation(0, 0, 0, 750);
                    anim.setDuration(750);
                    anim.setFillEnabled(true);
                    anim.setAnimationListener(new AnimationListener() {

                        @Override
                        public void onAnimationStart(Animation animation) {
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            Log.i(getClass().getSimpleName(), "webview.getMeasuredWidth() : " + webview.getMeasuredWidth());
                            Log.i(getClass().getSimpleName(), "webview.getMeasuredHeight() : " + webview.getMeasuredHeight());
                            webview.layout(0, 1000, webview.getMeasuredWidth(), webview.getMeasuredHeight() + 1000);
                        }
                    });
                    webview.startAnimation(anim);
                    listShown = false;
                }
            }
        });
        webview.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP && !listShown) {
                    TranslateAnimation anim = new TranslateAnimation(0, 0, 0, -750);
                    anim.setDuration(750);
                    anim.setFillAfter(true);
                    anim.setAnimationListener(new AnimationListener() {

                        @Override
                        public void onAnimationStart(Animation animation) {
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            webview.layout(0, 75, webview.getMeasuredWidth(), webview.getMeasuredHeight());
                        }
                    });
                    webview.startAnimation(anim);
                    listShown = true;
                }
                return false;
            }
        });
        return view;
    }
    
    @JavascriptInterface
    public void openDetail(String id) {
        getActivity().startActivity(DetailActivity.newIntent(getActivity(), id));
    }
}
