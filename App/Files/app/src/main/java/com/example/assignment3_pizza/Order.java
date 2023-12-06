package com.example.assignment3_pizza;

import android.content.Context;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.sql.Timestamp;

public class Order {

    private String customerName;
    private String customerPhone;
    private String customerEmail;
    private String customerAddress;
    private String orderNotes;
    private int orderNumber;
    private double orderPrice;
    private Date orderTime;
    private List<Pizza> pizzas;

    public Order() {
        customerName = "";
        customerPhone = "";
        customerEmail = "";
        customerAddress = "";
        orderNotes = "";
        orderNumber = 0;
        orderPrice = 0;
        pizzas = new ArrayList<>();
    }

    public Order(String customerName, String customerPhone, String customerEmail, String customerAddress, String orderNotes, int orderNumber, List<Pizza> pizzas) {
        this.customerName = customerName;
        this.customerPhone = customerPhone;
        this.customerEmail = customerEmail;
        this.customerAddress = customerAddress;
        this.orderNotes = orderNotes;
        this.orderNumber = orderNumber;
        this.pizzas = pizzas;
    }

    public String getCustomerName() { return customerName; }

    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getCustomerPhone() { return customerPhone; }

    public void setCustomerPhone(String customerPhone) { this.customerPhone = customerPhone; }

    public String getCustomerEmail() { return customerEmail; }

    public void setCustomerEmail(String customerEmail) { this.customerEmail = customerEmail; }

    public String getCustomerAddress() { return customerAddress; }

    public void setCustomerAddress(String customerAddress) { this.customerAddress = customerAddress; }

    public String getOrderNotes() { return orderNotes; }

    public void setOrderNotes(String orderNotes) { this.orderNotes = orderNotes; }

    public int getOrderNumber() { return orderNumber; }

    public void setOrderNumber(int orderNumber) { this.orderNumber = orderNumber; }

    public String getOrderTime() {
        SimpleDateFormat df = new SimpleDateFormat("MMM dd yyyy\nhh:mm aa");
        return df.format(orderTime);
    }

    public void setOrderTime() {
        Date date = new Date();
        this.orderTime = new Timestamp(date.getTime());
    }

    public void setOrderTime(String time) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat("MMM dd yyyy\nhh:mm aa");
        this.orderTime = df.parse(time);
    }

    public void setOrderPrice() {
        double total = 0;
        for(Pizza pizza : pizzas) {
            total += pizza.getPrice();
        }

        this.orderPrice = total;
    }

    public void setOrderPrice(double total) {
        this.orderPrice = total;
    }

    public double getOrderPrice() { return orderPrice; }

    public void addPizzas(Pizza pizza) { pizzas.add(pizza); }

    public List<Pizza> getPizzas() { return pizzas; }

    public String displayOrder(Context context) {
        String display = "";

        // Adding ordger information
        display += context.getString(R.string.order) + String.format("#%04d", orderNumber) + "\n";

        for(Pizza pizza : pizzas) {
            display += pizza.getSizeStr() + "\n";

            for (String topping : pizza.getToppings()) {
                display += topping + "\n";
            }

            display += pizza.getPrice() + "\n";
        }

        display += context.getString(R.string.total) + ": " + getOrderPrice() + "\n";
        display += "\n";

        // Adding customer information
        display += context.getString(R.string.customer_information) + "\n";
        display += context.getString(R.string.name) + ": " + getCustomerName() + "\n";
        display += context.getString(R.string.phone) + ": " + getCustomerPhone() + "\n";
        display += context.getString(R.string.email) + ": " + getCustomerEmail() + "\n";
        display += context.getString(R.string.address) + ": " + getCustomerAddress() + "\n";
        display += context.getString(R.string.notes) + ": " + getOrderNotes() + "\n";

        return display;
    }

    public String customerInformation(Context context) {
        String display = "";
        // Adding customer information
        display += context.getString(R.string.customer_information) + "\n";
        display += context.getString(R.string.name) + ": " + getCustomerName() + "\n";
        display += context.getString(R.string.phone) + ": " + getCustomerPhone() + "\n";
        display += context.getString(R.string.email) + ": " + getCustomerEmail() + "\n";
        display += context.getString(R.string.address) + ": " + getCustomerAddress() + "\n";
        display += context.getString(R.string.notes) + ": " + getOrderNotes() + "\n";

        return display;
    }
}