package com.nurun.activemtl.controller.parse;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;

import com.nurun.activemtl.PreferenceHelper;
import com.nurun.activemtl.controller.PlayerController;
import com.nurun.activemtl.http.AddPlayerToCourtRequestCallbacks;
import com.nurun.activemtl.http.DeletePlayerToCourtRequestCallbacks;
import com.nurun.activemtl.model.Player;
import com.nurun.activemtl.model.parse.ParseEvent;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

/**
 * Created by fatou-kine.camara on 13-05-16.
 */
public class ParsePlayerController implements PlayerController {

    private static final String DEFAULT_PASSWORD = "findbballcourtpwd";

    private static final String USERNAME = "username";
    private static final String NICKNAME = "nickname";
    private static final String PROFILE_PIC = "profile_pic";
    private static final String CURRENT_COURT_ID = "currentCourtId";

    private Context context;

    public ParsePlayerController(Context context) {
        this.context = context;
    }

    private Player parseUserToPlayer(final ParseUser parseUser) {
        Player player = new Player(parseUser.getUsername(), parseUser.getString(NICKNAME));
        player.setProfilePicture(parseUser.getString(PROFILE_PIC));
        return player;
    }

    @Override
    public boolean playerAlreadyExists(String email) {
        return getPlayerProfile(email) != null;
    }

    @Override
    public void signUp(String email) {
        signUp(email, DEFAULT_PASSWORD);
    }

    @Override
    public void signUp(String email, String password) {
        ParseUser user = new ParseUser();
        user.setUsername(email);
        user.setPassword(password);
        user.setEmail(email);

        try {
            user.signUp();
        } catch (ParseException e) {
            Log.e(getClass().getSimpleName(), e.getMessage(), e);
        }
    }

    @Override
    public Player login(String email) {
        return login(email, DEFAULT_PASSWORD);
    }

    @Override
    public Player login(String email, String password) {
        try {
            return parseUserToPlayer(ParseUser.logIn(email, password));
        } catch (ParseException e) {
            Log.e(getClass().getSimpleName(), e.getMessage(), e);
        }
        return null;
    }

    @Override
    public Player getCurrentPlayer() {
        ParseUser parseUser = ParseUser.getCurrentUser();
        if (parseUser != null && parseUser.isAuthenticated()) {
            return parseUserToPlayer(parseUser);
        }
        return null;
    }

    @Override
    public Player getPlayerProfile(String email) {
        try {
            ParseQuery<ParseUser> query = ParseUser.getQuery();
            query.whereEqualTo(USERNAME, email);
            return parseUserToPlayer((ParseUser) query.getFirst());
        } catch (ParseException e) {
            Log.e(getClass().getSimpleName(), e.getMessage(), e);
        }
        return null;
    }

    @Override
    public void editNickname(String email, String nickname) {
        try {
            ParseUser user = ParseUser.logIn(email, DEFAULT_PASSWORD);
            user.put(NICKNAME, nickname);
            user.save();
        } catch (ParseException e) {
            Log.e(getClass().getSimpleName(), e.getMessage(), e);
        }
    }

    @Override
    public void updateProfilePicture(String email, String contentUri) {
        try {
            ParseUser user = ParseUser.logIn(email, DEFAULT_PASSWORD);

            // TODO: Get stream from contentURI and put for PROFILE_PIC

            user.save();
        } catch (ParseException e) {
            Log.e(getClass().getSimpleName(), e.getMessage(), e);
        }
    }

    @Override
    public void checkInCourt(String courtId, final AddPlayerToCourtRequestCallbacks addPlayerToCourtRequestCallbacks) {
        try {
            final ParseUser parseUser = ParseUser.logIn(PreferenceHelper.getLogin(context), DEFAULT_PASSWORD);
            ParseQuery<ParseEvent> query = ParseQuery.getQuery(ParseEvent.class);
            query.getInBackground(courtId, new GetCallback<ParseEvent>() {

                @Override
                public void done(final ParseEvent court, ParseException e) {
                    if (e == null) {
                        ParseRelation<ParseUser> relation = court.getRelation("currentPlayers");
                        relation.add(parseUser);
                        court.saveInBackground(new SaveCallback() {

                            @Override
                            public void done(ParseException e) {
                                if (e == null) {
                                    addPlayerToCourtRequestCallbacks.onAddPlayerSuccess(court);
                                } else {
                                    addPlayerToCourtRequestCallbacks.onAddPlayerFail(new RuntimeException(e));
                                }
                            }
                        });
                    } else {
                        addPlayerToCourtRequestCallbacks.onAddPlayerFail(new RuntimeException(e));
                    }
                }
            });
        } catch (ParseException e) {
            addPlayerToCourtRequestCallbacks.onAddPlayerFail(new RuntimeException(e));
        }
    }

    @Override
    public void leaveCourt(String courtId, final DeletePlayerToCourtRequestCallbacks deletePlayerCallback) {
        try {
            final ParseUser parseUser = ParseUser.logIn(PreferenceHelper.getLogin(context), DEFAULT_PASSWORD);
            ParseQuery<ParseEvent> query = ParseQuery.getQuery(ParseEvent.class);
            query.getInBackground(courtId, new GetCallback<ParseEvent>() {

                @Override
                public void done(final ParseEvent court, ParseException e) {
                    if (e == null) {
                        ParseRelation<ParseUser> relation = court.getRelation("currentPlayers");
                        relation.remove(parseUser);
                        court.saveInBackground(new SaveCallback() {

                            @Override
                            public void done(ParseException e) {
                                if (e == null) {
                                    deletePlayerCallback.onDeletePlayerSuccess(court);
                                } else {
                                    deletePlayerCallback.onDeletePlayerFail(new RuntimeException(e));
                                }
                            }
                        });
                    } else {
                        deletePlayerCallback.onDeletePlayerFail(new RuntimeException(e));
                    }
                }
            });
        } catch (ParseException e) {
            deletePlayerCallback.onDeletePlayerFail(new RuntimeException(e));
        }
    }

    @Override
    public List<Player> getPlayersOnCourt(String courtId) {
        final List<Player> playersOnCourt = new ArrayList<Player>();
        try {
            ParseQuery<ParseUser> query = ParseUser.getQuery();
            query.whereEqualTo(CURRENT_COURT_ID, courtId);
            for (ParseObject object : query.find()) {
                ParseUser user = (ParseUser) object;
                playersOnCourt.add(parseUserToPlayer(user));
            }
            return playersOnCourt;
        } catch (ParseException e) {
            Log.e(getClass().getSimpleName(), e.getMessage(), e);
        }
        return null;
    }

    @Override
    public void deletePlayer(String email) {
        try {
            ParseUser user = ParseUser.logIn(email, DEFAULT_PASSWORD);
            user.delete();
        } catch (ParseException e) {
            Log.e(getClass().getSimpleName(), e.getMessage(), e);
        }
    }
}
