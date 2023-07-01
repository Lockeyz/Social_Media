package com.lucky.social_media_lemon.model;

import com.google.firebase.Timestamp;

public class CommentModel {
    private String id;
    private String comment;
    private Timestamp commentTime;
    private String commentUserId;

    public CommentModel(){}

    public CommentModel(String id, String comment, Timestamp commentTime, String commentUserId){
        this.id = id;
        this.comment = comment;
        this.commentUserId = commentUserId;
        this.commentTime = commentTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Timestamp getCommentTime() {
        return commentTime;
    }

    public void setCommentTime(Timestamp commentTime) {
        this.commentTime = commentTime;
    }

    public String getCommentUserId() {
        return commentUserId;
    }

    public void setCommentUserId(String commentUserId) {
        this.commentUserId = commentUserId;
    }
}
