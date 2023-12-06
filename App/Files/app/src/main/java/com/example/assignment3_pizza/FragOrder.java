package com.example.assignment3_pizza;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.*;
import android.view.LayoutInflater;
import android.view.*;
import android.widget.Button;
import java.util.*;

public class FragOrder extends Fragment {
    List<CardText> sizesList = new ArrayList<>();
    List<CardText> toppingsList = new ArrayList<>();
    public static Button btnAddToCart;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View inflatedView = inflater.inflate(R.layout.fragment_frag_order, container, false);
        NewOrderActivity mActivity = (NewOrderActivity) requireActivity();
        Context context = getContext();
        NewOrderActivity.pizza.context = context;
        btnAddToCart = inflatedView.findViewById(R.id.btnAddToCart);
        btnAddToCart.setClickable(false);

        // Recycler View for pizza sizes
        RecyclerView sizes = inflatedView.findViewById(R.id.sizesInfo);
        RecyclerView.LayoutManager sizeManager = new GridLayoutManager(context, 2);
        CardAdapter sizeAdapter = new CardAdapter(sizesList, context);
        sizes.setLayoutManager(sizeManager);
        sizes.setAdapter(sizeAdapter);

        // Recycler View for pizza toppings
        RecyclerView toppings = inflatedView.findViewById(R.id.toppingsInfo);
        RecyclerView.LayoutManager toppingsManager = new GridLayoutManager(context, 2);
        CardAdapter toppingAdapter = new CardAdapter(toppingsList, context);
        toppings.setLayoutManager(toppingsManager);
        toppings.setAdapter(toppingAdapter);

        setSizes();
        setToppings();

        // Listeners
        btnAddToCart.setOnClickListener(view -> mActivity.popUp());

        return inflatedView;
    }

    private void setSizes() {
        Pizza temp = new Pizza(1);
        String str = getString(R.string.small) + " " + temp.getInches() + "\n$" + temp.getPrice();
        CardText size = new CardText(str, getActivity().getDrawable(R.drawable.pizza));
        sizesList.add(size);

        temp = new Pizza(2);
        str = getString(R.string.medium) + " " + temp.getInches() + "\n$" + temp.getPrice();
        size = new CardText(str, getActivity().getDrawable(R.drawable.pizza));
        sizesList.add(size);

        temp = new Pizza(3);
        str = getString(R.string.large) + " " + temp.getInches() + "\n$" + temp.getPrice();
        size = new CardText(str, getActivity().getDrawable(R.drawable.pizza));
        sizesList.add(size);

        temp = new Pizza(4);
        str = getString(R.string.xlarge) + " " + temp.getInches() + "\n$" + temp.getPrice();
        size = new CardText(str, getActivity().getDrawable(R.drawable.pizza));
        sizesList.add(size);
    }

    private void setToppings() {
        CardText top = new CardText(getString(R.string.banana_peppers), getActivity().getDrawable(R.drawable.banana_pepper));
        toppingsList.add(top);

        top = new CardText(getString(R.string.bbq_chicken), getActivity().getDrawable(R.drawable.chicken));
        toppingsList.add(top);

        top = new CardText(getString(R.string.cheese), getActivity().getDrawable(R.drawable.cheese));
        toppingsList.add(top);

        top = new CardText(getString(R.string.ham), getActivity().getDrawable(R.drawable.ham));
        toppingsList.add(top);

        top = new CardText(getString(R.string.onion), getActivity().getDrawable(R.drawable.onion));
        toppingsList.add(top);

        top = new CardText(getString(R.string.pepperoni),getActivity().getDrawable(R.drawable.pepperoni));
        toppingsList.add(top);
    }
}