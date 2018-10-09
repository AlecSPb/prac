package com.example.user.dprac.models;

public class NavbarList {


    String title = "";
    int image;

    public NavbarList(String title, int image) {
        this.title = title;
        this.image =image;
    }

    public String getTitle() {
        return title;
    }

    public int getImage() {
        return image;
    }


}
