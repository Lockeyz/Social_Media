package com.lucky.social_media_lemon;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Filter;
import com.google.firebase.firestore.Query;
import com.lucky.social_media_lemon.adapter.NewsFeedRecyclerAdapter;
import com.lucky.social_media_lemon.model.PostModel;
import com.lucky.social_media_lemon.model.UserModel;
import com.lucky.social_media_lemon.utils.AndroidUtil;
import com.lucky.social_media_lemon.utils.FirebaseUtil;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class HomeFragment extends Fragment {

    ImageView profilePic;
    TextView statusTextView;
    RecyclerView recyclerView;
    NewsFeedRecyclerAdapter adapter;
    LinearLayoutManager layoutManager;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        profilePic = view.findViewById(R.id.profile_image_view);
        statusTextView = view.findViewById(R.id.status_text_view);
        recyclerView = view.findViewById(R.id.recycler_view);

        FirebaseUtil.getCurrentProfilePicStorageRef().getDownloadUrl()
                .addOnCompleteListener(t -> {
                    if (t.isSuccessful()){
                        Uri uri = t.getResult();
                        AndroidUtil.setProfilePic(getContext(), uri, profilePic);
                    }
                });

        profilePic.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), ProfileActivity.class);
            FirebaseUtil.currentUserDetails().get().addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    UserModel currentUser = task.getResult().toObject(UserModel.class);
                    AndroidUtil.passUserModelAsIntent(intent, currentUser);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            });


        });


        statusTextView.setOnClickListener(v -> {
            startActivity(new Intent(getContext(), CreatePostActivity.class));
        });

        setupRecyclerView();

        return view;
    }

    void setupRecyclerView(){

        FirebaseUtil.currentUserDetails().get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){

                UserModel currentUser = task.getResult().toObject(UserModel.class);
                List<String> userIds = currentUser.getFriendIds();
                userIds.add(FirebaseUtil.currentUserId());

                Query query;

                if (currentUser.getFriendIds().isEmpty()){
                    query = FirebaseUtil.allPostCollectionReference()
                            .whereEqualTo("postUserId", FirebaseUtil.currentUserId())
                            .limit(20);
                } else {
                    // Cần tạo Composite index trên Firebase mới có thể sử dụng .orderBy cùng .where()
                    query = FirebaseUtil.allPostCollectionReference()
                            .whereIn("postUserId", userIds)
                            .orderBy("postTime", Query.Direction.DESCENDING)
                            .limit(20);
                }

                FirestoreRecyclerOptions<PostModel> options = new FirestoreRecyclerOptions.Builder<PostModel>()
                        .setQuery(query, PostModel.class).build();

                adapter = new NewsFeedRecyclerAdapter(options, getContext());

                layoutManager = new LinearLayoutManager(getContext());
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);

                layoutManager.onRestoreInstanceState(layoutManager.onSaveInstanceState());
                adapter.startListening();
            }
        });



    }

    @Override
    public void onStart() {
        super.onStart();
        if (adapter!=null)
            adapter.startListening();
    }

    public void onDestroy() {
        super.onDestroy();
        if (adapter!=null){
            adapter.stopListening();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adapter != null){
            adapter.notifyItemRangeChanged(0, adapter.getItemCount());
        }
    }
}