package com.lucky.social_media_lemon.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.lucky.social_media_lemon.ChatActivity;
import com.lucky.social_media_lemon.OtherUserProfileActivity;
import com.lucky.social_media_lemon.R;
import com.lucky.social_media_lemon.model.RequestModel;
import com.lucky.social_media_lemon.model.UserModel;
import com.lucky.social_media_lemon.utils.AndroidUtil;
import com.lucky.social_media_lemon.utils.FirebaseUtil;

public class SearchUserRecyclerAdapter extends FirestoreRecyclerAdapter<UserModel, SearchUserRecyclerAdapter.UserModelViewHolder> {

    Context context;

    public SearchUserRecyclerAdapter(@NonNull FirestoreRecyclerOptions<UserModel> options, Context context) {
        super(options);
        this.context = context;
    }

    @SuppressLint("ResourceAsColor")
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


        FirebaseUtil.getFriendReference(model.getUserId()).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){

                DocumentSnapshot document = task.getResult();

                FirebaseUtil.getRequestReference(model.getUserId()).get().addOnCompleteListener(task1 -> {
                    if (task1.isSuccessful()){

                        DocumentSnapshot document1 = task1.getResult();

                        if (!document.exists() && !document1.exists()){

                            holder.addFriendBtn.setEnabled(true);
                            holder.cancelBtn.setEnabled(false);

                        }

                        if (!document.exists() && document1.exists()
                                && document1.toObject(RequestModel.class).getIsRequestUser() == true) {
                            holder.addFriendBtn.setEnabled(false);
                            holder.addFriendBtn.setText("Add friend");
                            holder.cancelBtn.setEnabled(true);
                            holder.cancelBtn.setText("Cancel");

                        }
                        if (!document.exists() && document1.exists()
                                && document1.toObject(RequestModel.class).getIsRequestUser() == false) {

                            holder.addFriendBtn.setEnabled(true);
                            holder.addFriendBtn.setText("Confirm");
                            holder.cancelBtn.setEnabled(true);
                            holder.cancelBtn.setText("Delete request");
                        }
                        if (document.exists()){

                            holder.addFriendBtn.setEnabled(false);
                            holder.addFriendBtn.setText("Friend");
                            holder.cancelBtn.setEnabled(true);
                            holder.cancelBtn.setText("Unfriend");

                        }

                    }
                });
            }
        });

        holder.addFriendBtn.setOnClickListener(v -> {
            FirebaseUtil.getFriendReference(model.getUserId()).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();

                    FirebaseUtil.getRequestReference(model.getUserId()).get().addOnCompleteListener(task1 -> {
                        DocumentSnapshot document1 = task1.getResult();

                        if (!document.exists() && !document1.exists()){

                            RequestModel requestModel = new RequestModel(model.getUserId(), Timestamp.now(), true);
                            RequestModel requestedModel = new RequestModel(FirebaseUtil.currentUserId(), Timestamp.now(), false);

                            FirebaseUtil.getRequestReference(model.getUserId()).set(requestModel);
                            FirebaseUtil.getOtherUserRequestReference(model.getUserId(), FirebaseUtil.currentUserId()).set(requestedModel);

                            holder.cancelBtn.setEnabled(true);
                            holder.addFriendBtn.setEnabled(false);


                        }
                        else if (!document.exists() && document1.exists()) {

                            FirebaseUtil.currentUserDetails().get().addOnCompleteListener(task2 -> {
                                if (task2.isSuccessful()){

                                    UserModel requestedUser = task2.getResult().toObject(UserModel.class);
                                    FirebaseUtil.getFriendReference(model.getUserId()).set(model);
                                    FirebaseUtil.getOtherUserFriendReference(model.getUserId(), FirebaseUtil.currentUserId()).set(requestedUser);

                                    FirebaseUtil.getRequestReference(model.getUserId()).delete();
                                    FirebaseUtil.getOtherUserRequestReference(model.getUserId(), FirebaseUtil.currentUserId()).delete();

                                    holder.addFriendBtn.setEnabled(false);
                                    holder.addFriendBtn.setText("Friend");
                                    holder.cancelBtn.setEnabled(true);
                                    holder.cancelBtn.setText("Unfriend");
                                }
                            });


                        }
                    });
                }
            });
        });

        holder.cancelBtn.setOnClickListener(v -> {
            FirebaseUtil.getFriendReference(model.getUserId()).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();

                    FirebaseUtil.getRequestReference(model.getUserId()).get().addOnCompleteListener(task1 -> {
                        DocumentSnapshot document1 = task1.getResult();

                        if (!document.exists() && !document1.exists()){

                        }
                        else if (!document.exists() && document1.exists()) {

                            FirebaseUtil.getRequestReference(model.getUserId()).delete();
                            FirebaseUtil.getOtherUserRequestReference(model.getUserId(), FirebaseUtil.currentUserId()).delete();

                            holder.addFriendBtn.setEnabled(true);
                            holder.addFriendBtn.setText("Add friend");
                            holder.cancelBtn.setEnabled(false);
                            holder.cancelBtn.setText("Cancel");

                        }


                        else if (document.exists()) {

                            FirebaseUtil.getFriendReference(model.getUserId()).delete();
                            FirebaseUtil.getOtherUserFriendReference(model.getUserId(), FirebaseUtil.currentUserId()).delete();
                            FirebaseUtil.getRequestReference(model.getUserId()).delete();
                            FirebaseUtil.getOtherUserRequestReference(model.getUserId(), FirebaseUtil.currentUserId()).delete();

                            holder.addFriendBtn.setEnabled(true);
                            holder.addFriendBtn.setText("Add friend");
                            holder.cancelBtn.setEnabled(false);
                            holder.cancelBtn.setText("Cancel");
                        }
                    });
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
        Button cancelBtn;
        Button messageBtn;

        public UserModelViewHolder(@NonNull View itemView) {
            super(itemView);
            usernameText = itemView.findViewById(R.id.user_name_text);
            phoneText = itemView.findViewById(R.id.phone_text);
            profilePic = itemView.findViewById(R.id.profile_pic_image_view);
            addFriendBtn = itemView.findViewById(R.id.add_friend_btn);
            cancelBtn = itemView.findViewById(R.id.cancel_btn);
            messageBtn = itemView.findViewById(R.id.message_btn);
        }
    }

}
