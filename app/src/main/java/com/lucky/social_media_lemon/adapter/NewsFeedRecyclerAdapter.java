package com.lucky.social_media_lemon.adapter;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.lucky.social_media_lemon.R;
import com.lucky.social_media_lemon.model.PostModel;
import com.lucky.social_media_lemon.model.UserModel;
import com.lucky.social_media_lemon.utils.AndroidUtil;
import com.lucky.social_media_lemon.utils.FirebaseUtil;

public class NewsFeedRecyclerAdapter extends FirestoreRecyclerAdapter<PostModel, NewsFeedRecyclerAdapter.PostModelViewHolder> {

    Context context;

    public NewsFeedRecyclerAdapter(@NonNull FirestoreRecyclerOptions<PostModel> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull PostModelViewHolder holder, int position, @NonNull PostModel model) {

        FirebaseUtil.getUserById(model.getPostUserId()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    UserModel postOwner = task.getResult().toObject(UserModel.class);
                    holder.postOwnerUsernameText.setText(postOwner.getUsername());
                    holder.postedTimeText.setText(FirebaseUtil.timestampToString(postOwner.getCreatedTimestamp()));
                    if(postOwner.getAvatarUrl() != null){
                        AndroidUtil.setProfilePic(context, postOwner.getAvatarUrl(), holder.postAvatarImage);
                    }

                }
            }
        });

        holder.captionText.setText(model.getCaption());
        Glide.with(context).load(model.getPictureUrl()).into(holder.postPicImage);
        holder.likeCounterText.setText(String.valueOf(model.getLikedUserIds().size()));
        holder.commentCounterText.setText(model.getCommentCounter()+" comments");
        if(model.getLikedUserIds().contains(FirebaseUtil.currentUserId())){
            holder.likeIconBtn.setImageResource(R.drawable.liked_icon);
        }
        else{
            holder.likeIconBtn.setImageResource(R.drawable.like_icon);
        }

        holder.likeLinear.setOnClickListener(v -> {
            holder.likeLinear.setEnabled(false);
            if (!model.getLikedUserIds().contains(FirebaseUtil.currentUserId())) {
                FirebaseUtil.getPostReference(model.getPostId())
                        .update("likedUserIds", FieldValue.arrayUnion(FirebaseUtil.currentUserId()))
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                holder.likeLinear.setEnabled(true);
                            }
                        });
            } else {
                FirebaseUtil.getPostReference(model.getPostId())
                        .update("likedUserIds", FieldValue.arrayRemove(FirebaseUtil.currentUserId()))
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()){
                                holder.likeLinear.setEnabled(true);
                            }
                        });
            }
        });

        holder.commentIconBtn.setOnClickListener(v -> {

        });
    }

    @NonNull
    @Override
    public PostModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.news_feed_recycler_row, parent, false);
        return new PostModelViewHolder(view);
    }



    class PostModelViewHolder extends RecyclerView.ViewHolder {

        TextView captionText;
        ImageView postPicImage;
        TextView likeCounterText;
        TextView commentCounterText;
        ImageButton likeIconBtn;
        LinearLayout likeLinear;
        ImageButton commentIconBtn;
        ImageButton shareIconBtn;
        ImageView postAvatarImage;
        TextView postOwnerUsernameText;
        TextView postedTimeText;

        public PostModelViewHolder(@NonNull View itemView) {
            super(itemView);
            captionText = itemView.findViewById(R.id.caption_text_view);
            postPicImage = itemView.findViewById(R.id.pic_image_view);
            likeCounterText = itemView.findViewById(R.id.like_counter_text_view);
            commentCounterText = itemView.findViewById(R.id.comment_counter_text_view);
            likeIconBtn = itemView.findViewById(R.id.like_icon_btn);
            likeLinear = itemView.findViewById(R.id.like_linear);
            commentIconBtn = itemView.findViewById(R.id.comment_icon_btn);
            shareIconBtn = itemView.findViewById(R.id.share_icon_btn);
            postAvatarImage = itemView.findViewById(R.id.iv_post_avatar);
            postOwnerUsernameText = itemView.findViewById(R.id.tv_post_owner_username);
            postedTimeText = itemView.findViewById(R.id.tv_posted_time);
        }
    }
}
