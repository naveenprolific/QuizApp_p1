package com.example.quizapp;

public class Blog {
    private String Description,Title,image_url,User_id,Date_posted;
    public Blog(){

    }

    public Blog(String description, String title, String image_url, String user_id, String date_posted) {
        Description = description;
        Title = title;
        this.image_url = image_url;
        User_id = user_id;
        Date_posted = date_posted;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getUser_id() {
        return User_id;
    }

    public void setUser_id(String user_id) {
        User_id = user_id;
    }

    public String getDate_posted() {
        return Date_posted;
    }

    public void setDate_posted(String date_posted) {
        Date_posted = date_posted;
    }
}
