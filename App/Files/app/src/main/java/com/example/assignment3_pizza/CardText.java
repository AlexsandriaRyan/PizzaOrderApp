package com.example.assignment3_pizza;

import android.graphics.drawable.Drawable;

public class CardText {

    // for new orders / cart
    private String name;
    private Drawable image;

    // for order history
    private int orderNumber;
    private String dateTime;
    private double price;

    public CardText() { }

    public CardText(String name, Drawable image) {
        this.name = name;
        this.image = image;
    }

    public CardText(int orderNumber, String dateTime, double price) {
        this.orderNumber = orderNumber;
        this.dateTime = dateTime;
        this.price = price;
    }

    public String getName()     { return name; }
    public Drawable getImage()  { return image; }
    public int getOrderNumber() { return orderNumber; }
    public String getDateTime() { return dateTime; }
    public double getPrice() { return price; }
}
