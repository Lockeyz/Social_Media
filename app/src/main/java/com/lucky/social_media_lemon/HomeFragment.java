package com.lucky.social_media_lemon;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.Query;
import com.lucky.social_media_lemon.adapter.NewsFeedRecyclerAdapter;
import com.lucky.social_media_lemon.model.PostModel;
import com.lucky.social_media_lemon.utils.AndroidUtil;
import com.lucky.social_media_lemon.utils.FirebaseUtil;

import java.util.Arrays;

public class HomeFragment extends Fragment {

    ImageView profilePic;
    TextView statusTextView;
    RecyclerView recyclerView;
    NewsFeedRecyclerAdapter adapter;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        profilePic = view.findViewById(R.id.profile_image_view);

        FirebaseUtil.getCurrentProfilePicStorageRef().getDownloadUrl()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        Uri uri = task.getResult();
                        AndroidUtil.setProfilePic(getContext(), uri, profilePic);
                    }
                });

        statusTextView = view.findViewById(R.id.status_text_view);

        statusTextView.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), CreatePostActivity.class));
        });
        recyclerView = view.findViewById(R.id.recycler_view);
        setupRecyclerView();

        return view;
    }

    void setupRecyclerView(){
        Query query = FirebaseUtil.allPostCollectionReference()
                .orderBy("postTime", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<PostModel> options = new FirestoreRecyclerOptions.Builder<PostModel>()
                .setQuery(query, PostModel.class).build();

        adapter = new NewsFeedRecyclerAdapter(options, getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }



    @Override
    public void onStart() {
        super.onStart();
        if (adapter!=null)
            adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (adapter!=null)
            adapter.stopListening();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adapter!=null)
//            adapter.startListening();
            adapter.notifyDataSetChanged();
    }
}