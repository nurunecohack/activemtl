package com.nurun.activemtl.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.nurun.activemtl.R;
import com.nurun.activemtl.model.EventType;
import com.nurun.activemtl.ui.fragment.SuggestionFragment;
import com.nurun.activemtl.util.NavigationUtil;

public class SuggestionActivity extends FragmentActivity {

	private static final String EXTRA_EVENT_TYPE = "EXTRA_EVENT_TYPE";

	private EventType eventType;

	public static Intent newIntent(Context context) {
		Intent intent = new Intent(context, SuggestionActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		return intent;
	}

	public static Intent newIntent(Context context, EventType eventType) {
		Intent intent = newIntent(context);
		intent.putExtra(EXTRA_EVENT_TYPE, eventType);
		return intent;
	}

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.suggestion_activity);
		EventType eventType = (EventType) getIntent().getSerializableExtra(
				EXTRA_EVENT_TYPE);
		if (eventType == null) {
			getSupportFragmentManager()
					.beginTransaction()
					.replace(R.id.suggestion_frame,
							SuggestionFragment.newFragment()).commit();
		} else {
			NavigationUtil.goToFormFragment(this, getSupportFragmentManager(),
					eventType);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		if (requestCode == NavigationUtil.requestLoginCode && resultCode == 200) {
			NavigationUtil.goToFormFragment(this, getSupportFragmentManager(),
					eventType);
		}
	}

	public void setEventType(EventType eventType) {
		this.eventType = eventType;
	}

}
