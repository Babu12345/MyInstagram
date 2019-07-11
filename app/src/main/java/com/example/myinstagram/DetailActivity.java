package com.example.myinstagram;

import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myinstagram.model.Post;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class DetailActivity extends AppCompatActivity {


    Post post;
    public ImageView ivMainProfile2;
    public TextView tvdescription2;
    public TextView tvUsername2;
    public TextView tvHandler2;
    public ImageButton ibLike2;
    public ImageButton ibComment2;
    public ImageButton ibShare2;
    public TextView tvTimestamp2;
    public TextView tvLiked2;
    public CircleImageView ivProfilePic2;
    public TextView tvComments2;
    public ImageButton ibCancel;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ivMainProfile2 = findViewById(R.id.ivMainProfile2);
        tvdescription2 = findViewById(R.id.tvdescription2);
        tvUsername2 = findViewById(R.id.tvUsername2);
        tvHandler2 = findViewById(R.id.tvHandler2);
        ibLike2 = findViewById(R.id.ibLike2);
        ibComment2 = findViewById(R.id.ibComment2);
        ibShare2 = findViewById(R.id.ibShare2);
        tvTimestamp2 = findViewById(R.id.tvTimestamp2);
        ivProfilePic2 = findViewById(R.id.ivProfilePic2);
        tvLiked2 = findViewById(R.id.tvLiked2);
        ibCancel = findViewById(R.id.ibCancel);

        tvComments2 = findViewById(R.id.tvComments);

        post = (Post)getIntent().getExtras().get("post");
//        Toast.makeText(getApplicationContext(), post.getUser().getUsername(),Toast.LENGTH_SHORT).show();


        tvdescription2.setText(post.getDescription());
        tvHandler2.setText(post.getUser().getUsername());
        tvUsername2.setText(post.getUser().getUsername());
        tvLiked2.setText(post.numLiked().toString() + " likes");

        DateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZZZ yyyy");

        //to convert Date to String, use format method of SimpleDateFormat class.
        String strDate = dateFormat.format(post.getCreatedAt());
        tvTimestamp2.setText(getRelativeTimeAgo(strDate));

        ibCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }


    public String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }
}
