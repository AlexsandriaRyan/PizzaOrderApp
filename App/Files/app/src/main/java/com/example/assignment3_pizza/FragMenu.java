package com.example.assignment3_pizza;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.*;
import android.widget.Button;

public class FragMenu extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View inflatedView = inflater.inflate(R.layout.fragment_frag__menu, container, false);
        MainActivity mActivity = (MainActivity) requireActivity();
        Button btnNewOrder = inflatedView.findViewById(R.id.btnNewOrder);
        Button btnAllOrders = inflatedView.findViewById(R.id.btnAllOrders);
        Button btnChangeLanguage = inflatedView.findViewById(R.id.btnChangeLanguage);

        // Listeners
        btnNewOrder.setOnClickListener(v -> mActivity.newOrder());
        btnAllOrders.setOnClickListener(v -> mActivity.allOrders());
        btnChangeLanguage.setOnClickListener(v -> mActivity.changeLanguage());

        return inflatedView;
    }
}