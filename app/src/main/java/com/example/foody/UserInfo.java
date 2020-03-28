package com.example.foody;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.io.InputStream;

public class UserInfo extends AppCompatActivity {

    ImageView imageView;
    private static final int GALLERY_REQUEST = 100;
    private  DatabaseReference newUser;
    EditText name;
    EditText phno;
    Button btnSave;
    Uri selectedImage;
    String profileImageUrl;

    @Override
     protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
         super.onActivityResult(requestCode, resultCode, data);

         if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK && data != null && data.getData()!=null) {
             try {
                 selectedImage = data.getData();
                 InputStream imageStream = getContentResolver().openInputStream(selectedImage);
                 imageView.setImageBitmap(BitmapFactory.decodeStream(imageStream));
                 uploadImageToFirebaseStorage();
             } catch (IOException e) {
                 e.printStackTrace();
             }
         }
    }

    private void uploadImageToFirebaseStorage() {
        StorageReference profileImageRef =
                FirebaseStorage.getInstance().getReference("profilepics/"+System.currentTimeMillis()+".jpg");
        if(selectedImage != null) {
            profileImageRef.putFile(selectedImage)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Get a URL to the uploaded content
                            profileImageUrl = taskSnapshot.getStorage().getDownloadUrl().toString();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(UserInfo.this, "Error", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void showImageChooser(){
        Intent intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, GALLERY_REQUEST);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        imageView = findViewById(R.id.photos);
        name = findViewById(R.id.name);
        btnSave = findViewById(R.id.save);
        phno = findViewById(R.id.phoneNumber);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImageChooser();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserInformation();
            }
        });
    }

    private void saveUserInformation() {
        String displayName = name.getText().toString();
        String displayNumber = phno.getText().toString();

        if(displayName.isEmpty()){
            name.setError("Name required");
            name.requestFocus();
        }
        else {
            DatabaseReference mDatabase =FirebaseDatabase.getInstance().getReference();
            FirebaseUser mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
            if (mCurrentUser == null) throw new AssertionError();
            newUser = mDatabase.child(mCurrentUser.getUid());
            newUser.child("name").setValue(displayName);
            newUser.child("Phno").setValue(displayNumber);
            newUser.child("Image").setValue(profileImageUrl);
        }
    }
}
