package com.nurun.activemtl.ui.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.facebook.LoggingBehavior;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.Settings;
import com.facebook.model.GraphUser;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.plus.PlusClient;
import com.google.android.gms.plus.model.people.Person;
import com.nurun.activemtl.ActiveMtlConfiguration;
import com.nurun.activemtl.PreferenceHelper;
import com.nurun.activemtl.R;
import com.nurun.activemtl.SocialMediaConnection;
import com.nurun.activemtl.util.NavigationUtil;
import com.nurun.activemtl.util.NavigationUtil.NextScreen;

public class LoginFragment extends Fragment {

    private static final String EXTRA_NEXT_SCREEN = "EXTRA_NEXT_SCREEN";
    private Session.StatusCallback statusCallback = new SessionStatusCallback();
    private View loginFacebookButton;
    private View loginGoogleButton;
    private PlusClient mPlusClient;
    private ProgressDialog mConnectionProgressDialog;

    private static final int REQUEST_CODE_RESOLVE_ERR = 9000;
    protected ConnectionResult mConnectionResult;

    public static Fragment newFragment(NextScreen nextScreen) {
        LoginFragment loginFragment = new LoginFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_NEXT_SCREEN, nextScreen);
        loginFragment.setArguments(bundle);
        return loginFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login, container, false);
        loginFacebookButton = view.findViewById(R.id.sign_in_button_facebook);
        loginFacebookButton.setVisibility(View.VISIBLE);
        loginFacebookButton.setOnClickListener(onClickListener);
        loginGoogleButton = view.findViewById(R.id.sign_in_button_google);
        loginGoogleButton.setVisibility(View.VISIBLE);
        loginGoogleButton.setOnClickListener(onClickListener);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Session session = Session.getActiveSession();
        mPlusClient = new PlusClient.Builder(getActivity(), connectionCallback, connectionFailListener).setVisibleActivities(
                "http://schemas.google.com/AddActivity", "http://schemas.google.com/BuyActivity").build();
        // Barre de progression à afficher si l'échec de connexion n'est pas résolu.
        mConnectionProgressDialog = new ProgressDialog(getActivity());
        mConnectionProgressDialog.setMessage("Signing in...");
        if (mPlusClient.isConnected()) {
            mPlusClient.clearDefaultAccount();
            mPlusClient.disconnect();
        }
        if (session != null && !session.isClosed()) {
            session.closeAndClearTokenInformation();
        }
        initFacebook(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Session session = Session.getActiveSession();
        Session.saveSession(session, outState);
    }

    @Override
    public void onStart() {
        super.onStart();
        Session session = Session.getActiveSession();
        if (session != null) {
            session.addCallback(statusCallback);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int responseCode, Intent intent) {
        super.onActivityResult(requestCode, responseCode, intent);
        if (requestCode == REQUEST_CODE_RESOLVE_ERR && responseCode == FragmentActivity.RESULT_OK) {
            mConnectionResult = null;
            mPlusClient.connect();
        }
        Session.getActiveSession().onActivityResult(getActivity(), requestCode, responseCode, intent);
    }

    private void initFacebook(Bundle savedInstanceState) {
        Settings.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);
        Session session = Session.getActiveSession();
        if (session == null) {
            if (savedInstanceState != null) {
                session = Session.restoreSession(getActivity(), null, statusCallback, savedInstanceState);
            }
            if (session == null) {
                session = new Session(getActivity());
            }
            Session.setActiveSession(session);
        }
    }

    private class SessionStatusCallback implements Session.StatusCallback {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
            mConnectionProgressDialog.show();
            PreferenceHelper.setSocialMediaConnection(getActivity(), SocialMediaConnection.Facebook);
            // make request to the /me API
            Request.newMeRequest(session, new Request.GraphUserCallback() {
                // callback after Graph API response with user object
                @Override
                public void onCompleted(GraphUser user, Response response) {
                    if (user != null) {
                        PreferenceHelper.setUserId(getActivity(), user.getId());
                        PreferenceHelper.setUserName(getActivity(), user.getName());
                        PreferenceHelper.setProfilePictureUrl(getActivity(),
                                ActiveMtlConfiguration.getInstance(getActivity()).getFacebookProfilePictureUrl(getActivity()));
                        goToNextScreen();
                    }
                    mConnectionProgressDialog.dismiss();
                }
            }).executeAsync();
        }
    }

    private void goToNextScreen() {
        getFragmentManager().popBackStack();
        NavigationUtil.gotoNextFragment(getFragmentManager(), (NextScreen) getArguments().getSerializable(EXTRA_NEXT_SCREEN));
    }

    @Override
    public void onStop() {
        super.onStop();
        Session.getActiveSession().removeCallback(statusCallback);
    }

    private OnClickListener onClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
            case R.id.sign_in_button_facebook:
                Settings.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);
                Session session = Session.getActiveSession();
                if (!session.isOpened() && !session.isClosed()) {
                    session.openForRead(new Session.OpenRequest(LoginFragment.this).setCallback(statusCallback));
                } else {
                    Session.openActiveSession(getActivity(), LoginFragment.this, true, statusCallback);
                }
                break;
            case R.id.sign_in_button_google:
                if (mConnectionResult == null) {
                    mPlusClient.connect();
                } else {
                    try {
                        mConnectionProgressDialog.show();
                        mConnectionResult.startResolutionForResult(getActivity(), REQUEST_CODE_RESOLVE_ERR);
                    } catch (SendIntentException e) {
                        // Nouvelle tentative de connexion
                        mConnectionResult = null;
                        mPlusClient.connect();
                    }
                }
                break;
            default:
                break;
            }
        }
    };

    private ConnectionCallbacks connectionCallback = new ConnectionCallbacks() {

        @Override
        public void onDisconnected() {

        }

        @Override
        public void onConnected(Bundle arg0) {
            mConnectionProgressDialog.dismiss();
            Person currentPerson = mPlusClient.getCurrentPerson();
            PreferenceHelper.setSocialMediaConnection(getActivity(), SocialMediaConnection.Google_plus);
            PreferenceHelper.setUserId(getActivity(), currentPerson.getId());
            PreferenceHelper.setUserName(getActivity(), currentPerson.getDisplayName());
            PreferenceHelper.setProfilePictureUrl(getActivity(), ActiveMtlConfiguration.getInstance(getActivity()).getGooleProfilePictureUrl(getActivity()));
            goToNextScreen();
        }
    };

    private OnConnectionFailedListener connectionFailListener = new OnConnectionFailedListener() {

        @Override
        public void onConnectionFailed(ConnectionResult result) {
            if (mConnectionProgressDialog.isShowing()) {
                // L'utilisateur a déjà appuyé sur le bouton de connexion. Commencer à résoudre
                // les erreurs de connexion. Attendre jusqu'à onConnected() pour masquer la
                // boîte de dialogue de connexion.
                if (result.hasResolution()) {
                    try {
                        result.startResolutionForResult(getActivity(), REQUEST_CODE_RESOLVE_ERR);
                    } catch (SendIntentException e) {
                        mPlusClient.connect();
                    }
                }
            }

            // Enregistrer l'intention afin que nous puissions lancer une activité lorsque
            // l'utilisateur clique sur le bouton de connexion.
            mConnectionResult = result;
        }
    };
}
