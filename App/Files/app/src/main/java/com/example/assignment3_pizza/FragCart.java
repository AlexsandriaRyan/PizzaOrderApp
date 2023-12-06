package com.example.assignment3_pizza;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.*;
import android.view.*;
import android.widget.*;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class FragCart extends Fragment {

    List<CardText> ordersList = new ArrayList<>();
    Context context = getContext();
    static TextView txtTotalValue;
    TextView txtTotal, txtError;
    Button btnCheckout, btnCartBack;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflatedView = inflater.inflate(R.layout.fragment_frag_cart, container, false);

        // General
        NewOrderActivity mActivity = (NewOrderActivity) requireActivity();

        // Cart
        btnCartBack = inflatedView.findViewById(R.id.btnCartBack);
        btnCheckout = inflatedView.findViewById(R.id.btnCheckout);
        txtTotalValue = inflatedView.findViewById(R.id.txtTotalValue);
        txtTotal = inflatedView.findViewById(R.id.txtTotal);

        // Pop Up CUSTOMER INFORMATION
        View popUpCustomerInfo = inflatedView.findViewById(R.id.popUpCustomerInfo);
        txtError = inflatedView.findViewById(R.id.txtError);
        Button btnConfirm = inflatedView.findViewById(R.id.btnConfirm);
        Button btnInfoBack = inflatedView.findViewById(R.id.btnInfoBack);

        // Pop Up CONFIRMATION
        View popUpConfirmed = inflatedView.findViewById(R.id.popUpConfirmed);

        // If there are pizza orders to list, list them
        if (NewOrderActivity.order != null) {
            RecyclerView cart = inflatedView.findViewById(R.id.cartInfo);
            RecyclerView.LayoutManager cartManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
            CardCartAdapter cartAdapter = new CardCartAdapter(ordersList, context);
            cart.setLayoutManager(cartManager);
            cart.setAdapter(cartAdapter);

            String value = "$" + setOrder();
            txtTotalValue.setText(value);
        } else {
            emptyCart();
        }

        // Listeners
        btnCartBack.setOnClickListener(view -> { prepCart(); mActivity.toOrder(); });
        btnCheckout.setOnClickListener(view -> popUpCustomerInfo.setVisibility(View.VISIBLE));
        btnInfoBack.setOnClickListener(v -> popUpCustomerInfo.setVisibility(View.GONE));
        btnConfirm.setOnClickListener(view -> {
            boolean confirmed = false;
            try {
                confirmed = checkConfirmation(mActivity);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }

            if (confirmed) {
                // Confirmation Pop Up
                popUpCustomerInfo.setVisibility(View.GONE);
                popUpConfirmed.setVisibility(View.VISIBLE);
                TextView orderConfirmation = inflatedView.findViewById(R.id.txtOrderPreview);
                TextView orderDateTime = inflatedView.findViewById(R.id.txtOrderConfirmedDescription);
                Button btnOK = inflatedView.findViewById(R.id.btnOK);

                String confirm = getString(R.string.order) + " " + getString(R.string.is_confirmed);
                orderConfirmation.setText(confirm);
                orderDateTime.setText(NewOrderActivity.order.getOrderTime());

                btnOK.setOnClickListener(v -> { popUpConfirmed.setVisibility(View.GONE); mActivity.toAllOrders(); });
            }
        });

        return inflatedView;
    }

    private String setOrder() {
        ordersList.clear();
        double total = 0;
        for(Pizza pizza : NewOrderActivity.order.getPizzas()) {
            if (pizza.getSize() != 0) {
                String str = pizza.displayPizza();
                CardText orderText = new CardText(str, getActivity().getDrawable(R.drawable.pizza));
                ordersList.add(orderText);
                total += pizza.getPrice();
            }
        }

        return String.format("%.2f", total);
    }

    public void emptyCart() {
        // if there is nothing in the cart, show error message
        txtTotal.setText(R.string.empty_cart);
        txtTotal.setGravity(Gravity.CENTER);
        txtTotalValue.setVisibility(View.GONE);
        btnCheckout.setVisibility(View.GONE);
        btnCartBack.setGravity(Gravity.CENTER);
    }

    public void prepCart() {
        // ensure the cart is not displaying the emptyCart()'s error message
        txtTotal.setText(R.string.total);
        txtTotal.setGravity(Gravity.LEFT);
        txtTotalValue.setVisibility(View.VISIBLE);
        btnCheckout.setVisibility(View.VISIBLE);
        btnCartBack.setGravity(Gravity.LEFT);
    }

    private boolean checkConfirmation(NewOrderActivity mActivity) throws ParseException {
        String error = mActivity.confirmOrder();
        if (!error.isBlank()) {
            txtError.setText(error);
            txtError.setVisibility(View.VISIBLE);
            return false;

        } else {
            return true;
        }
    }
}