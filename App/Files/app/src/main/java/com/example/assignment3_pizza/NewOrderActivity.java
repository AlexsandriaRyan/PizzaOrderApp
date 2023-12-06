package com.example.assignment3_pizza;

import android.content.*;
import android.content.res.Configuration;
import android.os.Bundle;
import androidx.appcompat.app.*;
import androidx.fragment.app.*;
import android.util.Patterns;
import android.view.View;
import android.widget.*;

import java.text.ParseException;
import java.util.Locale;


public class NewOrderActivity extends AppCompatActivity {

    public static Pizza pizza;
    public static Order order;

    View popUpAddToCart, popUpMenu;
    Button btnContinueToCart, btnHoldUp, btnConfirm, btnInfoBack;
    EditText etxtName, etxtPhone, etxtEmail, etxtAddress, etxtNotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_order);

        if (MainActivity.sharedPref.contains("LOCALE")) {
            setLanguage(MainActivity.sharedPref.getString("LOCALE", ""));
        }

        ImageButton btnCart = findViewById(R.id.btnCart);
        ImageButton btnMenu = findViewById(R.id.btnMenu);
        popUpMenu = findViewById(R.id.popUpMenu);
        popUpAddToCart = findViewById(R.id.popUpAddToCart);
        btnContinueToCart = findViewById(R.id.btnContinueToCart);
        btnHoldUp = findViewById(R.id.btnHoldUp);
        pizza = new Pizza();

        // Menu Items
        Button btnMainMenu = findViewById(R.id.btnMainMenu);
        Button btnAllOrdersMenu = findViewById(R.id.btnAllOrdersMenu);
        Button btnEnglishMenu = findViewById(R.id.btnEnglishMenu);
        Button btnFrenchMenu = findViewById(R.id.btnFrenchMenu);
        ImageButton btnCloseMenu = findViewById(R.id.btnCloseMenu);

        // Listeners
        btnHoldUp.setOnClickListener(view -> popUpAddToCart.setVisibility(View.GONE));
        btnContinueToCart.setOnClickListener(this::toCart);
        btnCart.setOnClickListener(this::toCart);
        btnMainMenu.setOnClickListener(v -> toMenu());
        btnAllOrdersMenu.setOnClickListener(v -> toAllOrders());
        btnEnglishMenu.setOnClickListener(v -> { setLanguage("en"); refresh(); });
        btnFrenchMenu.setOnClickListener(v -> { setLanguage("fr"); refresh(); });
        btnCloseMenu.setOnClickListener(v -> popUpMenu.setVisibility(View.GONE));
        btnMenu.setOnClickListener(view -> popUpMenu.setVisibility(View.VISIBLE));

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            boolean menuOpen = extras.getBoolean("MENU_OPEN");
            if (menuOpen) {
                popUpMenu.setVisibility(View.VISIBLE);
            }
        }
    }

    public void popUp() {
        popUpAddToCart.setVisibility(View.VISIBLE);
        TextView txtDescription = findViewById(R.id.txtOrderPreview);
        txtDescription.setText(NewOrderActivity.pizza.displayPizza());;
    }

    public void toOrder() {
        Fragment frOrder = new FragOrder();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.frag_top_menu, frOrder);
        ft.commit();
        pizza = new Pizza();
    }

    public void toCart(View v) {
        Fragment frCart = new FragCart();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.frag_top_menu, frCart);
        ft.commit();

        if (v  == btnContinueToCart) {
            if (order == null) {
                order = new Order();
            }
            order.addPizzas(pizza);
            popUpAddToCart.setVisibility(View.GONE);
        }
    }

    public String confirmOrder() throws ParseException {

        etxtName = findViewById(R.id.etxtName);
        etxtPhone = findViewById(R.id.etxtPhone);
        etxtEmail = findViewById(R.id.etxtEmail);
        etxtAddress = findViewById(R.id.etxtAddress);
        etxtNotes = findViewById(R.id.etxtNotes);

        String error = "";
        error += checkInput("name", etxtName);
        error += checkInput("phone", etxtPhone);
        error += checkInput("email", etxtEmail);
        error += checkInput("address", etxtAddress);

        if (error.isBlank()) {
            order.setCustomerName(etxtName.getText().toString());
            order.setCustomerPhone(etxtPhone.getText().toString());
            order.setCustomerEmail(etxtEmail.getText().toString());
            order.setCustomerAddress(etxtAddress.getText().toString());
            order.setOrderNotes(etxtNotes.getText().toString());
            order.setOrderTime();
            order.setOrderPrice();

            // ADD THE ORDER AND PIZZAS TO THE DATABASE
            Database database = new Database(NewOrderActivity.this);
            database.addOrder(order);
        }

        return error;
    }

    private String checkInput(String type, EditText inputField) {
        String retString = "";
        String input = inputField.getText().toString();

        switch (type) {
            case "name":
                if (input.isBlank()) { retString = getString(R.string.error_name); }
                break ;

            case "phone":
                if (!input.matches("\\d{9,10}")) { retString = getString(R.string.error_phone); }
                break;

            case "email":
                if (!Patterns.EMAIL_ADDRESS.matcher(input).matches()) { retString = getString(R.string.error_email); }
                break;

            case "address":
                if (input.isBlank()) { retString = getString(R.string.error_address); }
                break ;

            default:
                // names, addresses, and notes can accept any type of character
                retString = "";
        }

        return retString + "\n";
    }

    public void toMenu() {
        Intent mainMenu = new Intent(this, MainActivity.class);
        startActivity(mainMenu);
        finish();
    }

    public void toAllOrders() {
        order = null;
        Intent allOrders = new Intent(this, AllOrdersActivity.class);
        startActivity(allOrders);
        finish();
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