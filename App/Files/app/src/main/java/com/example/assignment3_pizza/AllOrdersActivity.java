package com.example.assignment3_pizza;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.*;
import android.content.*;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import java.util.*;

public class AllOrdersActivity extends AppCompatActivity {

    static List<CardText> ordersList = new ArrayList<>();
    public static View popUpOrderDetails, popUpAODelete, popUpAOEdit;
    public static TextView txtDisplayDetails, txtConfirmation, txtAllOrders;
    public static Button btnBackDetails, btnDeleteOK, btnDeleteBack, btnConfirmEdit, btnEditBack;
    public static EditText etxtName, etxtPhone, etxtEmail, etxtAddress, etxtNotes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_orders);

        if (MainActivity.sharedPref.contains("LOCALE")) {
            setLanguage(MainActivity.sharedPref.getString("LOCALE", ""));
        }

        // General
        ImageButton btnSearch = findViewById(R.id.btnSearch);
        ImageButton btnMenu = findViewById(R.id.btnMenu2);
        txtAllOrders = findViewById(R.id.txtAllOrders);

        // Pop Up SEARCH
        View popUpSearch = findViewById(R.id.popUpSearch);
        Button btnBack = findViewById(R.id.btnSearchBack);
        EditText etxtSearch = findViewById(R.id.etxtSearch);
        ImageButton btnLookup = findViewById(R.id.btnLookup);

        // Pop Up MENU
        View popUpMenu2 = findViewById(R.id.popUpMenu2);

        // Pop Up ORDER DETAILS
        popUpOrderDetails = findViewById(R.id.popUpOrderDetails);
        txtDisplayDetails = findViewById(R.id.txtDisplayDetails);
        btnBackDetails = findViewById(R.id.btnBackDetails);

        // Pop Up EDIT
        popUpAOEdit = findViewById(R.id.popUpEditCustomerInfo);
        etxtName = findViewById(R.id.etxtName2);
        etxtPhone = findViewById(R.id.etxtPhone2);
        etxtEmail = findViewById(R.id.etxtEmail2);
        etxtAddress = findViewById(R.id.etxtAddress2);
        etxtNotes = findViewById(R.id.etxtNotes2);
        btnConfirmEdit = findViewById(R.id.btnConfirmEdit);
        btnEditBack = findViewById(R.id.btnEditBack);

        // Pop Up DELETE
        popUpAODelete = findViewById(R.id.popUpAODelete);
        txtConfirmation = findViewById(R.id.txtAODeleteConfirmation);
        btnDeleteOK = findViewById(R.id.btnAODeleteOK);
        btnDeleteBack = findViewById(R.id.btnAODeleteBack);

        // Listeners
        btnBack.setOnClickListener(view -> popUpSearch.setVisibility(View.GONE));
        btnBackDetails.setOnClickListener(v -> AllOrdersActivity.popUpOrderDetails.setVisibility(View.GONE));
        btnSearch.setOnClickListener(view -> popUpSearch.setVisibility(View.VISIBLE));
        btnLookup.setOnClickListener(v-> {
            String type = "";
            if (etxtSearch.getText().toString().isEmpty()) {
                type = getString(R.string.all_orders);
            } else {
                type = getString(R.string.search);
            }

            setOrders(etxtSearch.getText().toString(), type);
            popUpSearch.setVisibility(View.GONE); });

        // Set up Menu only on click
        btnMenu.setOnClickListener(view -> {
            popUpMenu2.setVisibility(View.VISIBLE);
            Button btnMainMenu = findViewById(R.id.btnMainMenu);
            Button btnNewOrderMenu = findViewById(R.id.btnNewOrderMenu);
            Button btnEnglishMenu = findViewById(R.id.btnEnglishMenu);
            Button btnFrenchMenu = findViewById(R.id.btnFrenchMenu);
            ImageButton btnCloseMenu = findViewById(R.id.btnCloseMenu);

            btnMainMenu.setOnClickListener(v -> toMenu());
            btnNewOrderMenu.setOnClickListener(v -> toNewOrder());
            btnEnglishMenu.setOnClickListener(v -> { setLanguage("en"); refresh();});
            btnFrenchMenu.setOnClickListener(v -> { setLanguage("fr"); refresh();});
            btnCloseMenu.setOnClickListener(v -> popUpMenu2.setVisibility(View.GONE));
        });

        // If there are orders to list, list them
        if (MainActivity.allOrders != null) {
            RecyclerView orders = findViewById(R.id.ordersInfo);
            RecyclerView.LayoutManager ordersManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
            CardOrdersAdapter ordersAdapter = new CardOrdersAdapter(ordersList, getApplicationContext());
            orders.setLayoutManager(ordersManager);
            orders.setAdapter(ordersAdapter);
            setOrders("", getString(R.string.all_orders));
        }
    }

    public static void setOrders(String search, String type) {
        // clears order list before setting
        ordersList.clear();

        // set the title to whatever the user is looking at (Ex. All Orders or Search)
        txtAllOrders.setText(type);

        List<Order> orders = MainActivity.allOrders.getAllOrders();

        // create a card for each order
        for(Order order : orders) {
            // if a search was provided, list only the relevant order numbers
            if (!search.isEmpty()) {
                if (search.contains(String.valueOf(order.getOrderNumber()))) {
                    CardText orderText = new CardText(order.getOrderNumber(), order.getOrderTime(), order.getOrderPrice());
                    ordersList.add(orderText);
                }

            } else {
                CardText orderText = new CardText(order.getOrderNumber(), order.getOrderTime(), order.getOrderPrice());
                ordersList.add(orderText);
            }
        }
    }

    private void toMenu() {
        Intent mainMenu = new Intent(this, MainActivity.class);
        finish();
        startActivity(mainMenu);
    }

    private void toNewOrder() {
        Intent newOrder = new Intent(this, NewOrderActivity.class);
        finish();
        startActivity(newOrder);
    }

    private void setLanguage(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        SharedPreferences.Editor editor = MainActivity.sharedPref.edit();
        editor.putString("LOCALE", lang);
        editor.commit();
    }

    // if the user was in the menu and requested a language change, the activity will refresh with the new language
    private void refresh() {
        Intent refresh = getIntent();
        Bundle menuOpen = new Bundle();
        menuOpen.putBoolean("MENU_OPEN", true);
        refresh.putExtras(menuOpen);
        finish();
        startActivity(refresh);
    }


}