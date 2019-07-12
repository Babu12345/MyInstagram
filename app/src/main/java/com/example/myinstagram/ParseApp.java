package com.example.myinstagram;

import android.app.Application;

import com.example.myinstagram.model.Comments;
import com.example.myinstagram.model.Post;
import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        ParseObject.registerSubclass(Post.class);
        ParseObject.registerSubclass(Comments.class);

        final Parse.Configuration configuration = new Parse.Configuration.Builder(this)
                .applicationId("bwanyeki12")
                .clientKey("Simba_1999")
                .server("http://babu12345-myinstagram.herokuapp.com/parse")
                .build();

        Parse.initialize(configuration);

    }
}
