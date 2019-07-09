package com.example.myinstagram;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myinstagram.fragments.BitmapScaler;
import com.example.myinstagram.model.Post;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.parceler.Parcels;

import java.io.File;

public class descriptionActivity extends AppCompatActivity {

    ImageView composeiv;
    Button submitbtn;
    EditText descriptionet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);

        composeiv = findViewById(R.id.compose_iv);
        submitbtn = findViewById(R.id.submit_btn);
        descriptionet = findViewById(R.id.description_et);


        Bitmap bitmap =(Bitmap) Parcels.unwrap(getIntent().getParcelableExtra("image"));
        Bitmap resizedBitmap = BitmapScaler.scaleToFitWidth(bitmap, 600);
        composeiv.setImageBitmap(resizedBitmap);

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
                    return;
                }else{
                    Toast.makeText(getApplicationContext(), "Saving success", Toast.LENGTH_SHORT).show();
                    Log.d("descriptionActivity", "saving success");
                    finish();
                }
            }
        });

    }



}
