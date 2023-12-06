package com.example.assignment3_pizza;

import android.content.Context;
import android.database.Cursor;
import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class AllOrders implements Serializable {

    List<Order> allOrders;
    Database database;
    public static ArrayList<String> order_id_order;
    public ArrayList<String> order_date, customer_info, order_price, pizza_id, order_id_pizza, pizza_size, topping_1, topping_2, topping_3;

    public AllOrders(Context context) throws ParseException {
        allOrders = new ArrayList<>();

        // Read from database
        database = new Database(context);
        order_id_order = new ArrayList<>();
        order_date = new ArrayList<>();
        customer_info = new ArrayList<>();
        order_price = new ArrayList<>();
        pizza_id = new ArrayList<>();
        order_id_pizza = new ArrayList<>();
        pizza_size = new ArrayList<>();
        topping_1 = new ArrayList<>();
        topping_2 = new ArrayList<>();
        topping_3 = new ArrayList<>();

        setOrderArrays();
        setPizzaArrays();

        // populate allOrders with database information
        for(int i = 0; i < order_id_order.size(); i++) {
            Order tempOrder = new Order();
            tempOrder.setOrderNumber(Integer.parseInt(order_id_order.get(i)));
            tempOrder.setOrderPrice(Double.parseDouble(order_price.get(i)));
            tempOrder.setOrderTime(order_date.get(i));

            // separate client information:
            String customerName = "";
            String customerPhone = "";
            String customerEmail = "";
            String customerAddress = "";
            String orderNotes = "";

            String customerInformation = customer_info.get(i);
            String temp = customerInformation.substring(customerInformation.indexOf("Name:") + 1);
            temp.trim();

            String[] split = temp.split("Phone:");
            customerName = split[0].trim().replace(" ", "").replace("\n", "").replace("ame:", "");
            temp = split[1];

            split = temp.split("Email:");
            customerPhone = split[0].trim().replace(" ", "").replace("\n", "");
            temp = split[1];

            split = temp.split("Address:");
            customerEmail= split[0].trim().replace(" ", "").replace("\n", "");
            temp = split[1];

            split = temp.split("Notes:");
            customerAddress= split[0].trim().replace(" ", "").replace("\n", "");
            orderNotes = split[1];

            tempOrder.setCustomerName(customerName);
            tempOrder.setCustomerPhone(customerPhone);
            tempOrder.setCustomerEmail(customerEmail);
            tempOrder.setCustomerAddress(customerAddress);
            tempOrder.setOrderNotes(orderNotes);

            // add pizzas
            allOrders.add(tempOrder);

            for(int j = 0; j < pizza_id.size(); j++) {
                if (order_id_pizza.get(j) == order_id_order.get(i)) {
                    Pizza tempPizza = new Pizza();
                    tempPizza.setSize(pizza_size.get(j));

                    if (topping_1.get(j)!=null) { tempPizza.addToppings(topping_1.get(j)); }
                    if (topping_2.get(j)!=null) {tempPizza.addToppings(topping_2.get(j)); }
                    if (topping_3.get(j)!=null) {tempPizza.addToppings(topping_3.get(j)); }

                    tempOrder.addPizzas(tempPizza);
                }
            }
        }
    }

    public void addOrder(Order order) {
        allOrders.add(order);
    }

    public List<Order> getAllOrders() { return allOrders; }

    void setOrderArrays() {
        Cursor cursor = database.readAllOrders();
        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                order_id_order.add(cursor.getString(0));
                order_date.add(cursor.getString(1));
                customer_info.add(cursor.getString(2));
                order_price.add(cursor.getString(3));
            }
        }
    }

    void setPizzaArrays() {
        Cursor cursor = database.readAllPizzas();
        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                pizza_id.add(cursor.getString(0));
                order_id_pizza.add(cursor.getString(1));
                pizza_size.add(cursor.getString(2));
                topping_1.add(cursor.getString(3));
                topping_2.add(cursor.getString(4));
                topping_3.add(cursor.getString(5));
            }
        }
    }
}
