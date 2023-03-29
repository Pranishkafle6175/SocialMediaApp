package com.example.instagram.Model;

public class Notification {

    private String userid;
    private String postid;
//    private boolean ispost;
    private String text;


    public Notification() {
    }

    public Notification(String userid, String postid, String text) {
        this.userid = userid;
        this.postid = postid;
//        this.ispost = ispost;
        this.text = text;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getPostid() {
        return postid;
    }

    public void setPostid(String postid) {
        this.postid = postid;
    }

//    public boolean isIspost() {
//        return ispost;
//    }
//
//    public void setIspost(boolean ispost) {
//        this.ispost = ispost;
//    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
