package com.example.myinstagram.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;


@ParseClassName("Comments")
public class Comments extends ParseObject{


    public void putComment(String comment, ParseUser user, String postId){

        put("comment", comment);
        put("user", user);
        put("postId", postId);

    }

}
