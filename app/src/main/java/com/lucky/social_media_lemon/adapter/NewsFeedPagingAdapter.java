package com.lucky.social_media_lemon.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.AggregateQuerySnapshot;
import com.google.firebase.firestore.AggregateSource;
import com.google.firebase.firestore.FieldValue;
import com.lucky.social_media_lemon.CommentActivity;
import com.lucky.social_media_lemon.CreatePostActivity;
import com.lucky.social_media_lemon.ProfileActivity;
import com.lucky.social_media_lemon.R;
import com.lucky.social_media_lemon.constants.Constants;
import com.lucky.social_media_lemon.model.PostModel;
import com.lucky.social_media_lemon.model.UserModel;
import com.lucky.social_media_lemon.utils.AndroidUtil;
import com.lucky.social_media_lemon.utils.FirebaseUtil;

import java.util.List;

public class NewsFeedPagingAdapter extends RecyclerView.Adapter<NewsFeedPagingAdapter.PostModelViewHolder> {

    Context context;
    List<PostModel> postList;

    public NewsFeedPagingAdapter(Context context, List<PostModel> postList){
        this.context = context;
        this.postList = postList;
    }

    @NonNull
    @Override
    public PostModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.news_feed_recycler_row, parent, false);
        return new PostModelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostModelViewHolder holder, int position) {
        PostModel model = postList.get(position);
        FirebaseUtil.getOtherProfilePicStorageRef(model.getPostUserId()).getDownloadUrl()
                .addOnCompleteListener(t -> {
                    if (t.isSuccessful()){
                        Uri uri = t.getResult();
                        AndroidUtil.setProfilePic(context, uri, holder.postAvatarImage);
                    }
                });

        holder.postAvatarImage.setOnClickListener(v -> {
            Intent intent = new Intent(context, ProfileActivity.class);
            FirebaseUtil.getUserDetailsById(model.getPostUserId()).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    UserModel userModel = task.getResult().toObject(UserModel.class);
                    AndroidUtil.passUserModelAsIntent(intent, userModel);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });

        });

        FirebaseUtil.getUserDetailsById(model.getPostUserId()).get().addOnCompleteListener(task -> {
            UserModel postOwner = task.getResult().toObject(UserModel.class);
            holder.postOwnerUsernameText.setText(postOwner.getUsername());
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

        if (!model.getPostUserId().contains(FirebaseUtil.currentUserId())){
            holder.moreBtn.setVisibility(View.INVISIBLE);
        } else {
            holder.moreBtn.setVisibility(View.VISIBLE);
        }

        // setup  more_action dialog
        holder.moreBtn.setOnClickListener(v -> {
            Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.bottom_sheet_layout);

            LinearLayout editPostLayout = dialog.findViewById(R.id.edit_post_layout);
            LinearLayout deletePostLayout = dialog.findViewById(R.id.delete_post_layout);
            ImageView cancelBtn = dialog.findViewById(R.id.cancelButton);

            editPostLayout.setOnClickListener(v1 -> {
                Intent intent = new Intent(context, CreatePostActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                AndroidUtil.passPostModelAsIntent(intent, model);
                context.startActivity(intent);

                dialog.dismiss();
            });

            deletePostLayout.setOnClickListener(v1 -> {

                FirebaseUtil.getPostReference(model.getPostId()).delete();
                dialog.dismiss();
                AndroidUtil.showToast(context, "Delete post successfully");

            });

            cancelBtn.setOnClickListener(v1 -> {
                dialog.dismiss();
            });

            dialog.show();
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().getAttributes().windowAnimations = com.google.android.material.R.style.Animation_MaterialComponents_BottomSheetDialog;
            dialog.getWindow().setGravity(Gravity.BOTTOM);
        });

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


    @Override
    public int getItemCount() {
        return postList.size();
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
        ImageButton moreBtn;
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
            postAvatarImage = itemView.findViewById(R.id.post_avatar_image_view);
            postOwnerUsernameText = itemView.findViewById(R.id.post_username_text_view);
            moreBtn = itemView.findViewById(R.id.more_btn);
            postedTimeText = itemView.findViewById(R.id.tv_posted_time);

        }


    }
}
