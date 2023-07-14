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
import android.widget.Button;
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
    Button friendBtn;
    Button messageBtn;
    TextView phoneText;
    ImageView postImage;
    TextView postText;
    RecyclerView recyclerView;
    UserModel user;
    LinearLayout postLayout;
    ActivityResultLauncher<Intent> coverImagePickLauncher;
    ActivityResultLauncher<Intent> avatarImagePickLauncher;
    Uri selectedCoverImageUri;
    Uri selectedAvatarImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        user = AndroidUtil.getUserModelFromIntent(getIntent());

        backBtn = findViewById(R.id.back_btn);
        wallName = findViewById(R.id.wall_name_text_view);
        coverImage = findViewById(R.id.cover_image_view);
        avatarImage = findViewById(R.id.avatar_image_view);
        coverChangeBtn = findViewById(R.id.change_cover_btn);
        avatarChangeBtn = findViewById(R.id.change_avatar_btn);
        profileName = findViewById(R.id.username_text_view);
        friendBtn = findViewById(R.id.friend_btn);
        messageBtn = findViewById(R.id.message_btn);
        phoneText = findViewById(R.id.phone_text);
        postImage = findViewById(R.id.post_pic_image_view);
        postText = findViewById(R.id.post_text_view);
        postLayout = findViewById(R.id.post_layout);
        recyclerView = findViewById(R.id.wall_recycler_view);

        // Su dung .equals() thay vi "==" moi so sanh duoc 2 chuoi voi nhau
        if (user.getUserId().equals(FirebaseUtil.currentUserId())){
            wallName.setText("My Wall");
            friendBtn.setClickable(false);
            friendBtn.setText("Me");

        } else {
            wallName.setText(user.getUsername() + "'s Wall");
            coverChangeBtn.setVisibility(View.INVISIBLE);
            avatarChangeBtn.setVisibility(View.INVISIBLE);
            friendBtn.setClickable(false);
            postLayout.setVisibility(View.GONE);
        }

        coverImagePickLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK){
                        Intent data = result.getData();
                        if (data!=null && data.getData()!=null){
                            selectedCoverImageUri = data.getData();
                            AndroidUtil.setCoverPic(this, selectedCoverImageUri, coverImage);
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


        FirebaseUtil.getOtherProfilePicStorageRef(user.getUserId()).getDownloadUrl()
                .addOnCompleteListener(t -> {
                    if (t.isSuccessful()){
                        Uri uri = t.getResult();
                        AndroidUtil.setProfilePic(this, uri, avatarImage);
                        AndroidUtil.setProfilePic(this, uri, postImage);
                    }
                });



        profileName.setText(user.getUsername());

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

        messageBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, ChatActivity.class);
            AndroidUtil.passUserModelAsIntent(intent, user);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });

        postText.setOnClickListener(v -> {
            Intent intent = new Intent(this, CreatePostActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });



        phoneText.setText(user.getPhone());

    }
}