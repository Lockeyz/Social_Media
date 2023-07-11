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
import com.lucky.social_media_lemon.ChatActivity;
import com.lucky.social_media_lemon.R;
import com.lucky.social_media_lemon.model.UserModel;
import com.lucky.social_media_lemon.utils.AndroidUtil;
import com.lucky.social_media_lemon.utils.FirebaseUtil;

public class FriendListRecyclerAdapter extends FirestoreRecyclerAdapter<UserModel, FriendListRecyclerAdapter.FriendModelViewHolder> {

    Context context;

    public FriendListRecyclerAdapter(@NonNull FirestoreRecyclerOptions<UserModel> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull FriendModelViewHolder holder, int position, @NonNull UserModel model) {

        FirebaseUtil.getOtherProfilePicStorageRef(model.getUserId()).getDownloadUrl()
                .addOnCompleteListener(t -> {
                    if (t.isSuccessful()){
                        Uri uri = t.getResult();
                        AndroidUtil.setProfilePic(context, uri, holder.profilePic);
                    }
                });

        holder.userName.setText(model.getUsername());

        holder.unfriendBtn.setOnClickListener(v -> {
            // Xoa document cua nguoi khac truoc vi dang su dung model chinh la document se xoa
            FirebaseUtil.getOtherUserFriendReference(model.getUserId(), FirebaseUtil.currentUserId()).delete();
            FirebaseUtil.getFriendReference(model.getUserId()).delete();

        });

        holder.messageBtn.setOnClickListener(v -> {
            Intent intent = new Intent(context, ChatActivity.class);
            AndroidUtil.passUserModelAsIntent(intent, model);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        });

    }

    @NonNull
    @Override
    public FriendModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.friend_list_recycler_row, parent, false);
        return new FriendModelViewHolder(view);
    }

    class FriendModelViewHolder extends RecyclerView.ViewHolder {

        ImageView profilePic;
        TextView userName;
        Button unfriendBtn;
        Button messageBtn;

        public FriendModelViewHolder(@NonNull View itemView) {
            super(itemView);

            profilePic = itemView.findViewById(R.id.profile_pic_image_view);
            userName = itemView.findViewById(R.id.username_text_view);
            unfriendBtn = itemView.findViewById(R.id.unfriend_btn);
            messageBtn = itemView.findViewById(R.id.message_btn);
        }
    }
}
