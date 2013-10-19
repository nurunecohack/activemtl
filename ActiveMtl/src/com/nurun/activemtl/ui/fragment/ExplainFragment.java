package com.nurun.activemtl.ui.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.nurun.activemtl.R;
import com.nurun.activemtl.model.EventType;
import com.nurun.activemtl.util.NavigationUtil;


/**
 * A simple {@link android.support.v4.app.Fragment} subclass. Activities that
 * contain this fragment must implement the
 * {@link ExplainFragment.OnFragmentInteractionListener} interface to handle
 * interaction events. Use the {@link ExplainFragment#newInstance} factory
 * method to create an instance of this fragment.
 * 
 */
public class ExplainFragment extends Fragment {

    private static final String EXTRA_EVENT_TYPE = "EXTRA_EVENT_TYPE";

    public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;

    protected Uri fileUri;
    
	private Button photoButton;
	
    public static Fragment newFragment(EventType eventType) {
        ExplainFragment fragment = new ExplainFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_EVENT_TYPE, eventType);
        fragment.setArguments(bundle);
        return fragment;
    }
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.explain_fragment, container, false);
        setHasOptionsMenu(true);

    	photoButton = (Button) view.findViewById(R.id.buttonPicture);
    	photoButton.setOnClickListener(onClickListener);
        getActivity().setTitle(getTitle());
        return view;
    }

    private OnClickListener onClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
            case R.id.buttonPicture:
                NavigationUtil.goToFragment(getFragmentManager(), R.id.suggestion_frame, FormFragment.newFragment(EventType.Alert));
                break;
            default:
                break;
            }
        }
    };

    
    private EventType getEventType() {
        return (EventType) getArguments().getSerializable(EXTRA_EVENT_TYPE);
    }
    
    private int getTitle() {
        switch (getEventType()) {
        case Alert:
            return R.string.submit_alert;
        case Challenge:
            return R.string.submit_challenge;
        case Idea:
            return R.string.submit_idea;
        default:
            throw new IllegalStateException("Wrong event type : " + getEventType());
        }
    }
   
    
}
