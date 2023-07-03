package com.lucky.social_media_lemon.model;

import com.google.firebase.Timestamp;

public class NotificationModel {
    private String notificationId;
    private Timestamp notificationTime;
    private String notificationSenderId;
    private String notificationContent;

    public NotificationModel() {
    }

    public NotificationModel(String notificationId, Timestamp notificationTime,
                             String notificationSenderId, String notificationContent) {
        this.notificationId = notificationId;
        this.notificationTime = notificationTime;
        this.notificationSenderId = notificationSenderId;
        this.notificationContent = notificationContent;
    }

    public String getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(String notificationId) {
        this.notificationId = notificationId;
    }

    public Timestamp getNotificationTime() {
        return notificationTime;
    }

    public void setNotificationTime(Timestamp notificationTime) {
        this.notificationTime = notificationTime;
    }

    public String getNotificationSenderId() {
        return notificationSenderId;
    }

    public void setNotificationSenderId(String notificationSenderId) {
        this.notificationSenderId = notificationSenderId;
    }

    public String getNotificationContent() {
        return notificationContent;
    }

    public void setNotificationContent(String notificationContent) {
        this.notificationContent = notificationContent;
    }
}
