package com.nurun.activemtl.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.nurun.activemtl.R;
import com.nurun.activemtl.model.EventType;

public class SuggestionFragment extends Fragment {

    public static Fragment newFragment() {
        return new SuggestionFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.suggestion_fragment, container, false);
        view.findViewById(R.id.viewSubmitIdea).setOnClickListener(onClickListener);
        view.findViewById(R.id.viewSubmitAlert).setOnClickListener(onClickListener);
        view.findViewById(R.id.viewSubmitChallenge).setOnClickListener(onClickListener);
        return view;
    }


    private OnClickListener onClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            Fragment fragment = getConfiguredFragment(v);
            getFragmentManager().beginTransaction().replace(R.id.suggestion_frame, fragment).commit();
        }

        private Fragment getConfiguredFragment(View v) {
            switch (v.getId()) {
            case R.id.viewSubmitIdea:
                return FormFragment.newFragment(EventType.Idea);
            case R.id.viewSubmitChallenge:
                return FormFragment.newFragment(EventType.Challenge);
            case R.id.viewSubmitAlert:
                return FormFragment.newFragment(EventType.Alert);
            default:
                throw new IllegalStateException("Wrong click");
            }
        }
    };

}
