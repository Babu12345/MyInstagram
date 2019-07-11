package com.example.myinstagram;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myinstagram.model.Comments;
import com.parse.ParseFile;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentListAdapter extends RecyclerView.Adapter<CommentListAdapter.ViewHolder> {


    Context context;
    List<Comments> mComments;
    public CommentListAdapter(List<Comments> comments){
        this.mComments = comments;

    }


    @NonNull
    @Override
    public CommentListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();

        LayoutInflater inflater = LayoutInflater.from(context);
        View postsView = inflater.inflate(R.layout.item_comment, parent, false);
        CommentListAdapter.ViewHolder viewHolder = new CommentListAdapter.ViewHolder(postsView);
        return viewHolder;
    }



    @Override
    public void onBindViewHolder(@NonNull CommentListAdapter.ViewHolder holder, int position) {
        Comments comment = mComments.get(position);
        Log.d("CommentsListAdapter", comment.getString("comment"));
        holder.bind(comment);
    }

    @Override
    public int getItemCount() {
        return mComments.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{


        public CircleImageView ivCommentProfile;
        public TextView        tvCommentHandler;
        public TextView        tvcComments;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivCommentProfile = itemView.findViewById(R.id.ivCommentProfile);
            tvCommentHandler = itemView.findViewById(R.id.tvCommentHandler);
            tvcComments = itemView.findViewById(R.id.tvcComments);
        }


        public void bind(Comments comments){



            ParseFile image = comments.getParseUser("user").getParseFile("profilePic");
            if (image != null){
                Glide.with(context).load(image.getUrl()).into(ivCommentProfile);
            }else{
                Glide.with(context).load(R.drawable.instagram_user_outline_24).into(ivCommentProfile);
            }

            tvCommentHandler.setText(comments.getParseUser("user").getUsername());

            tvcComments.setText(comments.getString("comment"));




        }
    }
}
