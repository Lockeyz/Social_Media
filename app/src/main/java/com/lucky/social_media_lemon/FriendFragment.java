package com.lucky.social_media_lemon;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.Filter;
import com.google.firebase.firestore.Query;
import com.lucky.social_media_lemon.adapter.FriendListRecyclerAdapter;
import com.lucky.social_media_lemon.adapter.RequestRecyclerAdapter;
import com.lucky.social_media_lemon.model.RequestModel;
import com.lucky.social_media_lemon.model.UserModel;
import com.lucky.social_media_lemon.utils.AndroidUtil;
import com.lucky.social_media_lemon.utils.FirebaseUtil;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;


public class FriendFragment extends Fragment {

    RecyclerView requestRecyclerView;
    RecyclerView friendRecyclerView;
    RequestRecyclerAdapter requestAdapter;
    FriendListRecyclerAdapter friendAdapter;
    public FriendFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_friend, container, false);
        requestRecyclerView = view.findViewById(R.id.add_friend_request_recycler_view);
        friendRecyclerView = view.findViewById(R.id.friend_list_recycler_view);

        setupRequestRecyclerView();
        setupFriendRecyclerView();

        return view;


    }

    void setupRequestRecyclerView() {

        // Cần tạo Composite index trên Firebase mới có thể sử dụng .orderBy cùng .where()
        Query query = FirebaseUtil.allRequestCollectionReference()
                .whereEqualTo("isRequestUser", false)
                .orderBy("requestTime", Query.Direction.DESCENDING);


        FirestoreRecyclerOptions<RequestModel> options = new FirestoreRecyclerOptions.Builder<RequestModel>()
                .setQuery(query, RequestModel.class).build();

        requestAdapter = new RequestRecyclerAdapter(options, getContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        requestRecyclerView.setLayoutManager(linearLayoutManager);
        requestRecyclerView.setAdapter(requestAdapter);

        requestAdapter.startListening();
    }
    void setupFriendRecyclerView() {

        Query query = FirebaseUtil.allFriendCollectionReference()
                .orderBy("username", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<UserModel> options = new FirestoreRecyclerOptions.Builder<UserModel>()
                .setQuery(query, UserModel.class).build();

        friendAdapter = new FriendListRecyclerAdapter(options, getContext());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        friendRecyclerView.setLayoutManager(linearLayoutManager);
        friendRecyclerView.setAdapter(friendAdapter);

        friendAdapter.startListening();

    }

    @Override
    public void onStart() {
        super.onStart();
        if (friendAdapter != null || requestAdapter!=null) {
            requestAdapter.startListening();
            friendAdapter.startListening();
        }
    }

    public void onDestroy() {
        super.onDestroy();
        if (friendAdapter!=null || requestAdapter!=null){
            requestAdapter.stopListening();
            friendAdapter.stopListening();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (friendAdapter!=null || requestAdapter!=null){
            requestAdapter.notifyDataSetChanged();
            friendAdapter.notifyDataSetChanged();
        }
    }

}