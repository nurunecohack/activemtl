package com.nurun.activemtl.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

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

public class LoginActivity extends FragmentActivity {
    private Session.StatusCallback statusCallback = new SessionStatusCallback();
    private PlusClient mPlusClient;

    private static final int REQUEST_CODE_RESOLVE_ERR = 9000;
    protected ConnectionResult mConnectionResult;

    private ProgressDialog mConnectionProgressDialog;

    public static Intent newIntent(Context context) {
        return new Intent(context, LoginActivity.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        Session session = Session.getActiveSession();
        mPlusClient = new PlusClient.Builder(this, connectionCallback, connectionFailListener).setVisibleActivities("http://schemas.google.com/AddActivity",
                "http://schemas.google.com/BuyActivity").build();
        // Barre de progression à afficher si l'échec de connexion n'est pas
        // résolu.
        mConnectionProgressDialog = new ProgressDialog(this);
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
    public void onActivityResult(int requestCode, int responseCode, Intent intent) {
        super.onActivityResult(requestCode, responseCode, intent);
        if (requestCode == REQUEST_CODE_RESOLVE_ERR && responseCode == FragmentActivity.RESULT_OK) {
            mConnectionResult = null;
            mPlusClient.connect();
        }
        Session.getActiveSession().onActivityResult(this, requestCode, responseCode, intent);
    }

    private void initFacebook(Bundle savedInstanceState) {
        Settings.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);
        Session session = Session.getActiveSession();
        if (session == null) {
            if (savedInstanceState != null) {
                session = Session.restoreSession(this, null, statusCallback, savedInstanceState);
            }
            if (session == null) {
                session = new Session(this);
            }
            Session.setActiveSession(session);
        }
    }

    private void goToNextScreen() {
        getFragmentManager().popBackStack();
        setResult(200);
        finish();
    }

    @Override
    public void onStop() {
        super.onStop();
        Session.getActiveSession().removeCallback(statusCallback);
    }

    private class SessionStatusCallback implements Session.StatusCallback {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
            mConnectionProgressDialog.show();
            PreferenceHelper.setSocialMediaConnection(LoginActivity.this, SocialMediaConnection.Facebook);
            // make request to the /me API
            Request.newMeRequest(session, new Request.GraphUserCallback() {
                // callback after Graph API response with user object
                @Override
                public void onCompleted(GraphUser user, Response response) {
                    if (user != null) {
                        PreferenceHelper.setUserId(LoginActivity.this, user.getId());
                        PreferenceHelper.setUserName(LoginActivity.this, user.getName());
                        PreferenceHelper.setProfilePictureUrl(LoginActivity.this, ActiveMtlConfiguration.getInstance(LoginActivity.this)
                                .getFacebookProfilePictureUrl(LoginActivity.this));
                        goToNextScreen();
                    }
                    mConnectionProgressDialog.dismiss();
                }
            }).executeAsync();
        }
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

    private ConnectionCallbacks connectionCallback = new ConnectionCallbacks() {

        @Override
        public void onDisconnected() {

        }

        @Override
        public void onConnected(Bundle arg0) {
            mConnectionProgressDialog.dismiss();
            Person currentPerson = mPlusClient.getCurrentPerson();
            PreferenceHelper.setSocialMediaConnection(LoginActivity.this, SocialMediaConnection.Google_plus);
            PreferenceHelper.setUserId(LoginActivity.this, currentPerson.getId());
            PreferenceHelper.setUserName(LoginActivity.this, currentPerson.getDisplayName());
            PreferenceHelper.setProfilePictureUrl(LoginActivity.this,
                    ActiveMtlConfiguration.getInstance(LoginActivity.this).getGooleProfilePictureUrl(LoginActivity.this));
            goToNextScreen();
        }
    };

    private OnConnectionFailedListener connectionFailListener = new OnConnectionFailedListener() {

        @Override
        public void onConnectionFailed(ConnectionResult result) {
            if (mConnectionProgressDialog.isShowing()) {
                // L'utilisateur a déjà appuyé sur le bouton de connexion.
                // Commencer à résoudre
                // les erreurs de connexion. Attendre jusqu'à onConnected() pour
                // masquer la
                // boîte de dialogue de connexion.
                if (result.hasResolution()) {
                    try {
                        result.startResolutionForResult(LoginActivity.this, REQUEST_CODE_RESOLVE_ERR);
                    } catch (SendIntentException e) {
                        mPlusClient.connect();
                    }
                }
            }

            // Enregistrer l'intention afin que nous puissions lancer une
            // activité lorsque
            // l'utilisateur clique sur le bouton de connexion.
            mConnectionResult = result;
        }
    };

    public void onFacebookConnectionClicked() {
        Settings.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);
        Session session = Session.getActiveSession();
        if (!session.isOpened() && !session.isClosed()) {
            session.openForRead(new Session.OpenRequest(this).setCallback(statusCallback));
        } else {
            Session.openActiveSession(this, true, statusCallback);
        }
    }

    public void onGooglePlusConnectionClicked() {
        mConnectionProgressDialog.show();
        if (mConnectionResult == null) {
            mPlusClient.connect();
        } else {
            try {
                mConnectionProgressDialog.show();
                mConnectionResult.startResolutionForResult(this, REQUEST_CODE_RESOLVE_ERR);
            } catch (SendIntentException e) {
                // Nouvelle tentative de connexion
                mConnectionResult = null;
                mPlusClient.connect();
            }
        }
    }

}
