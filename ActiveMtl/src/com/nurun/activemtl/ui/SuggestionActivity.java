package com.nurun.activemtl.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.nurun.activemtl.R;
import com.nurun.activemtl.ui.fragment.SuggestionFragment;

public class SuggestionActivity extends FragmentActivity {

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, SuggestionActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return intent;
    }

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.suggestion_activity);
        getSupportFragmentManager().beginTransaction().replace(R.id.suggestion_frame, SuggestionFragment.newFragment()).commit();
    }

}
