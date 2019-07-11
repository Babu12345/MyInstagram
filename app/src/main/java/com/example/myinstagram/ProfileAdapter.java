package com.example.myinstagram;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myinstagram.model.Post;
import com.parse.ParseFile;

import java.util.List;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ViewHolder> {


    Context context;
    List<Post> mPosts;
    public ProfileAdapter(Context context,List<Post> posts){
        this.mPosts = posts;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        context = parent.getContext();

        LayoutInflater inflater = LayoutInflater.from(context);
        View postsView = inflater.inflate(R.layout.item_instagram_profile, parent, false);
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

    public class ViewHolder extends RecyclerView.ViewHolder{


        public ImageView ivProfilePosts;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivProfilePosts = itemView.findViewById(R.id.ivProfilePosts);
        }

        public void bind(Post post){



            ParseFile image = post.getImage();
            if (image != null){
                Glide.with(context).load(image.getUrl()).into(ivProfilePosts);
            }






        }
    }
}
