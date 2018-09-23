package com.example.user.dprac.models;

public class OrderHistory {

    int image;
    String order_no,name,date,time;

    public OrderHistory(){

    }
    public OrderHistory(int image, String order_no, String name, String date, String time) {

        this.image = image;
        this.order_no = order_no;
        this.name = name;
        this.date = date;
        this.time = time;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setData(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
