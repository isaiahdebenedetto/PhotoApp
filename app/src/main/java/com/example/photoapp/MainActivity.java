package com.example.photoapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ImageView iv_photo;
    TextView tv_message;
    Button btn_list, btn_load, btn_take;

    List<Uri> uriList;

    String mCurrentPhotoPath;

    static final int REQUEST_TAKE_PHOTO = 1;

    static final int SELECT_A_PHOTO = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iv_photo = findViewById(R.id.iv_photo);
        tv_message = findViewById(R.id.tv_message);
        btn_list = findViewById(R.id.btn_list);
        btn_load = findViewById(R.id.btn_load);
        btn_take = findViewById(R.id.btn_take);

        btn_take.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });

        btn_load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // create an intent to select a photo from the gallery
                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                // start the intent with a request code
                startActivityForResult(i, SELECT_A_PHOTO);
            }
        });

        btn_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent( v.getContext(), PhotoList.class);
                startActivity(i);
            }
        });

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        ImageView iv_photo;
        iv_photo = findViewById(R.id.iv_photo);

        TextView tv_message;
        tv_message = findViewById(R.id.tv_message);

        uriList = ( (MyApplication) this.getApplication()).getUriList();

        if ( requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            // set the image in the iv_photo view
            Glide.with(this).load(mCurrentPhotoPath).into(iv_photo);
            // set text to image path
            tv_message.setText(mCurrentPhotoPath);

            uriList.add( Uri.fromFile(new File(mCurrentPhotoPath)));

        }
        if (requestCode == SELECT_A_PHOTO && resultCode == RESULT_OK) {
            Uri selectedPhoto = data.getData();
            Glide.with(this).load(selectedPhoto).into(iv_photo);
            // set text to image path
            tv_message.setText(selectedPhoto.toString());
            // add to list
            uriList.add(selectedPhoto);
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the file
                Toast.makeText(this, "Something went wrong. Could not create file.", Toast.LENGTH_SHORT).show();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.photoapp.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }


    private File createImageFile() throws IOException {
        //create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );

        // save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }
}
