package com.lucky.social_media_lemon.model;

import com.google.firebase.Timestamp;

public class RequestModel {
    private String requestId;
//    private String receiverId;
    private Timestamp requestTime;
    private boolean isRequestUser;

    public RequestModel() {
    }

    public RequestModel(String requestId, Timestamp requestTime, boolean isRequestUser) {
        this.requestId = requestId;
        this.requestTime = requestTime;
        this.isRequestUser = isRequestUser;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }


    public Timestamp getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(Timestamp requestTime) {
        this.requestTime = requestTime;
    }

    public boolean getIsRequestUser() {
        return isRequestUser;
    }

    public void setIsRequestUser(boolean isRequestUser) {
        this.isRequestUser = isRequestUser;
    }
}
