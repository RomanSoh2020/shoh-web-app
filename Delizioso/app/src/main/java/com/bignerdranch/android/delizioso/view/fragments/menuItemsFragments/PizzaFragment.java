package com.bignerdranch.android.delizioso.view.fragments.menuItemsFragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bignerdranch.android.delizioso.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PizzaFragment extends Fragment {


    public PizzaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pizza, container, false);
    }

}
