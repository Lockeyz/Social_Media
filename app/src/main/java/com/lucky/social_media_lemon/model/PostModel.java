package com.lucky.social_media_lemon.model;

import com.google.firebase.Timestamp;

import java.util.List;

public class PostModel {

    private String postId;
    private String postUserId;
    private Timestamp postTime;
    private String caption;
    private String pictureUrl;
    List<String> likedUserIds;
    private int commentCounter;

    public PostModel() {
    }

    public PostModel(String postId, String postUserId, Timestamp postTime, String caption,
                     String pictureUrl, List<String> likedUserIds ,int commentCounter) {
        this.postId = postId;
        this.postUserId = postUserId;
        this.postTime = postTime;
        this.caption = caption;
        this.pictureUrl = pictureUrl;
        this.likedUserIds = likedUserIds;
        this.commentCounter = commentCounter;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getPostUserId() {
        return postUserId;
    }

    public void setPostUserId(String postUserId) {
        this.postUserId = postUserId;
    }

    public Timestamp getPostTime() {
        return postTime;
    }

    public void setPostTime(Timestamp postTime) {
        this.postTime = postTime;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public List<String> getLikedUserIds() {
        return likedUserIds;
    }

    public void setLikedUserIds(List<String> likedUserIds) {
        this.likedUserIds = likedUserIds;
    }

    public int getCommentCounter() {
        return commentCounter;
    }

    public void setCommentCounter(int commentCounter) {
        this.commentCounter = commentCounter;
    }
}
