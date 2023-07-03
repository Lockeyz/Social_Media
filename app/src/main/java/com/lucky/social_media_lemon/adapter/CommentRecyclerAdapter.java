package com.lucky.social_media_lemon.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.lucky.social_media_lemon.R;
import com.lucky.social_media_lemon.model.CommentModel;
import com.lucky.social_media_lemon.model.UserModel;
import com.lucky.social_media_lemon.utils.AndroidUtil;
import com.lucky.social_media_lemon.utils.FirebaseUtil;

public class CommentRecyclerAdapter extends FirestoreRecyclerAdapter<CommentModel, CommentRecyclerAdapter.CommentRowViewHolder> {
    Context context;

    public CommentRecyclerAdapter(@NonNull FirestoreRecyclerOptions<CommentModel> options, Context context) {
        super(options);
        this.context = context;
    }

    class CommentRowViewHolder extends RecyclerView.ViewHolder{
        ImageView commentAvatarIv;
        TextView commentUsernameIv;
        TextView commentContentIv;
        TextView commentTimeIv;

        public void getViews(View itemView){
            commentAvatarIv = itemView.findViewById(R.id.iv_comment_avatar);
            commentUsernameIv = itemView.findViewById(R.id.tv_comment_username);
            commentContentIv = itemView.findViewById(R.id.tv_comment_content);
            commentTimeIv = itemView.findViewById(R.id.tv_comment_time);
        }

        public CommentRowViewHolder(@NonNull View itemView) {
            super(itemView);
            getViews(itemView);
        }
    }

    @Override
    protected void onBindViewHolder(@NonNull CommentRowViewHolder holder, int position, @NonNull CommentModel model) {
        FirebaseUtil.getUserDetailsById(model.getCommentUserId()).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                UserModel postOwner = task.getResult().toObject(UserModel.class);
                holder.commentUsernameIv.setText(postOwner.getUsername());
                if(postOwner.getAvatarUrl() != null){
                    AndroidUtil.setProfilePic(context, postOwner.getAvatarUrl(), holder.commentAvatarIv);
                }

            }
        });

        holder.commentContentIv.setText(model.getComment());
        holder.commentTimeIv.setText(FirebaseUtil.timestampToFullDateAndHourString(model.getCommentTime()));
    }


    @NonNull
    @Override
    public CommentRowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.comment_recycler_row , parent, false);
        return new CommentRowViewHolder(view);
    }
}


