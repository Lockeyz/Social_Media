package com.lucky.social_media_lemon.model;

import com.google.firebase.Timestamp;

import java.util.List;

public class FriendModel {
    private String requestId;
    private String requestSenderId;
    private String requestReceiverId;
    List<String> userIds;
    private Timestamp requestTime;
    private boolean isFriend;

    public FriendModel() {
    }

    public FriendModel(String requestId, String requestSenderId, String requestReceiverId,
                       List<String> userIds, Timestamp requestTime, boolean isFriend) {
        this.requestId = requestId;
        this.requestSenderId = requestSenderId;
        this.requestReceiverId = requestReceiverId;
        this.userIds = userIds;
        this.requestTime = requestTime;
        this.isFriend = isFriend;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getRequestSenderId() {
        return requestSenderId;
    }

    public void setRequestSenderId(String requestSenderId) {
        this.requestSenderId = requestSenderId;
    }

    public String getRequestReceiverId() {
        return requestReceiverId;
    }

    public void setRequestReceiverId(String requestReceiverId) {
        this.requestReceiverId = requestReceiverId;
    }

    public List<String> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<String> userIds) {
        this.userIds = userIds;
    }

    public Timestamp getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(Timestamp requestTime) {
        this.requestTime = requestTime;
    }

    public boolean isFriend() {
        return isFriend;
    }

    public void setFriend(boolean friend) {
        isFriend = friend;
    }
}
