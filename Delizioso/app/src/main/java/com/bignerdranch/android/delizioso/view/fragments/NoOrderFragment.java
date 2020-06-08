package com.bignerdranch.android.delizioso.view.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bignerdranch.android.delizioso.R;
import com.bignerdranch.android.delizioso.view.activities.MainActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class NoOrderFragment extends Fragment {
    private View noOrderButton;

    public NoOrderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_no_order, container, false);
        noOrderButton = root.findViewById(R.id.order);
        noOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                noOrderButton.setEnabled(false);
                NavigationView navigationView;
                if(getActivity() != null) {
                    navigationView = getActivity().findViewById(R.id.nav_view);
                    if (navigationView != null){
                        navigationView.setCheckedItem(R.id.nav_menu);
                        if (getActivity().getSupportFragmentManager() != null) {
                            FragmentTransaction ft = getActivity()
                                    .getSupportFragmentManager()
                                    .beginTransaction();
                            ft.replace(R.id.frame_content, new RestaurantMenuFragment());
                            ft.commit();
                        }
                    }
                }

            }
        });

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(noOrderButton != null){
            noOrderButton.setEnabled(true);
        }
    }
}
