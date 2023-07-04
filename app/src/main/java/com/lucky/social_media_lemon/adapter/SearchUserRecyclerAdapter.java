package com.lucky.social_media_lemon.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.Timestamp;
import com.lucky.social_media_lemon.ChatActivity;
import com.lucky.social_media_lemon.NotificationFragment;
import com.lucky.social_media_lemon.OtherUserProfileActivity;
import com.lucky.social_media_lemon.R;
import com.lucky.social_media_lemon.model.ChatRoomModel;
import com.lucky.social_media_lemon.model.FriendModel;
import com.lucky.social_media_lemon.model.NotificationModel;
import com.lucky.social_media_lemon.model.UserModel;
import com.lucky.social_media_lemon.utils.AndroidUtil;
import com.lucky.social_media_lemon.utils.FirebaseUtil;

import java.util.Arrays;

public class SearchUserRecyclerAdapter extends FirestoreRecyclerAdapter<UserModel, SearchUserRecyclerAdapter.UserModelViewHolder> {

    Context context;

    public SearchUserRecyclerAdapter(@NonNull FirestoreRecyclerOptions<UserModel> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull UserModelViewHolder holder, int position, @NonNull UserModel model) {

        holder.usernameText.setText(model.getUsername());
        holder.phoneText.setText(model.getPhone());
        if (model.getUserId().equals(FirebaseUtil.currentUserId())){
            holder.usernameText.setText(model.getUsername() + " (Me)");
        }

        // set avatar
        FirebaseUtil.getOtherProfilePicStorageRef(model.getUserId()).getDownloadUrl()
                .addOnCompleteListener(t -> {
                    if (t.isSuccessful()){
                        Uri uri = t.getResult();
                        AndroidUtil.setProfilePic(context, uri, holder.profilePic);
                    }
                });

        holder.messageBtn.setOnClickListener(v -> {
            Intent intent = new Intent(context, ChatActivity.class);
            AndroidUtil.passUserModelAsIntent(intent, model);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        });

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, OtherUserProfileActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        });

        holder.addFriendBtn.setOnClickListener(v -> {

//            String notificationId = FirebaseUtil.allNotificationCollectionReference().document().getId();
//            Timestamp notificationTime = Timestamp.now();
//            String notificationSenderId = FirebaseUtil.currentUserId();
//
//            FirebaseUtil.currentUserDetails().get().addOnCompleteListener(task -> {
//                if (task.isSuccessful()){
//
//                    String usernameSender = task.getResult().toObject(UserModel.class).getUsername();
//                    String notificationContent = usernameSender + " sent a friend request.";
//                    NotificationModel notificationModel = new NotificationModel(notificationId, notificationTime,
//                            notificationSenderId, notificationContent, true);
//
//                    FirebaseUtil.getNotificationReference(notificationId).set(notificationModel);
//
//                }
//            });

            String requestId = FirebaseUtil.getChatroomId(FirebaseUtil.currentUserId(), model.getUserId());

            FirebaseUtil.getChatroomReference(requestId).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    FriendModel friendModel = task.getResult().toObject(FriendModel.class);
                    if (friendModel==null){
                        // first time chat
//                        friendModel = new ChatRoomModel(
//                                requestId,
//                                Arrays.asList(FirebaseUtil.currentUserId(), model.getUserId()),
//                                Timestamp.now(),
//                                ""
//                        );
                        FirebaseUtil.getChatroomReference(requestId).set(friendModel);
                    }
                }
            });

        });


    }

    @NonNull
    @Override
    public UserModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.search_user_recycler_row, parent, false);
        return new UserModelViewHolder(view);
    }

    class UserModelViewHolder extends RecyclerView.ViewHolder{

        TextView usernameText;
        TextView phoneText;
        ImageView profilePic;
        Button addFriendBtn;
        Button messageBtn;

        public UserModelViewHolder(@NonNull View itemView) {
            super(itemView);
            usernameText = itemView.findViewById(R.id.user_name_text);
            phoneText = itemView.findViewById(R.id.phone_text);
            profilePic = itemView.findViewById(R.id.profile_pic_image_view);
            addFriendBtn = itemView.findViewById(R.id.add_friend_btn);
            messageBtn = itemView.findViewById(R.id.message_btn);
        }
    }

}
