package com.example.instagram.Model;

public class User {

    private String Id;
    private String Name;
    private String Username;
    private String email;
    private String image;

    public User() {
    }

    public User(String id, String name, String username, String email,String image) {
        this.Id = id;
        this.Name = name;
        this.Username = username;
        this.email = email;
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        this.Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        this.Username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
