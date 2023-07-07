package com.lucky.social_media_lemon.model;

import com.google.firebase.Timestamp;

public class RequestModel {
    private String requestId;
    private String receiverId;
    private Timestamp requestTime;
    private boolean isFriend;

    public RequestModel() {
    }

    public RequestModel(String requestId, String receiverId,
                        Timestamp requestTime, boolean isFriend) {
        this.requestId = requestId;
        this.receiverId = receiverId;
        this.requestTime = requestTime;
        this.isFriend = isFriend;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String requestReceiverId) {
        this.receiverId = receiverId;
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
