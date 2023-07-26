package com.lucky.social_media_lemon;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.lucky.social_media_lemon.adapter.NewsFeedPagingAdapter;
import com.lucky.social_media_lemon.adapter.NewsFeedRecyclerAdapter;
import com.lucky.social_media_lemon.model.PostModel;
import com.lucky.social_media_lemon.model.UserModel;
import com.lucky.social_media_lemon.utils.AndroidUtil;
import com.lucky.social_media_lemon.utils.FirebaseUtil;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    ImageView profilePic;
    TextView statusTextView;
    RecyclerView recyclerView;
    NewsFeedRecyclerAdapter adapter;
    LinearLayoutManager layoutManager;
//    NewsFeedPagingAdapter adapter;
    List<PostModel> list;
    private DocumentSnapshot lastVisible;
    private boolean isScrolling = false;
    private boolean isLastItemReached = false;
    private int limit = 5;


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
            Intent intent = new Intent(getActivity(), ProfileActivity.class);
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
                            .orderBy("postTime", Query.Direction.DESCENDING)
                            .limit(50);
                } else {
                    // Cần tạo Composite index trên Firebase mới có thể sử dụng .orderBy cùng .where()
                    query = FirebaseUtil.allPostCollectionReference()
                            .whereIn("postUserId", userIds)
                            .orderBy("postTime", Query.Direction.DESCENDING)
                            .limit(50);
                }
                FirestoreRecyclerOptions<PostModel> options = new FirestoreRecyclerOptions.Builder<PostModel>()
                        .setQuery(query, PostModel.class).build();
//                list = new ArrayList<>();
                adapter = new NewsFeedRecyclerAdapter(options, getContext());
                layoutManager = new LinearLayoutManager(getContext());
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);

//                adapter.notifyDataSetChanged();
                adapter.startListening();

                //Phân trang nhưng không sử dụng được real-time
//                setupPagination(query, userIds);
            }
        });

    }

    // Phân trang phải sử dụng với RecyclerViewAdapter thường
    void setupPagination(Query query, List<String> userIds){
        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (DocumentSnapshot document : task.getResult()) {
                    PostModel productModel = document.toObject(PostModel.class);
                    list.add(productModel);
                }

                adapter.notifyDataSetChanged();

                lastVisible = task.getResult().getDocuments().get(task.getResult().size() - 1);
                AndroidUtil.showToast(getContext(), "First page");
                RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                        super.onScrollStateChanged(recyclerView, newState);
                        if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                            isScrolling = true;
                        }
                    }
                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);

                        LinearLayoutManager linearLayoutManager = ((LinearLayoutManager) recyclerView.getLayoutManager());
                        int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();
                        int visibleItemCount = linearLayoutManager.getChildCount();
                        int totalItemCount = linearLayoutManager.getItemCount();

                        if (isScrolling && (firstVisibleItemPosition + visibleItemCount == totalItemCount)) {
                            isScrolling = false;

                            Query nextQuery = FirebaseUtil.allPostCollectionReference()
                                    .whereIn("postUserId", userIds)
                                    .orderBy("postTime", Query.Direction.DESCENDING)
                                    .startAfter(lastVisible)
                                    .limit(limit);

                            nextQuery.get().addOnCompleteListener(t -> {
                                if (t.isSuccessful()) {
                                    for (DocumentSnapshot d : t.getResult()) {
                                        PostModel productModel = d.toObject(PostModel.class);
                                        list.add(productModel);
                                    }

                                    adapter.notifyDataSetChanged();

                                    if (t.getResult().size()!=0){
                                        lastVisible = t.getResult().getDocuments().get(t.getResult().size() - 1);
                                    }
                                }
                            });
                        }
                    }
                };
                recyclerView.addOnScrollListener(onScrollListener);
            }
        });
    }


    @Override
    public void onStart() {
        super.onStart();
        if (adapter!=null) {
            adapter.startListening();
        }
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