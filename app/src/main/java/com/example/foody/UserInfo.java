package com.example.foody;

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

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.io.InputStream;

public class UserInfo extends AppCompatActivity {

    ImageView imageView;
    private static final int GALLERY_REQUEST = 100;
    EditText name;
    Button btnSave;
    Uri selectedImage;

    @Override
     protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
         super.onActivityResult(requestCode, resultCode, data);

         if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK && data != null && data.getData()!=null) {
             try {
                 selectedImage = data.getData();
                 InputStream imageStream = getContentResolver().openInputStream(selectedImage);
                 imageView.setImageBitmap(BitmapFactory.decodeStream(imageStream));
             } catch (IOException e) {
                 e.printStackTrace();
             }
         }
    }

    private void showImageChooser(){
        /*Intent intent = new Intent();
        intent.setType("Image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select picture"), GALLERY_REQUEST);*/
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

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImageChooser();
            }
        });


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(UserInfo.this, "hello", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
