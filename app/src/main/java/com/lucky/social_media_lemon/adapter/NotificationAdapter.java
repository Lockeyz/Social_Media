package com.lucky.social_media_lemon.adapter;

import android.content.Context;
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
import com.lucky.social_media_lemon.model.NotificationModel;
import com.lucky.social_media_lemon.utils.FirebaseUtil;

public class NotificationAdapter extends FirestoreRecyclerAdapter<NotificationModel, NotificationAdapter.NotificationModelViewHolder> {

    Context context;

    public NotificationAdapter(@NonNull FirestoreRecyclerOptions<NotificationModel> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull NotificationModelViewHolder holder, int position, @NonNull NotificationModel model) {
        FirebaseUtil.getNotificationReference(model.getNotificationId()).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                NotificationModel notificationModel = task.getResult().toObject(NotificationModel.class);

            }
        });
    }

    @NonNull
    @Override
    public NotificationModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.friend_recycler_row, parent, false);
        return new NotificationModelViewHolder(view);
    }

    class NotificationModelViewHolder extends RecyclerView.ViewHolder{

        ImageView notificationPic;
        TextView contentText;
        Button confirmButton;
        Button deleteRequestButton;

        public NotificationModelViewHolder(@NonNull View itemView) {
            super(itemView);

//            notificationPic = itemView.findViewById(R.id.profile_pic_image_view);
//            contentText = itemView.findViewById(R.id.notification_content_text);
//            confirmButton = itemView.findViewById(R.id.confirm_btn);
//            deleteRequestButton = itemView.findViewById(R.id.delete_request_btn);
        }
    }
}
