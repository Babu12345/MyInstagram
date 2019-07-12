package com.example.myinstagram;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myinstagram.fragments.ProfileFragment;
import com.example.myinstagram.model.Comments;
import com.example.myinstagram.model.Post;
import com.parse.GetCallback;
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder>{

    Context context;
    List<Post> mPosts;
    public PostsAdapter(Context context,List<Post> posts){
        this.mPosts = posts;
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        context = parent.getContext();

        LayoutInflater inflater = LayoutInflater.from(context);
        View postsView = inflater.inflate(R.layout.item_instagram, parent, false);
        ViewHolder viewHolder = new ViewHolder(postsView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Post post = mPosts.get(position);
        holder.bind(post);

    }

    @Override
    public int getItemCount() {
        return mPosts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public ImageView ivMainProfile;
        public TextView tvdescription;
        public TextView tvUsername;
        public TextView tvHandler;
        public ImageButton ibLike;
        public ImageButton ibComment;
        public ImageButton ibShare;
        public TextView tvTimestamp;
        public TextView tvLiked;
        public CircleImageView ivProfilePic;
        public CircleImageView ivProfileComments;
        public Button btnPost;
        public EditText etComment;
        public TextView tvComments;

        public ViewHolder(@NonNull View itemView) {
        super(itemView);

        ivMainProfile = itemView.findViewById(R.id.ivMainProfile);
        tvdescription = itemView.findViewById(R.id.tvdescription);
        tvUsername = itemView.findViewById(R.id.tvUsername);
        tvHandler = itemView.findViewById(R.id.tvHandler);
        ibLike = itemView.findViewById(R.id.ibLike);
        ibComment = itemView.findViewById(R.id.ibComment);
        ibShare = itemView.findViewById(R.id.ibShare);
        tvTimestamp = itemView.findViewById(R.id.tvTimestamp);
        ivProfilePic = itemView.findViewById(R.id.ivProfilePic);
        tvLiked = itemView.findViewById(R.id.tvLiked);
        ivProfileComments = itemView.findViewById(R.id.ivProfileComments);
        btnPost = itemView.findViewById(R.id.btnPostSubmit);
        etComment = itemView.findViewById(R.id.etComment);
        tvComments = itemView.findViewById(R.id.tvComments);


            itemView.setOnClickListener(this);
    }

    // getRelativeTimeAgo("Mon Apr 01 21:16:23 +0000 2014");
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
    public void bind(final Post post){

        tvdescription.setText(post.getDescription());
        tvHandler.setText(post.getUser().getUsername());
        tvUsername.setText(post.getUser().getUsername());
        tvLiked.setText(post.numLiked().toString() + " likes");
//        tvTimestamp.setText(DateFormat.getDateInstance().format(post.getPostTime()));
        DateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZZZ yyyy");

        //to convert Date to String, use format method of SimpleDateFormat class.
        String strDate = dateFormat.format(post.getCreatedAt());
        tvTimestamp.setText(getRelativeTimeAgo(strDate));


        ParseFile image = post.getImage();
        if (image != null){
            Glide.with(context).load(image.getUrl()).into(ivMainProfile);
        }else{
            Glide.with(context).load(context.getResources().getDrawable(R.drawable.instagram_user_outline_24)).into(ivMainProfile);
        }

        ParseFile image2 = post.getUser().getParseFile("profilePic");
        if (image2 != null){

            Glide.with(context)
                    .load(image2.getUrl())
                    .into(ivProfilePic);



        }
        else{
            Glide.with(context)
                    .load(context.getResources().getDrawable(R.drawable.instagram_user_outline_24))
                    .into(ivProfilePic);


        }


        ParseFile image3 = ParseUser.getCurrentUser().getParseFile("profilePic");

        if(image3!=null){
            Glide.with(context)
                    .load(image3.getUrl())
                    .into(ivProfileComments);
        }else{
            Glide.with(context)
                    .load(context.getResources().getDrawable(R.drawable.instagram_user_outline_24))
                    .into(ivProfileComments);
        }

        // Get number of commented posts
        List <Comments> commentArray = post.getList("commentList");
        Integer commentNum = 0;
        if (commentArray == null){
            commentNum = 0;
        }else{
            commentNum = commentArray.size();

        }


        tvComments.setText("View all "+commentNum.toString()+" comments");
        tvComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO - have an intent to do to the Comments List
                //If there is a click on this then go to a different activity with a recycler view of all the comments
                Intent intent = new Intent(context, CommentsList.class);
                intent.putExtra("postId", post.getObjectId());
                intent.putExtra("post", post);
                context.startActivity(intent);
            }
        });

        ibComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //If there is a click on this then go to a different activity with a recycler view of all the comments

                Toast.makeText(context,"Clicked Comment", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, CommentsList.class);
                intent.putExtra("postId", post.getObjectId());
                intent.putExtra("post", post);
                context.startActivity(intent);
            }
        });


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
            ibLike.setImageResource(R.drawable.ufi_heart_active);
            ibLike.setColorFilter(Color.RED);
            post.setLike(Boolean.TRUE);



        }else if (!isLiked && (users1 !=null)){
            ibLike.setImageResource(R.drawable.ufi_heart);
            ibLike.setColorFilter(Color.BLACK);
            post.setLike(Boolean.FALSE);

        }

        post.saveInBackground();

        ibLike.setOnClickListener(new View.OnClickListener() {
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
                    ibLike.setImageResource(R.drawable.ufi_heart_active);
                    ibLike.setColorFilter(Color.RED);
                    post.setLike(Boolean.TRUE);


                    users.add(ParseUser.getCurrentUser());



                    newLiked =post.numLiked() + 1;
                    post.setNumLiked(newLiked);

                    tvLiked.setText(newLiked.toString() + " likes");

                    post.put("likedBy", users);

                }else if (isLiked1 &&users!=null){
                    ibLike.setImageResource(R.drawable.ufi_heart);
                    ibLike.setColorFilter(Color.BLACK);
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

                    tvLiked.setText(newLiked.toString() + " likes");

                    post.put("likedBy", users);

                }




                post.saveInBackground();



            }
        });


        tvUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(context,"Username Click", Toast.LENGTH_SHORT).show();

                FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                Fragment fragment = new ProfileFragment(post.getUser());
                fragmentManager.beginTransaction().replace(R.id.flContainer,fragment).commit();





            }
        });

        ivProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(context,"Profile Click", Toast.LENGTH_SHORT).show();

                FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                Fragment fragment = new ProfileFragment(post.getUser());
                fragmentManager.beginTransaction().replace(R.id.flContainer,fragment).commit();





            }
        });

        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // First Create the comment

                Comments comments = new Comments();
                comments.putComment(etComment.getText().toString(), ParseUser.getCurrentUser(), post.getObjectId());
                comments.saveInBackground();



                //Later add the comment into the list in the Post class database



                comments.fetchInBackground(new GetCallback<Comments>() {
                    @Override
                    public void done(Comments comments, com.parse.ParseException e) {
                        Toast.makeText(context, "Clicked Post", Toast.LENGTH_SHORT).show();

                        //Later add the comment into the list in the Post class database

                        List <Comments> commentsList = post.getList("commentList");

                        if (commentsList ==null){
                            commentsList = new ArrayList<Comments>();
                        }

                        commentsList.add(comments);


                        post.put("commentList", commentsList);

                        post.saveInBackground();


                        Integer commentNum = 0;
                        commentNum = commentsList.size();


                        tvComments.setText("View all "+commentNum.toString()+" comments");
                    }
                });











            }
        });







    }

        @Override
        public void onClick(View view) {

        }
    }

}
