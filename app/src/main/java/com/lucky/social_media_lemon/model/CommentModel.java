package com.lucky.social_media_lemon.model;

import com.google.firebase.Timestamp;

public class CommentModel {
    private String commentId;
    private String comment;
    private Timestamp commentTime;
    private String commentUserId;

    public CommentModel(){}

    public CommentModel(String commentId, String comment, Timestamp commentTime, String commentUserId){
        this.commentId = commentId;
        this.comment = comment;
        this.commentUserId = commentUserId;
        this.commentTime = commentTime;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String id) {
        this.commentId = commentId;
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
