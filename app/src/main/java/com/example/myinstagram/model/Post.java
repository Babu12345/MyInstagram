package com.example.myinstagram.model;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.Date;
import java.util.List;


@ParseClassName("Post")
public class Post extends ParseObject {

    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_IMAGE = "image";
    public static final String KEY_USER = "user";


    public String getDescription(){

        return getString(KEY_DESCRIPTION);
    }

    public void setDescription(String description){
        put(KEY_DESCRIPTION, description);
    }

    public ParseFile getImage(){

        return getParseFile(KEY_IMAGE);
    }

    public Integer numLiked(){
        return getInt("NumLiked");
    }

    public void setNumLiked(Integer num){
        put("NumLiked", num);
    }

    public List<ParseUser> likedBy(){

        return getList("likedBy");

    }




    public void setImage(ParseFile image){
        put(KEY_IMAGE, image);


    }

    public Date getPostTime(){

        return getDate("createdAt");
    }

    public ParseUser getUser(){
        return getParseUser(KEY_USER);
    }

    public Boolean isLiked(){


        return getBoolean("Like");

    }

    public void setLike(Boolean likeStatus){

        put("Like", likeStatus);
    }


    public void setUser(ParseUser user){
        put(KEY_USER,user);
    }



}
