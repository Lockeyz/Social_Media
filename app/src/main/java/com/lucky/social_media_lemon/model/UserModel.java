package com.lucky.social_media_lemon.model;

import com.google.firebase.Timestamp;

import java.util.List;

public class UserModel {

    private String phone;
    private String username;
    private Timestamp createdTimestamp;
    private String userId;
    private List<String> friendIds;

    public UserModel() {
    }

    public UserModel(String phone, String username, Timestamp createdTimestamp, String userId, List<String> friendIds) {
        this.phone = phone;
        this.username = username;
        this.createdTimestamp = createdTimestamp;
        this.userId = userId;
        this.friendIds = friendIds;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Timestamp getCreatedTimestamp() {
        return createdTimestamp;
    }

    public void setCreatedTimestamp(Timestamp createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<String> getFriendIds() {
        return friendIds;
    }

    public void setFriendIds(List<String> friendIds) {
        this.friendIds = friendIds;
    }


}
