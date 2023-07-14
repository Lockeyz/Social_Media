package com.lucky.social_media_lemon;

import android.os.Bundle;

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
import com.lucky.social_media_lemon.adapter.RecentChatRecyclerAdapter;
import com.lucky.social_media_lemon.model.ChatRoomModel;
import com.lucky.social_media_lemon.utils.FirebaseUtil;


public class ChatFragment extends Fragment {

    RecyclerView recyclerView;
    RecentChatRecyclerAdapter adapter;

    public ChatFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);
        setupRecyclerView();
        return view;
    }

    void setupRecyclerView(){

        // Nếu sử dụng .orderBy() để sắp xếp theo trường Time cùng .where() thì trong
        // .where() cũng cần có trường Time, còn không sẽ không lấy được query
        // Sử dụng .notEqualTo() thì load ngay, .lessThanOrEqualTo thì phải tải lại lần nữa mới load
        //
        Query query = FirebaseUtil.allChatroomCollectionReference()
                .where(Filter.and(Filter.arrayContains("userIds", FirebaseUtil.currentUserId()),
                                Filter.notEqualTo("lastMessageTimestamp", Timestamp.now())))
                .orderBy("lastMessageTimestamp", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<ChatRoomModel> options = new FirestoreRecyclerOptions.Builder<ChatRoomModel>()
                .setQuery(query, ChatRoomModel.class).build();

        adapter = new RecentChatRecyclerAdapter(options, getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (adapter!=null){
            adapter.startListening();
        }

    }

    @Override
    public void onStop() {
        super.onStop();
        if (adapter!=null){
            adapter.stopListening();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adapter!=null){
            adapter.notifyDataSetChanged();
        }
    }
}