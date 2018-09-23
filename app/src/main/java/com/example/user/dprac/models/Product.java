package com.example.user.dprac.models;

public class Product {
    String title,price,quantity,serial_no;

    public Product(){

    }

    public Product(String title,String serial_no,String price,String quantity){
        this.title = title;
        this.price = price;
        this.quantity = quantity;
        this.serial_no = serial_no;
    }

    public String getTitle(){
        return title;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public String getQuantity(){
        return quantity;

    }
    public void setQuantity(String quantity){
        this.quantity = quantity;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getSerial_no() {
        return serial_no;
    }

    public void setSerial_no(String serial_no) {
        this.serial_no = serial_no;
    }
}
