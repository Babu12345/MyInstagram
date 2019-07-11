package com.example.myinstagram.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.myinstagram.EndlessRecyclerViewScrollListener;
import com.example.myinstagram.PostsAdapter;
import com.example.myinstagram.R;
import com.example.myinstagram.model.Post;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView rvInstagram;

    private PostsAdapter postsAdapter;
    private ArrayList<Post> mPosts;
    private EndlessRecyclerViewScrollListener scrollListener;
    private SwipeRefreshLayout swipeContainer;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);

        rvInstagram = view.findViewById(R.id.rvInstagram);

        mPosts = new ArrayList<>();

        postsAdapter = new PostsAdapter(getContext(),mPosts);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvInstagram.setAdapter(postsAdapter);
        rvInstagram.setLayoutManager(linearLayoutManager);

        //create the adapter
        //create the data source

        //Loads the next twenty


        queryPosts();
        // Lookup the swipe container view
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                queryPosts();


            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);



        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {

            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                queryPosts();

            }
        };

        rvInstagram.addOnScrollListener(scrollListener);

    }


    private void queryPosts(){
        ParseQuery<Post> postQuery = new ParseQuery<Post>(Post.class);
        postQuery.include(Post.KEY_USER);
        postQuery.setLimit(20);
        postQuery.addDescendingOrder("createdAt");

        if (mPosts.size() > 0){
            postQuery.whereLessThan("createdAt", mPosts.get(mPosts.size() - 1).getCreatedAt());
        }



        postQuery.findInBackground(new FindCallback<Post>(){
            @Override
            public void done(List<Post> posts, ParseException e) {
                if (e!=null){
                    Log.e("HomeFragment", "Error with query");
                    e.printStackTrace();
                    return;

                }else{


                    for(int i = 0; i < posts.size();i++) {

                        Post post = posts.get(i);
                        mPosts.add(post);
                        postsAdapter.notifyItemInserted(mPosts.size() - 1);


                        Log.d("HomeFragment", "Posts: "+ post.getDescription() + ",username: " + post.getUser().getUsername());

                    }
                    swipeContainer.setRefreshing(false);
                }


            }
        });

    }

}
