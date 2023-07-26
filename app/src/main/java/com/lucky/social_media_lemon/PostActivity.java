package com.lucky.social_media_lemon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.AggregateQuerySnapshot;
import com.google.firebase.firestore.AggregateSource;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.ktx.Firebase;
import com.lucky.social_media_lemon.adapter.NewsFeedPagingAdapter;
import com.lucky.social_media_lemon.adapter.NewsFeedRecyclerAdapter;
import com.lucky.social_media_lemon.constants.Constants;
import com.lucky.social_media_lemon.model.NotificationModel;
import com.lucky.social_media_lemon.model.PostModel;
import com.lucky.social_media_lemon.model.UserModel;
import com.lucky.social_media_lemon.utils.AndroidUtil;
import com.lucky.social_media_lemon.utils.FirebaseUtil;

public class PostActivity extends AppCompatActivity {

    ImageButton backBtn;
    NotificationModel notificationModel;
    RecyclerView recyclerView;
    NewsFeedRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        notificationModel = AndroidUtil.getNotificationModelFromIntent(getIntent());

        backBtn = findViewById(R.id.back_btn);

        recyclerView = findViewById(R.id.recycler_view);

        backBtn.setOnClickListener(v -> {
            onBackPressed();
        });

        setupRecyclerView();

    }

    private void setupRecyclerView() {

        Query query = FirebaseUtil.allPostCollectionReference()
                .whereEqualTo("postId", notificationModel.getNotificationId());

        FirestoreRecyclerOptions<PostModel> options = new FirestoreRecyclerOptions.Builder<PostModel>()
                .setQuery(query, PostModel.class).build();

        adapter = new NewsFeedRecyclerAdapter(options, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }



    @Override
    protected void onStart() {
        super.onStart();
        if (adapter!=null){
            adapter.startListening();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (adapter!=null){
            adapter.stopListening();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (adapter!=null){
            adapter.notifyDataSetChanged();
        }
    }
}