package com.lucky.social_media_lemon;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;
import com.lucky.social_media_lemon.adapter.NewsFeedRecyclerAdapter;
import com.lucky.social_media_lemon.adapter.NotificationAdapter;
import com.lucky.social_media_lemon.adapter.PostCounterAdapter;
import com.lucky.social_media_lemon.model.NotificationModel;
import com.lucky.social_media_lemon.model.PostModel;
import com.lucky.social_media_lemon.model.UserModel;
import com.lucky.social_media_lemon.utils.AndroidUtil;
import com.lucky.social_media_lemon.utils.FirebaseUtil;

import java.util.List;


public class NotificationFragment extends Fragment {

RecyclerView recyclerView;
NotificationAdapter adapter;
LinearLayoutManager linearLayoutManager;


    public NotificationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        setupRecyclerView();
        return view;
    }

    void setupRecyclerView(){
        FirebaseUtil.currentUserDetails().get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                List<String> friendIds = task.getResult().toObject(UserModel.class).getFriendIds();

                if (!friendIds.isEmpty()){
                    Query query = FirebaseUtil.allNotificationCollectionReference()
                            .whereIn("notificationUserId", friendIds);
                    FirestoreRecyclerOptions<NotificationModel> options = new FirestoreRecyclerOptions.Builder<NotificationModel>()
                            .setQuery(query, NotificationModel.class).build();

                    adapter = new NotificationAdapter(options, getContext());

                    linearLayoutManager = new LinearLayoutManager(getContext());
                    recyclerView.setLayoutManager(linearLayoutManager);
                    recyclerView.setAdapter(adapter);
                    adapter.startListening();
                }
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
            adapter.notifyDataSetChanged();
        }
    }

}