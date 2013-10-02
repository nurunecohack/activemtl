package com.nurun.activemtl.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.nurun.activemtl.R;

public class SuggestionActivity extends FragmentActivity {

    public static Intent newIntent(Context context) {
        return new Intent(context, SuggestionActivity.class);
    }

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.suggestion_activity);
    }

}
