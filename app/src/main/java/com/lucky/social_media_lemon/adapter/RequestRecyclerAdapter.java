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
import com.lucky.social_media_lemon.ProfileActivity;
import com.lucky.social_media_lemon.R;
import com.lucky.social_media_lemon.model.RequestModel;
import com.lucky.social_media_lemon.model.UserModel;
import com.lucky.social_media_lemon.utils.AndroidUtil;
import com.lucky.social_media_lemon.utils.FirebaseUtil;

public class RequestRecyclerAdapter extends FirestoreRecyclerAdapter<RequestModel, RequestRecyclerAdapter.RequestModelViewHolder> {

    Context context;

    public RequestRecyclerAdapter(@NonNull FirestoreRecyclerOptions<RequestModel> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull RequestModelViewHolder holder, int position, @NonNull RequestModel model) {

        FirebaseUtil.getOtherProfilePicStorageRef(model.getRequestId()).getDownloadUrl()
                .addOnCompleteListener(t -> {
                    if (t.isSuccessful()){
                        Uri uri = t.getResult();
                        AndroidUtil.setProfilePic(context, uri, holder.profilePic);
                    }
                });

        FirebaseUtil.getUserDetailsById(model.getRequestId()).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                UserModel requestUser = task.getResult().toObject(UserModel.class);
                holder.userName.setText(requestUser.getUsername());
            }
        });

        holder.confirmButton.setOnClickListener(v -> {
            FirebaseUtil.currentUserDetails().get().addOnCompleteListener(task -> {
                FirebaseUtil.getUserDetailsById(model.getRequestId()).get().addOnCompleteListener(task1 -> {
                   if (task.isSuccessful() && task1.isSuccessful()){

                       UserModel requestedUser = task.getResult().toObject(UserModel.class);
                       UserModel requestUser = task1.getResult().toObject(UserModel.class);

                       FirebaseUtil.getFriendReference(model.getRequestId()).set(requestUser);
                       FirebaseUtil.getOtherUserFriendReference(model.getRequestId(), FirebaseUtil.currentUserId()).set(requestedUser);

                       // Xoa document cua nguoi khac truoc vi dang su dung model chinh la document se xoa
                       FirebaseUtil.getOtherUserRequestReference(model.getRequestId(), FirebaseUtil.currentUserId()).delete();
                       FirebaseUtil.getRequestReference(model.getRequestId()).delete();


                       holder.confirmButton.setEnabled(false);
                       holder.confirmButton.setText("Friend");
                       holder.deleteRequestButton.setEnabled(false);
                       holder.deleteRequestButton.setText("Unfriend");
                   }
                });
            });
        });

        holder.deleteRequestButton.setOnClickListener(v -> {

            FirebaseUtil.getOtherUserFriendReference(model.getRequestId(), FirebaseUtil.currentUserId()).delete();
            FirebaseUtil.getRequestReference(model.getRequestId()).delete();

        });

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ProfileActivity.class);
            FirebaseUtil.getUserDetailsById(model.getRequestId()).get().addOnCompleteListener(task -> {
               if (task.isSuccessful()){
                   UserModel requestUser = task.getResult().toObject(UserModel.class);
                   AndroidUtil.passUserModelAsIntent(intent, requestUser);
                   intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                   context.startActivity(intent);
               }
            });

        });


    }

    @NonNull
    @Override
    public RequestModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.request_recycler_row, parent, false);
        return new RequestModelViewHolder(view);
    }

    class RequestModelViewHolder extends RecyclerView.ViewHolder {

        ImageView profilePic;
        TextView userName;
        Button confirmButton;
        Button deleteRequestButton;

        public RequestModelViewHolder(@NonNull View itemView) {
            super(itemView);

            profilePic = itemView.findViewById(R.id.profile_pic_image_view);
            userName = itemView.findViewById(R.id.username_text_view);
            confirmButton = itemView.findViewById(R.id.confirm_btn);
            deleteRequestButton = itemView.findViewById(R.id.delete_request_btn);
        }
    }
}
