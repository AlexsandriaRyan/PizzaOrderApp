package com.example.assignment3_pizza;

import androidx.appcompat.app.*;
import androidx.fragment.app.*;
import android.content.*;
import android.content.res.Configuration;
import android.view.View;
import android.os.Bundle;

import java.text.ParseException;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    // TO-DO to get this where I want it (for funsies):
    // Make a "Menu Header" with fragments below
    // Make fragments for cart, all orders, etc. They will all use the "Menu Header" design.
    // Swap out the "Cart" and "Search" buttons on the "Menu Header",
    // depending on whether the user is in the Order section or the All Orders section
    // Make all the popUps in the "Menu Header" so that they overshadow the menu
    // (like what I did for the Cart!)
    // Make the "Menu Header" unclickable while there is a popup.

    public static AllOrders allOrders;
    public static SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            allOrders = new AllOrders(this.getApplicationContext());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        sharedPref = getPreferences(MODE_PRIVATE);

        // check if Shared Preferences already has a locale set & set appropriate fragment
        if (sharedPref.contains("LOCALE")) {
            setLanguage(sharedPref.getString("LOCALE", ""));
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            Fragment frMenu = new FragMenu();
            ft.replace(R.id.frag_place, frMenu);
            ft.commit();

        } else {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            Fragment frLang = new FragLanguage();
            ft.replace(R.id.frag_place, frLang);
            ft.commit();
        }
    }

    // when English / French buttons are clicked, proceed to setLanguage();
    public void langClicked(View v) {
        if (v.getId() == R.id.btnEnglish) {
            setLanguage("en");
        } else if (v.getId() == R.id.btnFrench) {
            setLanguage("fr");
        }

        toMenu();
    }

    // set the language and save Shared Preferences
    private void setLanguage(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        SharedPreferences sharedPref = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("LOCALE", lang);
        editor.commit();
    }

    // go to main menu
    public void toMenu() {
        Fragment frMenu = new FragMenu();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.frag_place, frMenu);
        ft.commit();
    }

    // start new order
    public void newOrder() {
        Intent launch = new Intent(this, NewOrderActivity.class);
        startActivity(launch);
    }

    //view all orders
    public void allOrders() {
        Intent launch = new Intent(MainActivity.this, AllOrdersActivity.class);
        startActivity(launch);
    }

    // go to change language screen
    public void changeLanguage() {
        Fragment frLang = new FragLanguage();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.frag_place, frLang);
        ft.commit();
    }
}