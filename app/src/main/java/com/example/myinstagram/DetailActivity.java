package com.example.myinstagram;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.myinstagram.model.Comments;
import com.example.myinstagram.model.Post;
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
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

        tvComments2 = findViewById(R.id.tvComments2);

        post = (Post)getIntent().getExtras().get("post");




        tvdescription2.setText(post.getDescription());
        tvHandler2.setText(getIntent().getStringExtra("username"));
        tvUsername2.setText(getIntent().getStringExtra("username"));
        tvLiked2.setText(post.numLiked().toString() + " likes");

        DateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZZZ yyyy");

        //to convert Date to String, use format method of SimpleDateFormat class.
        String strDate = dateFormat.format(post.getCreatedAt());
        tvTimestamp2.setText(getRelativeTimeAgo(strDate));

        ParseFile image = ParseUser.getCurrentUser().getParseFile("profilePic");
        if (image != null){
            Glide.with(getApplicationContext()).load(image.getUrl()).into(ivProfilePic2);
        }else{
            Glide.with(getApplicationContext()).load(getResources().getDrawable(R.drawable.instagram_user_outline_24)).into(ivProfilePic2);
        }



        ParseFile image3 = post.getImage();

        if(image3!=null){
            Glide.with(getApplicationContext())
                    .load(image3.getUrl())
                    .into(ivMainProfile2);
        }else{
            Glide.with(getApplicationContext())
                    .load(getResources().getDrawable(R.drawable.instagram_user_outline_24))
                    .into(ivMainProfile2);
        }


        // Get number of commented posts
        List<Comments> commentArray = post.getList("commentList");
        Integer commentNum = 0;
        if (commentArray == null){
            commentNum = 0;
        }else{
            commentNum = commentArray.size();

        }


        tvComments2.setText("View all "+commentNum.toString()+" comments");
        tvComments2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO - have an intent to do to the Comments List
                //If there is a click on this then go to a different activity with a recycler view of all the comments
                Intent intent = new Intent(getApplicationContext(), CommentsList.class);
                intent.putExtra("postId", post.getObjectId());
                intent.putExtra("post", post);
                startActivity(intent);
            }
        });

        ibComment2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //If there is a click on this then go to a different activity with a recycler view of all the comments

                Intent intent = new Intent(getApplicationContext(), CommentsList.class);
                intent.putExtra("postId", post.getObjectId());
                intent.putExtra("post", post);
                startActivity(intent);
            }
        });


        //Like logic

        Boolean isLiked = false;

        List <ParseUser> users1 = post.getList("likedBy");

        if (users1!=null) {
            for (int i = 0; i < users1.size(); i++) {



                if (ParseUser.getCurrentUser().getObjectId().equals(users1.get(i).getObjectId()) ){

                    isLiked = true;
                    break;
                }
            }
        }else{


            post.put("likedBy", new ArrayList<ParseUser>());
        }



        if (isLiked && (users1 != null)){
            ibLike2.setImageResource(R.drawable.ufi_heart_active);
            ibLike2.setColorFilter(Color.RED);
            post.setLike(Boolean.TRUE);



        }else if (!isLiked && (users1 !=null)){
            ibLike2.setImageResource(R.drawable.ufi_heart);
            ibLike2.setColorFilter(Color.BLACK);
            post.setLike(Boolean.FALSE);

        }

        post.saveInBackground();

        ibLike2.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
                Boolean isLiked1 = false;

                List <ParseUser> users = new ArrayList<ParseUser>();
                users.clear();


                users = post.getList("likedBy");



                if (users!=null) {
                    for (int i = 0; i < users.size(); i++) {

                        if (ParseUser.getCurrentUser().getObjectId().equals(users.get(i).getObjectId()) ){
                            isLiked1 = true;

                        }

//
                    }
                }
                else{

//                    users = new ArrayList<ParseUser>();
                    post.put("likedBy", new ArrayList<ParseUser>());
                }


                Integer newLiked;
                if (!isLiked1 && (users!=null)){
                    ibLike2.setImageResource(R.drawable.ufi_heart_active);
                    ibLike2.setColorFilter(Color.RED);
                    post.setLike(Boolean.TRUE);


                    users.add(ParseUser.getCurrentUser());



                    newLiked =post.numLiked() + 1;
                    post.setNumLiked(newLiked);

                    tvLiked2.setText(newLiked.toString() + " likes");

                    post.put("likedBy", users);

                }else if (isLiked1 &&users!=null){
                    ibLike2.setImageResource(R.drawable.ufi_heart);
                    ibLike2.setColorFilter(Color.BLACK);
                    post.setLike(Boolean.FALSE);


                    int index = 0;

                    for (int i = 0; i < users.size(); i++) {

                        if (ParseUser.getCurrentUser().getObjectId().equals(users.get(i).getObjectId()) ){
//
                            index = i;
                        }


                    }

                    users.remove(index);
                    Log.d("PostsAdapter", String.format("%d",users.size()));




                    newLiked =post.numLiked() - 1;
                    post.setNumLiked(newLiked);

                    tvLiked2.setText(newLiked.toString() + " likes");

                    post.put("likedBy", users);

                }


                try {
                    post.save();
                } catch (com.parse.ParseException e) {
                    e.printStackTrace();
                }


            }
        });

        ibCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    post.save();
                } catch (com.parse.ParseException e) {
                    e.printStackTrace();
                }
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
