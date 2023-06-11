package com.lucky.social_media_lemon.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.lucky.social_media_lemon.R;
import com.lucky.social_media_lemon.model.PostModel;

public class NewsFeedRecyclerAdapter extends FirestoreRecyclerAdapter<PostModel, NewsFeedRecyclerAdapter.PostModelViewHolder> {

    Context context;

    public NewsFeedRecyclerAdapter(@NonNull FirestoreRecyclerOptions<PostModel> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull PostModelViewHolder holder, int position, @NonNull PostModel model) {
        holder.captionText.setText(model.getCaption());
        Glide.with(context).load(model.getPictureUrl()).into(holder.postPicImage);
        holder.likeCounterText.setText(model.getLikeCounter()+"");
        holder.commentCounterText.setText(model.getCommentCounter()+" comments");
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
        ImageButton commentIconBtn;
        ImageButton shareIconBtn;

        public PostModelViewHolder(@NonNull View itemView) {
            super(itemView);
            captionText = itemView.findViewById(R.id.caption_text_view);
            postPicImage = itemView.findViewById(R.id.pic_image_view);
            likeCounterText = itemView.findViewById(R.id.like_counter_text_view);
            commentCounterText = itemView.findViewById(R.id.comment_counter_text_view);
            likeIconBtn = itemView.findViewById(R.id.like_icon_btn);
            commentIconBtn = itemView.findViewById(R.id.comment_icon_btn);
            shareIconBtn = itemView.findViewById(R.id.share_icon_btn);
        }
    }
}
