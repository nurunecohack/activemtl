package com.nurun.activemtl.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.nurun.activemtl.R;
import com.nurun.activemtl.model.EventType;
import com.nurun.activemtl.ui.SuggestionActivity;
import com.nurun.activemtl.util.NavigationUtil;

public class SuggestionFragment extends Fragment {

    public static Fragment newFragment() {
        return new SuggestionFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.suggestion_fragment, container, false);
        view.findViewById(R.id.viewSubmitIdea).setOnClickListener(onClickListener);
        view.findViewById(R.id.viewSubmitAlert).setOnClickListener(onClickListener);
        return view;
    }

    private OnClickListener onClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            EventType eventType = getEventTypeFromView(v);
        	((SuggestionActivity)getActivity()).setEventType(eventType);
			NavigationUtil.goToFormFragment(getActivity(), getFragmentManager(), eventType);
        }
        
        private EventType getEventTypeFromView(View v) {
            switch (v.getId()) {
            case R.id.viewSubmitIdea:
                return EventType.Idea;
            case R.id.viewSubmitAlert:
                return EventType.Alert;
            default:
                throw new IllegalStateException("Wrong click");
            }
        }

    };

}
