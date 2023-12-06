package com.example.assignment3_pizza;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;
import androidx.annotation.Nullable;
import java.text.ParseException;

public class Database extends SQLiteOpenHelper {

    private Context context;

    private static final String DATABASE_NAME = "pizza.db";
    private static final int DATABASE_VERSION = 1;

    // FOR ORDER TABLE
    private static final String TABLE_NAME_ORDERS = "orders";
    private static final String COLUMN_ORDER_ID = "order_id";
    private static final String COLUMN_DATE = "order_date";
    private static final String COLUMN_CUSTOMER = "customer_info";
    private static final String COLUMN_PRICE = "order_price";

    // FOR PIZZA TABLE
    private static final String TABLE_NAME_PIZZAS = "pizzas";
    private static final String COLUMN_PIZZA_ID = "pizza_id";
    private static final String FK_ORDER_ID = "order_id";
    private static final String COLUMN_PIZZA_SIZE = "pizza_size";
    private static final String COLUMN_TOPPING_1 = "topping_1";
    private static final String COLUMN_TOPPING_2 = "topping_2";
    private static final String COLUMN_TOPPING_3 = "topping_3";

    public Database(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String queryOrder = "CREATE TABLE " + TABLE_NAME_ORDERS + " (" +
                COLUMN_ORDER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_DATE + " TEXT, " +
                COLUMN_CUSTOMER + " TEXT, " +
                COLUMN_PRICE + " TEXT);";

        String queryPizzas = "CREATE TABLE " + TABLE_NAME_PIZZAS + " (" +
                COLUMN_PIZZA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                FK_ORDER_ID + " INTEGER, " +
                COLUMN_PIZZA_SIZE + " INTEGER, " +
                COLUMN_TOPPING_1 + " TEXT, " +
                COLUMN_TOPPING_2 + " TEXT, " +
                COLUMN_TOPPING_3 + " TEXT, " +
                "FOREIGN KEY (" + FK_ORDER_ID + ") REFERENCES " + TABLE_NAME_ORDERS + "(" + COLUMN_ORDER_ID + "));";

        db.execSQL(queryOrder);
        db.execSQL(queryPizzas);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_ORDERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_PIZZAS);
    }

    void addOrder(Order order) throws ParseException {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_DATE, order.getOrderTime());
        cv.put(COLUMN_CUSTOMER, order.customerInformation(context));
        cv.put(COLUMN_PRICE, order.getOrderPrice());
        long result = db.insert(TABLE_NAME_ORDERS, null, cv);

        if (result == -1) {
            Toast.makeText(context, "Failed to add order to database.", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(context, "Added order to database.", Toast.LENGTH_SHORT).show();
        }

        addPizzas(order);
        MainActivity.allOrders = new AllOrders(context);
    }

    void addPizzas(Order order) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        // for each pizza in the order, add to database
        for(Pizza pizza : order.getPizzas()) {
            for (int i = 0; i < pizza.getToppings().size(); i++) {
                switch (i) {
                    case 0:
                        cv.put(COLUMN_TOPPING_1, pizza.getToppings().get(i));
                        break;
                    case 1:
                        cv.put(COLUMN_TOPPING_2, pizza.getToppings().get(i));
                        break;
                    case 2:
                        cv.put(COLUMN_TOPPING_3, pizza.getToppings().get(i));
                }
            }

            cv.put(FK_ORDER_ID, order.getOrderNumber());
            cv.put(COLUMN_PIZZA_SIZE, pizza.getSize());
            long result = db.insert(TABLE_NAME_PIZZAS, null, cv);

            if (result == -1) {
                Toast.makeText(context, "Failed to add pizza to database", Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(context, "Added pizza to database.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    Cursor readAllOrders() {
        String query = "SELECT * FROM " + TABLE_NAME_ORDERS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);
        }

        return cursor;
    }

    Cursor readAllPizzas() {
        String query = "SELECT * FROM " + TABLE_NAME_PIZZAS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);
        }

        return cursor;
    }

    void updateOrder(String row_id, String customer_info) throws ParseException {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_CUSTOMER, customer_info);

        long result = db.update(TABLE_NAME_ORDERS, cv, " order_id=?", new String[]{row_id});

        if (result == -1) {
            Toast.makeText(context, "Failed to update order information", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(context, "Updated order successfully.", Toast.LENGTH_SHORT).show();
        }

        MainActivity.allOrders = new AllOrders(context);
        AllOrdersActivity.setOrders("", context.getString(R.string.all_orders));
    }

    void deleteOrder(String row_id) throws ParseException {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TABLE_NAME_ORDERS, "order_id=?", new String[]{row_id});

        if (result == -1) {
            Toast.makeText(context, "Failed to delete order.", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(context, "Deleted order successfully.", Toast.LENGTH_SHORT).show();
        }

        result = db.delete(TABLE_NAME_PIZZAS, "order_id=?", new String[]{row_id});
        if (result == -1) {
            Toast.makeText(context, "Failed to delete order from pizza table.", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(context, "Deleted order successfully from pizza table.", Toast.LENGTH_SHORT).show();
        }

        MainActivity.allOrders = new AllOrders(context);
        AllOrdersActivity.setOrders("", context.getString(R.string.all_orders));
    }
}