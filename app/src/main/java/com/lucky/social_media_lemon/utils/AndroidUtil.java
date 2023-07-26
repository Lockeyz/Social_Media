package com.lucky.social_media_lemon.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.lucky.social_media_lemon.model.NotificationModel;
import com.lucky.social_media_lemon.model.PostModel;
import com.lucky.social_media_lemon.model.UserModel;

import java.util.Collections;


public class AndroidUtil {

    public static void showToast(Context context, String message){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void passUserModelAsIntent(Intent intent, UserModel model){
        intent.putExtra("username", model.getUsername());
        intent.putExtra("phone", model.getPhone());
        intent.putExtra("userId", model.getUserId());
    }

    public static UserModel getUserModelFromIntent(Intent intent){
        UserModel userModel = new UserModel();
        userModel.setUsername(intent.getStringExtra("username"));
        userModel.setPhone(intent.getStringExtra("phone"));
        userModel.setUserId(intent.getStringExtra("userId"));
        return userModel;
    }

    public static void passPostModelAsIntent(Intent intent, PostModel model){
        intent.putExtra("postId", model.getPostId());
        intent.putExtra("postUserId", model.getPostUserId());
        intent.putExtra("caption", model.getCaption());
        intent.putExtra("pictureUrl", model.getPictureUrl());
    }

    public static PostModel getPostModelFromIntent(Intent intent){
        PostModel postModel = new PostModel();
        postModel.setPostId(intent.getStringExtra("postId"));
        postModel.setPostUserId(intent.getStringExtra("postUserId"));
        postModel.setCaption(intent.getStringExtra("caption"));
        postModel.setPictureUrl(intent.getStringExtra("pictureUrl"));
        return postModel;
    }

    public static void passNotificationModelAsIntent(Intent intent, NotificationModel model){
        intent.putExtra("notificationId", model.getNotificationId());
        intent.putExtra("notificationUserId", model.getNotificationUserId());
    }

    public static NotificationModel getNotificationModelFromIntent(Intent intent){
        NotificationModel notificationModel = new NotificationModel();
        notificationModel.setNotificationId(intent.getStringExtra("notificationId"));
        notificationModel.setNotificationUserId(intent.getStringExtra("notificationUserId"));
        return notificationModel;
    }


    public static void setProfilePic(Context context, Uri imageUrl, ImageView imageView){
        Glide.with(context).load(imageUrl).apply(RequestOptions.circleCropTransform()).into(imageView);
    }

    public static void setCoverPic(Context context, Uri imageUrl, ImageView imageView){
        Glide.with(context).load(imageUrl).into(imageView);
    }
}
