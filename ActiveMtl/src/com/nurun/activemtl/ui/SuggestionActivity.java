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

	public static Intent newIntent(Context context) {
		Intent intent = new Intent(context, SuggestionActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		return intent;
	}

	private EventType eventType;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.suggestion_activity);
		getSupportFragmentManager()
				.beginTransaction()
				.replace(R.id.suggestion_frame,
						SuggestionFragment.newFragment()).commit();
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
