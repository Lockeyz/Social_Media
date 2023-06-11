package com.lucky.social_media_lemon.model;

public class PostModel {

    private String postId;
    private String caption;
    private String pictureUrl;
    private int likeCounter;
    private int commentCounter;

    public PostModel() {
    }

    public PostModel(String postId, String caption, String pictureUrl, int likeCounter, int commentCounter) {
        this.postId = postId;
        this.caption = caption;
        this.pictureUrl = pictureUrl;
        this.likeCounter = likeCounter;
        this.commentCounter = commentCounter;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
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

    public int getLikeCounter() {
        return likeCounter;
    }

    public void setLikeCounter(int likeCounter) {
        this.likeCounter = likeCounter;
    }

    public int getCommentCounter() {
        return commentCounter;
    }

    public void setCommentCounter(int commentCounter) {
        this.commentCounter = commentCounter;
    }
}
