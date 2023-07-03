package com.lucky.social_media_lemon.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import com.google.firebase.firestore.AggregateQuerySnapshot;
import com.google.firebase.firestore.AggregateSource;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.lucky.social_media_lemon.CommentActivity;
import com.lucky.social_media_lemon.MainActivity;
import com.lucky.social_media_lemon.R;
import com.lucky.social_media_lemon.SearchUserActivity;
import com.lucky.social_media_lemon.constants.Constants;
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
        FirebaseUtil.getUserDetailsById(model.getPostUserId()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    UserModel postOwner = task.getResult().toObject(UserModel.class);
                    holder.postOwnerUsernameText.setText(postOwner.getUsername());
                    if(postOwner.getAvatarUrl() != null){
                        AndroidUtil.setProfilePic(context, postOwner.getAvatarUrl(), holder.postAvatarImage);
                    }

                }
            }
        });

        FirebaseUtil.postCommentsCollectionReference(model.getPostId()).count().get(AggregateSource.SERVER).addOnCompleteListener(new OnCompleteListener<AggregateQuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<AggregateQuerySnapshot> task) {
                AggregateQuerySnapshot snapshot = task.getResult();
                holder.commentCounterText.setText(String.valueOf(snapshot.getCount()));
            }
        });

        holder.captionText.setText(model.getCaption());
        Glide.with(context).load(model.getPictureUrl()).into(holder.postPicImage);
        holder.postedTimeText.setText(FirebaseUtil.timestampToFullDateAndHourString(model.getPostTime()));
        holder.likeCounterText.setText(String.valueOf(model.getLikedUserIds().size()));

        if(model.getLikedUserIds().contains(FirebaseUtil.currentUserId())){
            holder.likeIconBtn.setImageResource(R.drawable.liked_icon);
            holder.likeText.setTextColor(context.getResources().getColor(R.color.my_primary));
        }
        else{
            holder.likeIconBtn.setImageResource(R.drawable.like_icon);
            holder.likeText.setTextColor(context.getResources().getColor(R.color.grey_text));
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

        holder.commentLinear.setOnClickListener(v -> {
            Intent commentIntent = new Intent(context, CommentActivity.class);
            commentIntent.putExtra(Constants.EXTRA_POST_ID_NEWS_FEED_RECYCLER_ADAPTER_TO_COMMENT_ACTIVITY, model.getPostId());
            context.startActivity(commentIntent);
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
        LinearLayout commentLinear;
        ImageButton shareIconBtn;
        ImageView postAvatarImage;
        TextView postOwnerUsernameText;
        TextView postedTimeText;
        TextView likeText;

        public PostModelViewHolder(@NonNull View itemView) {
            super(itemView);
            captionText = itemView.findViewById(R.id.caption_text_view);
            postPicImage = itemView.findViewById(R.id.pic_image_view);
            likeCounterText = itemView.findViewById(R.id.like_counter_text_view);
            commentCounterText = itemView.findViewById(R.id.comment_counter_text_view);
            likeIconBtn = itemView.findViewById(R.id.like_icon_btn);
            likeLinear = itemView.findViewById(R.id.like_linear);
            likeText = itemView.findViewById(R.id.tv_news_feed_like);
            commentLinear = itemView.findViewById(R.id.ll_news_feed_comment);
            shareIconBtn = itemView.findViewById(R.id.share_icon_btn);
            postAvatarImage = itemView.findViewById(R.id.iv_post_avatar);
            postOwnerUsernameText = itemView.findViewById(R.id.tv_post_owner_username);
            postedTimeText = itemView.findViewById(R.id.tv_posted_time);
        }
    }
}
