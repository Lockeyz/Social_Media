package com.lucky.social_media_lemon;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.lucky.social_media_lemon.adapter.FriendRecyclerAdapter;
import com.lucky.social_media_lemon.adapter.RequestRecyclerAdapter;
import com.lucky.social_media_lemon.model.RequestModel;
import com.lucky.social_media_lemon.utils.FirebaseUtil;


public class FriendFragment extends Fragment {

    RecyclerView requestRecyclerView;
    RequestRecyclerAdapter requestAdapter;
    LinearLayoutManager layoutManager;

    public FriendFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_friend, container, false);
        requestRecyclerView = view.findViewById(R.id.add_friend_request_recycler_view);

        setupRequestRecyclerView();
        return view;


    }

    void setupRequestRecyclerView() {

        Query query = FirebaseUtil.allRequestCollectionReference()
                .whereEqualTo("isRequestUser", false)
                .orderBy("requestTime", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<RequestModel> options = new FirestoreRecyclerOptions.Builder<RequestModel>()
                .setQuery(query, RequestModel.class).build();

        requestAdapter = new RequestRecyclerAdapter(options, getContext());

        layoutManager = new LinearLayoutManager(getContext());
        requestRecyclerView.setLayoutManager(layoutManager);
        requestRecyclerView.setAdapter(requestAdapter);

        requestAdapter.startListening();

//        requestAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
//            @Override
//            public void onItemRangeInserted(int positionStart, int itemCount) {
//                super.onItemRangeInserted(positionStart, itemCount);
//                requestRecyclerView.smoothScrollToPosition(0);
//            }
//        });

    }

    @Override
    public void onStart() {
        super.onStart();
        if (requestAdapter!=null)
            requestAdapter.startListening();
    }

    public void onDestroy() {
        super.onDestroy();
        if (requestAdapter!=null){
            requestAdapter.stopListening();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (requestAdapter != null){
            requestAdapter.notifyDataSetChanged();
        }
    }

}