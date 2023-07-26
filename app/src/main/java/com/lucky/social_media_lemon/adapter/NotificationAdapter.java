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
import com.lucky.social_media_lemon.PostActivity;
import com.lucky.social_media_lemon.R;
import com.lucky.social_media_lemon.model.NotificationModel;
import com.lucky.social_media_lemon.model.UserModel;
import com.lucky.social_media_lemon.utils.AndroidUtil;
import com.lucky.social_media_lemon.utils.FirebaseUtil;

public class NotificationAdapter extends FirestoreRecyclerAdapter<NotificationModel, NotificationAdapter.NotificationModelViewHolder> {

    Context context;

    public NotificationAdapter(@NonNull FirestoreRecyclerOptions<NotificationModel> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull NotificationModelViewHolder holder, int position, @NonNull NotificationModel model) {
        FirebaseUtil.getOtherProfilePicStorageRef(model.getNotificationUserId()).getDownloadUrl()
                .addOnCompleteListener(t -> {
                    if (t.isSuccessful()){
                        Uri uri = t.getResult();
                        AndroidUtil.setProfilePic(context, uri, holder.notificationPic);
                    }
                });
        FirebaseUtil.getUserDetailsById(model.getNotificationUserId()).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                UserModel userModel = task.getResult().toObject(UserModel.class);
                holder.usernameText.setText(userModel.getUsername());
            }
        });

        holder.contentText.setText(model.getNotificationContent());
        holder.timeText.setText(FirebaseUtil.timestampToFullDateAndHourString(model.getNotificationTime()));

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, PostActivity.class);
            AndroidUtil.passNotificationModelAsIntent(intent, model);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        });

    }

    @NonNull
    @Override
    public NotificationModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.notification_recycler_row, parent, false);
        return new NotificationModelViewHolder(view);
    }

    class NotificationModelViewHolder extends RecyclerView.ViewHolder{

        ImageView notificationPic;
        TextView usernameText;
        TextView contentText;
        TextView timeText;


        public NotificationModelViewHolder(@NonNull View itemView) {
            super(itemView);

            notificationPic = itemView.findViewById(R.id.profile_pic_image_view);
            usernameText = itemView.findViewById(R.id.username_text_view);
            contentText = itemView.findViewById(R.id.notification_content_text_view);
            timeText = itemView.findViewById(R.id.notification_time_text_view);

        }
    }
}
