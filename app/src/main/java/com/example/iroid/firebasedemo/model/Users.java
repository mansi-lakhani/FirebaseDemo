package com.example.iroid.firebasedemo.model;

public class Users {
    public String name,email,phonenumber,imageUrl,userId;

    public Users(){

    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Users(String name, String email, String phonenumber, String imageUrl,String userId) {
        this.name = name;
        this.email = email;
        this.phonenumber = phonenumber;
        this.imageUrl = imageUrl;
        this.userId = userId;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getName() {
            return name;

    }

    public String getEmail() {
        return email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
