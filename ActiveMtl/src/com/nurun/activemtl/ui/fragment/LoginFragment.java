package com.nurun.activemtl.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.nurun.activemtl.R;
import com.nurun.activemtl.ui.LoginActivity;

public class LoginFragment extends Fragment {

	private View loginFacebookButton;
	private View loginGoogleButton;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.login, container, false);
		loginFacebookButton = view.findViewById(R.id.sign_in_button_facebook);
		loginFacebookButton.setOnClickListener(onClickListener);
		loginGoogleButton = view.findViewById(R.id.sign_in_button_google);
		loginGoogleButton.setOnClickListener(onClickListener);
		return view;
	}

	private OnClickListener onClickListener = new OnClickListener() {
		@Override
		public void onClick(View view) {
			switch (view.getId()) {
			case R.id.sign_in_button_facebook:
				((LoginActivity) getActivity()).onFacebookConnectionClicked();
				break;
			case R.id.sign_in_button_google:
				((LoginActivity) getActivity()).onGooglePlusConnectionClicked();
				break;
			default:
				break;
			}
		}
	};
}