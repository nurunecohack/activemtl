package com.nurun.activemtl.controller;

import java.util.List;

import com.nurun.activemtl.http.AddPlayerToCourtRequestCallbacks;
import com.nurun.activemtl.http.DeletePlayerToCourtRequestCallbacks;
import com.nurun.activemtl.model.Player;

/**
 * Created by fatou-kine.camara on 13-05-16.
 */
public interface PlayerController {

    public boolean playerAlreadyExists(String email);

    public void signUp(String email);

    public void signUp(String email, String password);

    public Player login(String email);

    public Player login(String email, String password);

    public Player getCurrentPlayer();

    public Player getPlayerProfile(String email);

    public void editNickname(String email, String nickname);

    public void updateProfilePicture(String email, String contentUri);

    public void checkInCourt(String courtId, AddPlayerToCourtRequestCallbacks addPlayerCallback);

    public void leaveCourt(String courtId, DeletePlayerToCourtRequestCallbacks deletePlayerCallback);

    public List<Player> getPlayersOnCourt(String courtId);

    public void deletePlayer(String email);
}
