package com.nurun.activemtl.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.PreferenceActivity;

import com.nurun.activemtl.PreferenceHelper;
import com.nurun.activemtl.R;

public class PreferencesActivity extends PreferenceActivity implements OnSharedPreferenceChangeListener {

    public static final String KEY_MARKER = "marker_key";

    @SuppressWarnings("deprecation")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.prefs);
        updateSummary(PreferenceHelper.KEY_PREF_RADIUS);
        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    public static Intent newIntent(Context context) {
        return new Intent(context, PreferencesActivity.class);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (!KEY_MARKER.equals(key)) {
            updateSummary(key);
        }
    }

    @SuppressWarnings("deprecation")
    private void updateSummary(String key) {
        float value = PreferenceHelper.getRadius(this);
        getPreferenceManager().findPreference(key).setSummary("" + value);
    }
}
