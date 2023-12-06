package com.example.assignment3_pizza;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

import java.io.Serializable;
import java.util.*;

public class Pizza {

    Context context;
    private int size;
    private double price;
    private String inches;
    private List<String> toppings;

    public int totalQuantity = 0;
    public final int max = 3;
    public boolean sizeSelect = false;

    public Pizza() {
        toppings = new ArrayList<>();
    }

    public Pizza(int size) {
        this.size = size;
        this.inches = convertInches(this.size);
        this.price = convertPrice(this.size);
        toppings = new ArrayList<>();
    }

    public Pizza (int size, List<String> toppings) {
        this.size = size;
        this.inches = convertInches(this.size);
        this.price = convertPrice(this.size);
        this.toppings = toppings;
    }

    public int getSize() { return size; }

    public String getSizeStr() {
        switch (size) {
            case 1:
                return context.getString(R.string.small);
            case 2:
                return context.getString(R.string.medium);
            case 3:
                return context.getString(R.string.large);
            case 4:
                return context.getString(R.string.xlarge);
            default:
                return "unknown";
        }
    }

    public String getInches() { return inches; }

    public void setSize(int size) {
        this.size = size;
        this.price = convertPrice(size);
    }

    public void setSize(String size) {
        this.size = convertSize(size);
        this.price = convertPrice(this.size);
    }

    public double getPrice() { return price; }

    public void setPrice(double price) { this.price = price; }

    public List<String> getToppings() { return toppings; }

    public void addToppings(String top) { toppings.add(top); }

    public void removeToppings(String top) { toppings.remove(top); }

    public String displayPizza() {
        String display = getSizeStr() + "\n";
        Collections.sort(toppings);

        for (String top : toppings) {
            display += top + "\n";
        }

        display += "\n" + context.getString(R.string.subtotal) + ": $" + getPrice();

        return display;
    }

    private int convertSize(String size) {
        if (size.equals(context.getString(R.string.small))) {
            return 1;

        } else if (size.equals(context.getString(R.string.medium))) {
            return 2;

        } else if (size.equals(context.getString(R.string.large))) {
            return 3;

        } else if (size.equals(context.getString(R.string.xlarge))) {
            return 4;

        } else {
            return 0;
        }
    }

    private String convertInches(int size) {
        switch (size) {
            case 1:
                return "12\"";
            case 2:
                return "14\"";
            case 3:
                return "16\"";
            case 4:
                return "18\"";
            default:
                return "0\"";
        }
    }

    private double convertPrice(int size) {
        switch (size) {
            case 1:
                return 12.99;
            case 2:
                return 14.99;
            case 3:
                return 16.99;
            case 4:
                return 18.99;
            default:
                return 0.00;
        }
    }
}