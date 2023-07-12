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
import com.google.firebase.firestore.Query;
import com.lucky.social_media_lemon.adapter.FriendListRecyclerAdapter;
import com.lucky.social_media_lemon.adapter.RequestRecyclerAdapter;
import com.lucky.social_media_lemon.model.RequestModel;
import com.lucky.social_media_lemon.model.UserModel;
import com.lucky.social_media_lemon.utils.FirebaseUtil;


public class FriendFragment extends Fragment {

    RecyclerView requestRecyclerView;
    RecyclerView friendRecyclerView;
    RequestRecyclerAdapter requestAdapter;
    FriendListRecyclerAdapter friendAdapter;
    LinearLayoutManager requestLayoutManager;
    LinearLayoutManager friendLayoutManager;
    public FriendFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_friend, container, false);
        requestRecyclerView = view.findViewById(R.id.add_friend_request_recycler_view);
        friendRecyclerView = view.findViewById(R.id.friend_list_recycler_view);

//        setupRequestRecyclerView();
        setupFriendRecyclerView();


        return view;


    }

    void setupFriendRecyclerView() {

        // setup request
        // ?? su dung .oderBy() thi khong lay dc query??
        // .orderBy("requestTime", Query.Direction.ASCENDING)
        // .orderBy("requestTime", Query.Direction.DESCENDING);

        Query query = FirebaseUtil.allRequestCollectionReference()
                .whereEqualTo("isRequestUser", false);

        FirestoreRecyclerOptions<RequestModel> options = new FirestoreRecyclerOptions.Builder<RequestModel>()
                .setQuery(query, RequestModel.class).build();

        requestAdapter = new RequestRecyclerAdapter(options, getContext());

        requestLayoutManager = new LinearLayoutManager(getContext());
        requestRecyclerView.setLayoutManager(requestLayoutManager);
        requestRecyclerView.setAdapter(requestAdapter);

        requestAdapter.startListening();

        // setup friend list
        Query query1 = FirebaseUtil.allFriendCollectionReference()
                .orderBy("username", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<UserModel> options1 = new FirestoreRecyclerOptions.Builder<UserModel>()
                .setQuery(query1, UserModel.class).build();

        friendAdapter = new FriendListRecyclerAdapter(options1, getContext());

        friendLayoutManager = new LinearLayoutManager(getContext());
        friendRecyclerView.setLayoutManager(friendLayoutManager);
        friendRecyclerView.setAdapter(friendAdapter);

        friendAdapter.startListening();

        RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (recyclerView == requestRecyclerView) {
                    int lastVisibleItemPosition = requestLayoutManager.findLastVisibleItemPosition();
                    int totalItemCount = requestLayoutManager.getItemCount();
                    if (lastVisibleItemPosition == totalItemCount - 1) {
                        // Cuộn hết item trong RecyclerView 1
                        // Chuyển đến RecyclerView 2
                        friendRecyclerView.scrollToPosition(0);
                    }
                }
            }
        };

        requestRecyclerView.addOnScrollListener(scrollListener);

    }

    void setupRequestRecyclerView() {

        Query query1 = FirebaseUtil.allRequestCollectionReference()
                .whereEqualTo("isRequestUser", false)
                .orderBy("requestTime", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<RequestModel> options = new FirestoreRecyclerOptions.Builder<RequestModel>()
                .setQuery(query1, RequestModel.class).build();

        requestAdapter = new RequestRecyclerAdapter(options, getContext());

        requestLayoutManager = new LinearLayoutManager(getContext());
        requestRecyclerView.setLayoutManager(requestLayoutManager);
        requestRecyclerView.setAdapter(requestAdapter);

        requestAdapter.startListening();


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