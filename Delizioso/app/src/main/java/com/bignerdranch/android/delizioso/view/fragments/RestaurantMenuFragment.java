package com.bignerdranch.android.delizioso.view.fragments;


import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bignerdranch.android.delizioso.R;
import com.bignerdranch.android.delizioso.view.fragments.menuItemsFragments.DessertFragment;
import com.bignerdranch.android.delizioso.view.fragments.menuItemsFragments.DrinkFragment;
import com.bignerdranch.android.delizioso.view.fragments.menuItemsFragments.FoodFragment;
import com.bignerdranch.android.delizioso.view.fragments.menuItemsFragments.PastaFragment;
import com.bignerdranch.android.delizioso.view.fragments.menuItemsFragments.PizzaFragment;

public class RestaurantMenuFragment extends Fragment {


    public RestaurantMenuFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =
                inflater.inflate(R.layout.fragment_restaurant_menu, container, false);
        SectionsPagerAdapter pagerAdapter = new SectionsPagerAdapter(getChildFragmentManager());
        ViewPager pager = (ViewPager) root.findViewById(R.id.pager);
        pager.setAdapter(pagerAdapter);

        TabLayout tabLayout = (TabLayout) root.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(pager);

        return root;
    }

    private class SectionsPagerAdapter extends FragmentPagerAdapter{

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Bundle bundle = new Bundle();
            FoodFragment fragment = new FoodFragment();
            switch (position){
                case 0:
                    bundle.clear();
                    bundle.putString("type", "pizza");
                    break;
                case 1:
                    fragment = new FoodFragment();
                    bundle.clear();
                    bundle.putString("type", "pasta");
                    break;
                case 2:
                    fragment = new FoodFragment();
                    bundle.clear();
                    bundle.putString("type", "dessert");
                    break;
                case 3:
                    fragment = new FoodFragment();
                    bundle.clear();
                    bundle.putString("type", "drink");
                    break;
            }
            fragment.setArguments(bundle);
            return fragment;
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            Resources res = getResources();

            switch (position){
                case 0:
                    return res.getText(R.string.pizza_tab);
                case 1:
                    return res.getText(R.string.pasta_tab);
                case 2:
                    return res.getText(R.string.dessert_tab);
                case 3:
                    return res.getText(R.string.drink_tab);
            }
            return null;
        }
    }
}
