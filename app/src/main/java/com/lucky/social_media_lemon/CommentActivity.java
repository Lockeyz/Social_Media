package com.lucky.social_media_lemon;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.Query;
import com.lucky.social_media_lemon.adapter.CommentRecyclerAdapter;
import com.lucky.social_media_lemon.constants.Constants;
import com.lucky.social_media_lemon.model.CommentModel;
import com.lucky.social_media_lemon.utils.FirebaseUtil;

public class CommentActivity extends AppCompatActivity {
    ImageButton btnBack;
    EditText etComment;
    ImageButton btnSend;
    RecyclerView rvCommentList;
    LinearLayoutManager layoutManager;
    CommentRecyclerAdapter adapter;
    String postId;
    String commentUserId;

    private void getViews(){
        btnBack = findViewById(R.id.ib_comment_back);
        etComment = findViewById(R.id.et_comment_input);
        btnSend = findViewById(R.id.ib_comment_send);
        rvCommentList = findViewById(R.id.rv_comment_list);
    }

    private void getData(){
        postId = getIntent().getStringExtra(Constants.EXTRA_POST_ID_NEWS_FEED_RECYCLER_ADAPTER_TO_COMMENT_ACTIVITY);
        commentUserId = FirebaseUtil.currentUserId();
    }

    private void setupListeners (){
        btnBack.setOnClickListener(view -> {
            onBackPressed();
        });

        btnSend.setOnClickListener(view -> {
            btnSend.setEnabled(false);
            String commentId = FirebaseUtil.postCommentsCollectionReference(postId).document().getId();
            String comment = etComment.getText().toString();
            Timestamp commentTime = Timestamp.now();

            CommentModel newComment = new CommentModel(commentId, comment, commentTime, commentUserId);
            FirebaseUtil.postCommentsCollectionReference(postId).document().set(newComment).addOnCompleteListener(task ->{
                etComment.setText("");
                btnSend.setEnabled(true);
            });
        });

        etComment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.toString().trim().length() > 0){
                    btnSend.setColorFilter(getResources().getColor(R.color.my_primary));
                    btnSend.setEnabled(true);
                }
                else{
                    btnSend.setColorFilter(getResources().getColor(R.color.grey_text));
                    btnSend.setEnabled(false);
                }
            }
        });
    }

    private void setUpRecyclerView (){
        Query query = FirebaseUtil.postCommentsCollectionReference(postId)
                .orderBy("commentTime", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<CommentModel> options = new FirestoreRecyclerOptions.Builder<CommentModel>()
                .setQuery(query, CommentModel.class).build();

        adapter = new CommentRecyclerAdapter(options, CommentActivity.this);

        layoutManager = new LinearLayoutManager(CommentActivity.this);

        rvCommentList.setLayoutManager(layoutManager);
        rvCommentList.setAdapter(adapter);

        layoutManager.onRestoreInstanceState(layoutManager.onSaveInstanceState());
        adapter.startListening();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        getViews();
        getData();
        setupListeners();
        setUpRecyclerView();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (adapter!=null)
            adapter.startListening();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (adapter!=null)
            adapter.stopListening();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adapter!=null)
            adapter.notifyItemRangeChanged(0, adapter.getItemCount());
    }
}
