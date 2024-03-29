package com.lucky.social_media_lemon.utils;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.List;

public class FirebaseUtil {

    public static String currentUserId(){
        return FirebaseAuth.getInstance().getUid();
    }

    public static boolean isLoggedIn(){
        if (currentUserId()!=null){
            return true;
        }
        return false;
    }

    public static DocumentReference currentUserDetails(){
        return FirebaseFirestore.getInstance().collection("users").document(currentUserId());
    }

    public static DocumentReference getUserDetailsById(String userId){
        return FirebaseFirestore.getInstance().collection("users").document(userId);
    }

    public static CollectionReference allUserCollectionReference(){
        return FirebaseFirestore.getInstance().collection("users");
    }

    public static DocumentReference getChatroomReference(String chatRoomId){
        return FirebaseFirestore.getInstance().collection("chatrooms").document(chatRoomId);
    }

    // Tạo đường dẫn đến Document cụ thể trong Collection posts
    public static DocumentReference getPostReference(String postId){
        return FirebaseFirestore.getInstance().collection("posts").document(postId);
    }
    // Tham chiếu Collection posts chứa tất cả bài đăng
    public static CollectionReference allPostCollectionReference(){
        return FirebaseFirestore.getInstance().collection("posts");
    }



    // Tham chiếu Collection chats chứa các đoạn chat trong phòng chat
    public static CollectionReference getChatroomMessageReference(String chatroomId){
        return getChatroomReference(chatroomId).collection("chats");
    }

    public static String getChatroomId(String userId1, String userId2){
        if (userId1.hashCode()<userId2.hashCode()){
            return userId1+"_"+userId2;
        } else {
            return userId2+"_"+userId1;
        }
    }

    public static CollectionReference allChatroomCollectionReference(){
        return FirebaseFirestore.getInstance().collection("chatrooms");
    }

    public static DocumentReference getOtherUserFromChatroom(List<String> userIds){
        if (userIds.get(0).equals(FirebaseUtil.currentUserId())){
            return allUserCollectionReference().document(userIds.get(1));
        } else {
            return allUserCollectionReference().document(userIds.get(0));
        }
    }

    public static String timestampToHourMinuteString(Timestamp timestamp){
        return new SimpleDateFormat("HH:mm").format(timestamp.toDate());
    }

    public static String timestampToFullDateAndHourString(Timestamp timestamp){
        return new SimpleDateFormat("HH:mm, dd/MM/yyyy").format(timestamp.toDate());
    }

    public static void logout(){
        FirebaseAuth.getInstance().signOut();
    }

    // get Reference avatar and cover
    public static StorageReference getCurrentProfilePicStorageRef(){
        return FirebaseStorage.getInstance().getReference().child("profile_pic")
                .child(FirebaseUtil.currentUserId());
    }
    public static StorageReference getOtherProfilePicStorageRef(String otherUserId){
        return FirebaseStorage.getInstance().getReference().child("profile_pic")
                .child(otherUserId);
    }

    public static StorageReference getOtherProfileCoverStorageRef(String otherUserId){
        return FirebaseStorage.getInstance().getReference().child("profile_cover")
                .child(otherUserId);
    }


    public static CollectionReference postCommentsCollectionReference(String postId){
        return FirebaseFirestore.getInstance().collection("posts").document(postId).collection("comments");
    }

    // Tham chiếu Collection notification
    public static CollectionReference allNotificationCollectionReference(){
        return FirebaseFirestore.getInstance().collection("notifications");
    }

    public static DocumentReference getNotificationReference(String notificationId){
        return FirebaseFirestore.getInstance().collection("notifications").document(notificationId);
    }


    // Reference Collection friends && Document friends

    public static CollectionReference allFriendCollectionReference(){
        return FirebaseUtil.currentUserDetails().collection("friends");
    }

    public static CollectionReference allOtherUserFriendCollectionReference(String userId){
        return FirebaseUtil.getUserDetailsById(userId).collection("friends");
    }

    public static DocumentReference getFriendReference(String userId){
        return allFriendCollectionReference().document(userId);
    }

    public static DocumentReference getOtherUserFriendReference(String otherUserId, String currentUserId){
        return  allOtherUserFriendCollectionReference(otherUserId).document(currentUserId);
    }

    // requests
    public static CollectionReference allRequestCollectionReference(){
        return FirebaseUtil.currentUserDetails().collection("requests");
    }

    public static CollectionReference allOtherUserRequestCollectionReference(String otherUserId){
        return FirebaseUtil.getUserDetailsById(otherUserId).collection("requests");
    }

    public static DocumentReference getRequestReference(String receiverId){
        return allRequestCollectionReference().document(receiverId);
    }

    public static DocumentReference getOtherUserRequestReference(String otherUserId, String currentUserId){
        return  allOtherUserRequestCollectionReference(otherUserId).document(currentUserId);
    }
}
