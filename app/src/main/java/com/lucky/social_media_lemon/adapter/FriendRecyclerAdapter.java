package com.lucky.social_media_lemon.adapter;

import android.content.Context;
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
import com.lucky.social_media_lemon.R;
import com.lucky.social_media_lemon.model.FriendModel;
import com.lucky.social_media_lemon.model.UserModel;
import com.lucky.social_media_lemon.utils.AndroidUtil;
import com.lucky.social_media_lemon.utils.FirebaseUtil;

public class FriendRecyclerAdapter extends FirestoreRecyclerAdapter<FriendModel, FriendRecyclerAdapter.FriendModelViewHolder> {

    Context context;

    public FriendRecyclerAdapter(@NonNull FirestoreRecyclerOptions<FriendModel> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull FriendModelViewHolder holder, int position, @NonNull FriendModel model) {

//        FirebaseUtil.getOtherProfilePicStorageRef(model.getRequestSenderId()).getDownloadUrl()
//                .addOnCompleteListener(t -> {
//                    if (t.isSuccessful()){
//                        Uri uri = t.getResult();
//                        AndroidUtil.setProfilePic(context, uri, holder.profilePic);
//                    }
//                });
//
//        FirebaseUtil.getUserDetailsById(model.getRequestSenderId()).get().addOnCompleteListener(task -> {
//            if (task.isSuccessful()){
//                UserModel commentOwner = task.getResult().toObject(UserModel.class);
//                holder.userName.setText(commentOwner.getUsername());
//
//
//
//            }
//        });
//
//        holder.confirmButton.setOnClickListener(v -> {
////            FirebaseUtil.currentUserDetails().update("fff").addOnCompleteListener(task -> {
////
////            });
//            holder.confirmButton.setVisibility(View.INVISIBLE);
//            holder.deleteRequestButton.setVisibility(View.INVISIBLE);
//        });

    }

    @NonNull
    @Override
    public FriendModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.friend_recycler_row, parent, false);
        return new FriendRecyclerAdapter.FriendModelViewHolder(view);
    }

    class FriendModelViewHolder extends RecyclerView.ViewHolder{

        ImageView profilePic;
        TextView userName;
        Button confirmButton;
        Button deleteRequestButton;

        public FriendModelViewHolder(@NonNull View itemView) {
            super(itemView);

            profilePic = itemView.findViewById(R.id.profile_pic_image_view);
            userName = itemView.findViewById(R.id.username_text_view);
            confirmButton = itemView.findViewById(R.id.confirm_btn);
            deleteRequestButton = itemView.findViewById(R.id.delete_request_btn);
        }
    }
}
