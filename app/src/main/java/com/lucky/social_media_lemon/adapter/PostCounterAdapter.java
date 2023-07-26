package com.lucky.social_media_lemon.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.AggregateQuerySnapshot;
import com.google.firebase.firestore.AggregateSource;
import com.google.firebase.firestore.DocumentSnapshot;
import com.lucky.social_media_lemon.R;
import com.lucky.social_media_lemon.model.PostModel;
import com.lucky.social_media_lemon.model.UserModel;
import com.lucky.social_media_lemon.utils.AndroidUtil;
import com.lucky.social_media_lemon.utils.FirebaseUtil;

public class PostCounterAdapter extends FirestoreRecyclerAdapter<UserModel, PostCounterAdapter.PostCounterViewHolder> {



    Context context;
    public PostCounterAdapter(@NonNull FirestoreRecyclerOptions<UserModel> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull PostCounterViewHolder holder, int position, @NonNull UserModel model) {
        FirebaseUtil.getOtherProfilePicStorageRef(model.getUserId()).getDownloadUrl()
                .addOnCompleteListener(t -> {
                    if (t.isSuccessful()){
                        Uri uri = t.getResult();
                        AndroidUtil.setProfilePic(context, uri, holder.profilePic);
                    }
                });

        holder.userName.setText(model.getUsername());
        FirebaseUtil.allPostCollectionReference().whereEqualTo("postUserId", model.getUserId()).count().get(AggregateSource.SERVER).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                AggregateQuerySnapshot snapshot = task.getResult();
                holder.postCounter.setText(String.valueOf(snapshot.getCount()));
            }
        });

        super.startListening();


    }

    @NonNull
    @Override
    public PostCounterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.thongke, parent, false);
        return new PostCounterViewHolder(view);
    }


    class PostCounterViewHolder extends RecyclerView.ViewHolder{
        ImageView profilePic;
        TextView userName;
        TextView postCounter;

        public PostCounterViewHolder(@NonNull View itemView) {
            super(itemView);
            profilePic = itemView.findViewById(R.id.profile_pic_image_view);
            userName = itemView.findViewById(R.id.username_text_view);
            postCounter = itemView.findViewById(R.id.post_counter);
        }
    }
}
