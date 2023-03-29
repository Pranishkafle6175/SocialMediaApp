package com.example.instagram.Model;

public class Comment {
    private String Username;
    private String Uid;
    private String commentDescription;
    private String postid;
    private String Imageurl;

    public Comment() {
    }

    public Comment(String username, String uid, String commentDescription, String postid, String imageurl) {
        Username = username;
        Uid = uid;
        this.commentDescription = commentDescription;
        this.postid = postid;
        Imageurl = imageurl;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    public String getCommentDescription() {
        return commentDescription;
    }

    public void setCommentDescription(String commentDescription) {
        this.commentDescription = commentDescription;
    }

    public String getPostid() {
        return postid;
    }

    public void setPostid(String postid) {
        this.postid = postid;
    }

    public String getImageurl() {
        return Imageurl;
    }

    public void setImageurl(String imageurl) {
        Imageurl = imageurl;
    }
}


