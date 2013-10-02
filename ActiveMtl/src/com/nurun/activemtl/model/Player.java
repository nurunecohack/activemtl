package com.nurun.activemtl.model;

public class Player {
    private final String userId;
    private final String nickname;
    private String profilePicture;

    public Player(final String userId) {
        this(userId, null);
    }

    public Player(final String userId, final String nickname) {
        this.userId = userId;
        this.nickname = nickname;
    }

    public String getUserId() {
        return userId;
    }

    public String getNickName() {
        return nickname;
    }

    public void setProfilePicture(String pathToProfilePic) {
        this.profilePicture = pathToProfilePic;
    }

    public String getPathToProfilePicture() {
        return profilePicture;
    }
}
