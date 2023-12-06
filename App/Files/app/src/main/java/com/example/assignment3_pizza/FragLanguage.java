package com.example.assignment3_pizza;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.*;
import android.widget.Button;

public class FragLanguage extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View inflatedView = inflater.inflate(R.layout.fragment_frag__language, container, false);
        MainActivity mActivity = (MainActivity) requireActivity();
        Button btnEnglish = inflatedView.findViewById(R.id.btnEnglish);
        Button btnFrench = inflatedView.findViewById(R.id.btnFrench);

        // Listeners
        btnEnglish.setOnClickListener(mActivity::langClicked);
        btnFrench.setOnClickListener(mActivity::langClicked);

        return inflatedView;
    }
}