package com.example.myinstagram;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myinstagram.model.Comments;
import com.example.myinstagram.model.Post;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentsList extends AppCompatActivity {

    CommentListAdapter commentAdapter;
    ArrayList<Comments> comments;
    RecyclerView rvComments;

    CircleImageView ivMainCommentProfile;
    EditText etCommentAct;
    Button btnPostSubmit;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_comments);


        // find the RecyclerView
        rvComments = (RecyclerView) findViewById(R.id.rvComments);
        etCommentAct = findViewById(R.id.etCommentAct);
        btnPostSubmit = findViewById(R.id.btnPostSubmit);

        ivMainCommentProfile = findViewById(R.id.ivMainCommentProfile);
        // init the arraylist (data source)

        comments = new ArrayList<>();
        // construct the adapter from the data source

        commentAdapter = new CommentListAdapter(comments);

        ParseFile image = ParseUser.getCurrentUser().getParseFile("profilePic");

        if(image!=null){
            Glide.with(this)
                    .load(image.getUrl())
                    .into(ivMainCommentProfile);
        }else{
            Glide.with(this)
                    .load(getResources().getDrawable(R.drawable.instagram_user_outline_24))
                    .into(ivMainCommentProfile);
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        rvComments.setLayoutManager(linearLayoutManager);



        //set the adapter
        rvComments.setAdapter(commentAdapter);



        // Have an intent to send the post data

        queryComments(getIntent().getStringExtra("postId"));


        btnPostSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // First Create the comment

                Comments comments = new Comments();
                comments.putComment(etCommentAct.getText().toString(), ParseUser.getCurrentUser(), getIntent().getStringExtra("postId"));
                comments.saveInBackground();



                //Later add the comment into the list in the Post class database



                comments.fetchInBackground(new GetCallback<Comments>() {
                    @Override
                    public void done(Comments comments, com.parse.ParseException e) {
                        Toast.makeText(getApplicationContext(), "Clicked Post", Toast.LENGTH_SHORT).show();

                        //Later add the comment into the list in the Post class database

                        List <Comments> commentsList = ((Post)getIntent().getExtras().get("post")).getList("commentList");

                        if (commentsList ==null){
                            commentsList = new ArrayList<Comments>();
                        }

                        commentsList.add(comments);


                        ((Post)getIntent().getExtras().get("post")).put("commentList", commentsList);

                        ((Post)getIntent().getExtras().get("post")).saveInBackground();
                    }
                });
            }
        });

    }


    // Now make a function to add stuff to the comments timeline


    private void queryComments(String postId){
        ParseQuery<Comments> postQuery = new ParseQuery<Comments>(Comments.class);
        postQuery.include(Post.KEY_USER);
        postQuery.setLimit(20);
        postQuery.whereEqualTo("postId", postId);
        postQuery.addDescendingOrder("createdAt");



        if (comments.size() > 0){
            postQuery.whereLessThan("createdAt", comments.get(comments.size() - 1).getCreatedAt());
        }



        postQuery.findInBackground(new FindCallback<Comments>(){
            @Override
            public void done(List<Comments> posts, ParseException e) {
                if (e!=null){
                    Log.d("CommentsList", "Error with query");
                    e.printStackTrace();
                    return;

                }else{

                    Log.d("CommentsList", String.format("%d", posts.size()));
                    for(int i = 0; i < posts.size();i++) {

                        Comments post = posts.get(i);
                        comments.add(post);
                        commentAdapter.notifyItemInserted(comments.size() - 1);


                        Log.d("CommentsList", "Posts: "+ post.getString("comment") + ",username: " + post.getParseUser("user").getUsername());

                    }

                }


            }
        });

    }



}
