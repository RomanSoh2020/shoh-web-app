package com.bignerdranch.android.delizioso.view.fragments.menuItemsFragments;


import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bignerdranch.android.delizioso.R;
import com.bignerdranch.android.delizioso.view.activities.FoodDetailActivity;
import com.bignerdranch.android.delizioso.view.adapters.FoodAdapter;
import com.bignerdranch.android.delizioso.view.adapters.IClickable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import controller.database.DeliziosoDatabaseHelper;
import controller.database.DeliziosoDatabaseScheme.*;
import model.food.Food;
import model.food.pizza.Pizza;

/**
 * A simple {@link Fragment} subclass.
 */
public class FoodFragment extends Fragment {
    private List<Food> foods = new ArrayList<>();
    private RecyclerView recyclerView;

    private static SQLiteDatabase db;
    private static Cursor cursor;

    public FoodFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_food, container, false);
        foods.clear();

        recyclerView = (RecyclerView) root.findViewById(R.id.food_recycler);

        if (getArguments() != null) {
            String type = getArguments().getString("type");
            if (type != null) {
                SQLiteOpenHelper helper = new DeliziosoDatabaseHelper(getContext());

                try{
                    db = helper.getReadableDatabase();
                    cursor = db.query(FoodScheme.TABLE_NAME,
                            new String[]{
                                    FoodScheme.Cols.ID,
                                    FoodScheme.Cols.NAME,
                                    FoodScheme.Cols.PRICE,
                                    FoodScheme.Cols.IMAGE_RESOURCE_ID,
                                    FoodScheme.Cols.DESCRIPTION,
                                    FoodScheme.Cols.COMPOSITION,
                                    FoodScheme.Cols.TIME_OF_COOKING},
                            FoodScheme.Cols.TYPE + " = ?",
                            new String[]{type},
                            null, null, null);

                    if (cursor.moveToFirst()) {
                        do {
                            foods.add(new Food.Builder(new Pizza())
                                    .withId(cursor.getInt(0))
                                    .withName(cursor.getString(1))
                                    .withPrice(cursor.getDouble(2))
                                    .withImageResource(cursor.getInt(3))
                                    .withDescription(cursor.getString(4))
                                    .withComposition(Arrays.asList(cursor.getString(5)
                                            .split(",")))
                                    .withTimeOfCooking(cursor.getDouble(6))
                                    .build());
                        } while (cursor.moveToNext());
                    }
                } catch(SQLiteException e){
                    Log.d(getClass().getSimpleName(), "database err: " + e.getMessage());
                    Toast.makeText(getContext(), "Database unavailable",
                            Toast.LENGTH_SHORT).show();
                }
            }
        }

        final FoodAdapter adapter = new FoodAdapter(foods, new IClickable() {
            @Override
            public void onClick(int position) {
                recyclerView.getChildAt(position).setEnabled(false);
                Intent intent = new Intent(getContext(), FoodDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("food", foods.get(position));
                intent.putExtra("foodBundle", bundle);
                startActivity(intent);
            }
        });

        recyclerView.setAdapter(adapter);

        return root;
    }


    @Override
    public void onResume() {
        super.onResume();
        if(recyclerView != null) {
            for (int i = 0; i < foods.size(); i++) {
                if(recyclerView.getChildAt(i) != null) {
                    recyclerView.getChildAt(i).setEnabled(true);
                }
            }
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        cursor.close();
        db.close();
    }
}
