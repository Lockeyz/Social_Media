package com.lucky.social_media_lemon;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.lucky.social_media_lemon.model.UserModel;
import com.lucky.social_media_lemon.utils.AndroidUtil;
import com.lucky.social_media_lemon.utils.FirebaseUtil;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class ProfileActivity extends AppCompatActivity {

    ImageButton backBtn;
    TextView wallName;
    ImageView coverImage;
    ImageView avatarImage;
    ImageButton coverChangeBtn;
    ImageButton avatarChangeBtn;
    TextView profileName;
    ImageView postImage;
    TextView postText;
    RecyclerView recyclerView;
    UserModel otherUser;
    LinearLayout postLayout;
    ActivityResultLauncher<Intent> coverImagePickLauncher;
    ActivityResultLauncher<Intent> avatarImagePickLauncher;
    Uri selectedCoverImageUri;
    Uri selectedAvatarImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        otherUser = AndroidUtil.getUserModelFromIntent(getIntent());

        backBtn = findViewById(R.id.back_btn);
        wallName = findViewById(R.id.wall_name_text_view);
        coverImage = findViewById(R.id.cover_image_view);
        avatarImage = findViewById(R.id.avatar_image_view);
        coverChangeBtn = findViewById(R.id.change_cover_btn);
        avatarChangeBtn = findViewById(R.id.change_avatar_btn);
        profileName = findViewById(R.id.username_text_view);
        postImage = findViewById(R.id.post_pic_image_view);
        postText = findViewById(R.id.post_text_view);
        postLayout = findViewById(R.id.post_layout);
        recyclerView = findViewById(R.id.wall_recycler_view);

        if (otherUser.getUserId() == FirebaseUtil.currentUserId()){
            AndroidUtil.showToast(this, otherUser.getUserId());
            AndroidUtil.showToast(this, FirebaseUtil.currentUserId());
            coverChangeBtn.setVisibility(View.INVISIBLE);
            avatarChangeBtn.setVisibility(View.INVISIBLE);
            postLayout.setVisibility(View.GONE);
        }
//        if (otherUser.getUserId() == FirebaseUtil.currentUserId()){
//            coverChangeBtn.setVisibility(View.VISIBLE);
//            avatarChangeBtn.setVisibility(View.VISIBLE);
//            postLayout.setVisibility(View.VISIBLE);
//        }

        coverImagePickLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK){
                        Intent data = result.getData();
                        if (data!=null && data.getData()!=null){
                            selectedCoverImageUri = data.getData();
                            AndroidUtil.setProfilePic(this, selectedCoverImageUri, coverImage);
                        }
                    }
                }
        );

        avatarImagePickLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK){
                        Intent data = result.getData();
                        if (data!=null && data.getData()!=null){
                            selectedAvatarImageUri = data.getData();
                            AndroidUtil.setProfilePic(this, selectedAvatarImageUri, avatarImage);
                        }
                    }
                }
        );



        backBtn.setOnClickListener(v -> {
            onBackPressed();
        });

        wallName.setText(otherUser.getUsername() + "'s Wall");

        FirebaseUtil.getOtherProfilePicStorageRef(otherUser.getUserId()).getDownloadUrl()
                .addOnCompleteListener(t -> {
                    if (t.isSuccessful()){
                        Uri uri = t.getResult();
                        AndroidUtil.setProfilePic(this, uri, avatarImage);
                    }
                });

        profileName.setText(otherUser.getUsername());

        coverChangeBtn.setOnClickListener(v -> {
            ImagePicker.with(this).cropSquare().compress(512).maxResultSize(512, 512)
                    .createIntent(new Function1<Intent, Unit>() {
                        @Override
                        public Unit invoke(Intent intent) {
                            coverImagePickLauncher.launch(intent);
                            return null;
                        }
                    });
            });

        avatarChangeBtn.setOnClickListener(v -> {
            ImagePicker.with(this).cropSquare().compress(512).maxResultSize(512, 512)
                    .createIntent(new Function1<Intent, Unit>() {
                        @Override
                        public Unit invoke(Intent intent) {
                            avatarImagePickLauncher.launch(intent);
                            return null;
                        }
                    });
        });

    }
}