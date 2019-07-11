package com.example.myinstagram;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;

import com.example.myinstagram.model.Post;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;
import java.io.IOException;

public class descriptionActivity extends AppCompatActivity {

    ImageView composeiv;
    Button submitbtn;
    EditText descriptionet;
    public MenuItem miActionProgressItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);

        composeiv = findViewById(R.id.compose_iv);
        submitbtn = findViewById(R.id.submit_btn);
        descriptionet = findViewById(R.id.description_et);

        File photoFile1 = (File)getIntent().getExtras().get("photofile");

        composeiv.setImageBitmap(rotateBitmapOrientation(photoFile1.getAbsolutePath()));

        submitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                String description = descriptionet.getText().toString();
                File photoFile = (File)getIntent().getExtras().get("photofile");
                ParseUser user = ParseUser.getCurrentUser();

                if (photoFile == null || composeiv.getDrawable() == null){
                    Log.e("descriptionActivity", "No photo to submit");
                    Toast.makeText(getApplicationContext(), "No photo to submit", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getApplicationContext(), "Submit Click Successful", Toast.LENGTH_SHORT).show();
                    showProgressBar();
                    savePost(description, user, photoFile);
                }
            }
        });
    }


    private void savePost(String description, ParseUser parseUser, File photoFile){
        Post post = new Post();
        post.setDescription(description);
        post.setUser(parseUser);
        post.setImage(new ParseFile(photoFile));
        post.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e!=null){
                    Toast.makeText(getApplicationContext(), "Saving error", Toast.LENGTH_SHORT).show();
                    Log.d("descriptionActivity","Error while saving");
                    e.printStackTrace();
                    hideProgressBar();
                    return;
                }else{
                    Toast.makeText(getApplicationContext(), "Saving success", Toast.LENGTH_SHORT).show();
                    Log.d("descriptionActivity", "saving success");
                    hideProgressBar();
                    finish();
                }
            }
        });

    }


    public Bitmap rotateBitmapOrientation(String photoFilePath) {
        // Create and configure BitmapFactory
        BitmapFactory.Options bounds = new BitmapFactory.Options();
        bounds.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(photoFilePath, bounds);
        BitmapFactory.Options opts = new BitmapFactory.Options();
        Bitmap bm = BitmapFactory.decodeFile(photoFilePath, opts);
        // Read EXIF Data
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(photoFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String orientString = exif.getAttribute(ExifInterface.TAG_ORIENTATION);
        int orientation = orientString != null ? Integer.parseInt(orientString) : ExifInterface.ORIENTATION_NORMAL;
        int rotationAngle = 0;
        if (orientation == ExifInterface.ORIENTATION_ROTATE_90) rotationAngle = 90;
        if (orientation == ExifInterface.ORIENTATION_ROTATE_180) rotationAngle = 180;
        if (orientation == ExifInterface.ORIENTATION_ROTATE_270) rotationAngle = 270;
        // Rotate Bitmap
        Matrix matrix = new Matrix();
        matrix.setRotate(rotationAngle, (float) bm.getWidth() / 2, (float) bm.getHeight() / 2);
        Bitmap rotatedBitmap = Bitmap.createBitmap(bm, 0, 0, bounds.outWidth, bounds.outHeight, matrix, true);
        // Return result
        return rotatedBitmap;
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // Store instance of the menu item containing progress
        miActionProgressItem = menu.findItem(R.id.miActionProgress);
        // Extract the action-view from the menu item
        ProgressBar v =  (ProgressBar) MenuItemCompat.getActionView(miActionProgressItem);
        // Return to finish
        return super.onPrepareOptionsMenu(menu);

    }

    public void showProgressBar() {
        // Show progress item

        miActionProgressItem.setVisible(true);
    }

    public void hideProgressBar() {
        // Hide progress item
        miActionProgressItem.setVisible(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

}
