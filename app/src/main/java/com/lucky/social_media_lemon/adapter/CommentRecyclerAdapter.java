package com.lucky.social_media_lemon.adapter;

import android.content.Context;
import android.net.Uri;
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
    String postId;

    public CommentRecyclerAdapter(@NonNull FirestoreRecyclerOptions<CommentModel> options, Context context, String postId) {
        super(options);
        this.context = context;
        this.postId = postId;
    }



    @Override
    protected void onBindViewHolder(@NonNull CommentRowViewHolder holder, int position, @NonNull CommentModel model) {


        FirebaseUtil.getOtherProfilePicStorageRef(model.getCommentUserId()).getDownloadUrl()
                .addOnCompleteListener(t -> {
                    if (t.isSuccessful()){
                        Uri uri = t.getResult();
                        AndroidUtil.setProfilePic(context, uri, holder.commentAvatarIv);
                    }
                });


        FirebaseUtil.getUserDetailsById(model.getCommentUserId()).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                UserModel commentOwner = task.getResult().toObject(UserModel.class);
                holder.commentUsernameIv.setText(commentOwner.getUsername());

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
}


