package com.example.instagram.Model;

public class Post {
    private  String username;
    private String Description;
    private String Uid;
    private String imageurl;
    private String postid;
    private String fullname;

    public Post(){}

    public Post(String username,String description, String uid, String imageurl, String postid,String fullname) {
        this.username = username;
        Description = description;
        Uid = uid;
        this.imageurl = imageurl;
        this.postid = postid;
        this.fullname= fullname;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String  getPostid() {
        return postid;
    }

    public void setPostid(String postid) {
        this.postid = postid;
    }
}
