package com.example.myinstagram;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.myinstagram.model.Post;
import com.parse.ParseFile;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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




//        File photoFile1 = post.getImage().;
//        holder.ivMainProfile.setImageBitmap();
    }

    @Override
    public int getItemCount() {
        return mPosts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivMainProfile;
        public TextView tvdescription;
        public TextView tvUsername;
        public TextView tvHandler;
        public ImageButton ibLike;
        public ImageButton ibComment;
        public ImageButton ibShare;
        public TextView tvTimestamp;
        public CircleImageView ivProfilePic;


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
//        tvTimestamp.setText(DateFormat.getDateInstance().format(post.getPostTime()));
        DateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZZZ yyyy");

        //to convert Date to String, use format method of SimpleDateFormat class.
        String strDate = dateFormat.format(post.getCreatedAt());
        tvTimestamp.setText(getRelativeTimeAgo(strDate));


        ParseFile image = post.getImage();
        if (image != null){
            Glide.with(context).load(image.getUrl()).into(ivMainProfile);
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

        if (post.isLiked()){
            ibLike.setImageResource(R.drawable.ufi_heart_active);
            ibLike.setColorFilter(Color.RED);
            post.setLike(Boolean.TRUE);

        }else{
            ibLike.setImageResource(R.drawable.ufi_heart);
            ibLike.setColorFilter(Color.BLACK);
            post.setLike(Boolean.FALSE);

        }


        ibLike.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {


                if (!post.isLiked()){
                    ibLike.setImageResource(R.drawable.ufi_heart_active);
                    ibLike.setColorFilter(Color.RED);
                    post.setLike(Boolean.TRUE);

                }else{
                    ibLike.setImageResource(R.drawable.ufi_heart);
                    ibLike.setColorFilter(Color.BLACK);
                    post.setLike(Boolean.FALSE);

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





    }
}

}
