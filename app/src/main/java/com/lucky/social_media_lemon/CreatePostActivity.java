package com.lucky.social_media_lemon;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.lucky.social_media_lemon.model.PostModel;
import com.lucky.social_media_lemon.utils.AndroidUtil;
import com.lucky.social_media_lemon.utils.FirebaseUtil;

import java.util.ArrayList;
import java.util.List;

public class CreatePostActivity extends AppCompatActivity {

    ImageButton backBtn;
    TextView postBtn;
    EditText captionInput;
    ImageView image;
    ImageButton addImageBtn;
    ProgressBar progressBar;
    Uri imageUri;
//    final  private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Images");
    final private StorageReference storageReference = FirebaseStorage.getInstance().getReference("ImagesStore");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        backBtn = findViewById(R.id.back_btn);
        postBtn = findViewById(R.id.post_btn_text_view);
        captionInput = findViewById(R.id.cation_edit_text);
        image = findViewById(R.id.picture_image_view);
        addImageBtn = findViewById(R.id.add_image_btn);
        progressBar = findViewById(R.id.progressBar);

        backBtn.setOnClickListener(v -> {
            onBackPressed();
        });

        postBtn.setOnClickListener(v -> {
            uploadToFirebase();
        });

        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK){
                            Intent data = result.getData();
                            imageUri = data.getData();
                            image.setImageURI(imageUri);
                        } else {
                            Toast.makeText(CreatePostActivity.this, "No Image Selected", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        addImageBtn.setOnClickListener(v -> {
            Intent photoPicker = new Intent();
            photoPicker.setAction(Intent.ACTION_GET_CONTENT);
            photoPicker.setType("image/*");
            activityResultLauncher.launch(photoPicker);
        });
    }

    private void uploadPost(String pictureUrl){
        String caption = captionInput.getText().toString();
        String postId = FirebaseUtil.allPostCollectionReference().document().getId();
        String postUserId = FirebaseUtil.currentUserId();
        Timestamp postTime = Timestamp.now();
        List<String> likedUserIds = new ArrayList<>();

        PostModel postModel = new PostModel(postId, postUserId, postTime, caption, pictureUrl,
                likedUserIds);
        FirebaseUtil.getPostReference(postId).set(postModel);
        AndroidUtil.showToast(CreatePostActivity.this, "Your post was uploaded");
        Intent intent = new Intent(CreatePostActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void uploadToFirebase(){
        if(imageUri == null){
            uploadPost("");
        }
        else{
            final StorageReference imageReference = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(imageUri));
            imageReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    imageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            uploadPost(uri.toString());
                        }
                    });
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                    progressBar.setVisibility(View.VISIBLE);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(CreatePostActivity   .this, "Failed", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }



    private String getFileExtension(Uri fileUri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(fileUri));
    }

//    void setPostWithImage(boolean withImage){
//        if (withImage){
//            image.setVisibility(View.VISIBLE);
//        } else {
//            image.setVisibility(View.INVISIBLE);
//        }
//    }
}