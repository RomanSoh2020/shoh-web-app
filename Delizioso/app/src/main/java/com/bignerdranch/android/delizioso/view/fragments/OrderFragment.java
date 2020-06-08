package com.bignerdranch.android.delizioso.view.fragments;


import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.bignerdranch.android.delizioso.R;
import com.bignerdranch.android.delizioso.view.activities.FoodDetailActivity;
import com.bignerdranch.android.delizioso.view.adapters.CardAdapter;
import com.bignerdranch.android.delizioso.view.adapters.IClickable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import controller.service.DelayedMessageService;
import controller.database.DeliziosoDatabaseHelper;
import controller.database.DeliziosoDatabaseScheme.*;
import model.CardItem;
import model.food.Food;
import model.food.desserts.Dessert;
import model.food.drinks.Drink;
import model.food.pasta.Pasta;
import model.food.pizza.Pizza;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class OrderFragment extends Fragment {
    public static final int REQUEST_ORDER = 1543;
    private SQLiteOpenHelper helper;
    private SQLiteDatabase db;
    private Cursor cursor;
    private List<CardItem> cardItems;
    private List<CardItem> distinctItems;
    private CardAdapter adapter;
    private RecyclerView recyclerView;
    private AppCompatTextView bill;
    private AppCompatButton orderButton;

    private double totalPrice = 0;
    private double totalTime = 0;

    public OrderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_order, container, false);

        helper = new DeliziosoDatabaseHelper(getContext());

        try{
            db = helper.getWritableDatabase();
            cursor = db.query(
                    OrderScheme.TABLE_NAME,
                    new String[]{
                            OrderScheme.Cols.ID,
                            OrderScheme.Cols.TOTAL_TIME,
                            OrderScheme.Cols.TOTAL_PRICE
                    },
                    OrderScheme.Cols.ID + " = ?",
                    new String[]{"1"},
                    null, null, null
            );
            FragmentTransaction ft = getChildFragmentManager().beginTransaction();
            Fragment fragment;

            if(cursor.moveToFirst()) {
                totalTime = cursor.getDouble(1);
                totalPrice = cursor.getDouble(2);
                if (totalPrice == 0) {
                    fragment = new NoOrderFragment();
                } else {
                    fragment = new CardFragment();
                }

                FrameLayout layout = root.findViewById(R.id.order);
                if (layout.getChildCount() == 0) {
                    ft.add(R.id.order, fragment);
                } else {
                    ft.replace(R.id.order, fragment);
                }
                ft.addToBackStack(null);
                ft.commit();



                cursor = db.rawQuery("SELECT food." + FoodScheme.Cols.ID +
                                ", food." + FoodScheme.Cols.NAME +
                                ", food." + FoodScheme.Cols.DESCRIPTION +
                                ", food." + FoodScheme.Cols.IMAGE_RESOURCE_ID +
                                ", food." + FoodScheme.Cols.TIME_OF_COOKING +
                                ", food." + FoodScheme.Cols.PRICE +
                                ", fo_or." + FoodOrderScheme.Cols.QUANTITY +
                                ", food." + FoodScheme.Cols.TYPE +
                                ", food." + FoodScheme.Cols.COMPOSITION +
                                " FROM " + FoodScheme.TABLE_NAME
                                + " food, " + FoodOrderScheme.TABLE_NAME + " fo_or, "
                                + OrderScheme.TABLE_NAME + " ord WHERE ord." + OrderScheme.Cols.ID
                                + " = fo_or." + FoodOrderScheme.Cols.ORDER_ID + " AND fo_or."
                                + FoodOrderScheme.Cols.FOOD_ID + " = food." + FoodScheme.Cols.ID
                                + " AND ord." + OrderScheme.Cols.ID + " = 1;"
                        , null
                );
                if (cursor.moveToFirst()) {
                    cardItems = new ArrayList<>();
                    do {
                        int foodId = cursor.getInt(0);
                        String foodName = cursor.getString(1);
                        String description = cursor.getString(2);
                        int imgRes = cursor.getInt(3);
                        double timeOfCooking = cursor.getDouble(4);
                        double price = cursor.getDouble(5);
                        int quantity = cursor.getInt(6);
                        String type = cursor.getString(7);
                        String composition = cursor.getString(8);

                        Food.Builder builder;
                        switch (type) {
                            case "pizza":
                                builder = new Food.Builder(new Pizza());
                                break;
                            case "pasta":
                                builder = new Food.Builder(new Pasta());
                                break;
                            case "drink":
                                builder = new Food.Builder(new Drink());
                                break;
                            case "dessert":
                                builder = new Food.Builder(new Dessert());
                                break;
                            default:
                                builder = new Food.Builder(new Food() {
                                    @Override
                                    public String getType() {
                                        return "unknown";
                                    }
                                });
                                break;
                        }

                        Food orderedFood = builder
                                .withId(foodId)
                                .withName(foodName)
                                .withDescription(description)
                                .withImageResource(imgRes)
                                .withPrice(price)
                                .withTimeOfCooking(timeOfCooking)
                                .withComposition(Arrays.asList(composition.split(", ")))
                                .build();

                        CardItem item = new CardItem(orderedFood, quantity);
                        cardItems.add(item);

                    } while (cursor.moveToNext());

                    if (!cardItems.isEmpty()) {
                        distinctItems = new ArrayList<>();
                        unifyItems();

                        adapter = new CardAdapter(distinctItems, new IClickable() {
                            @Override
                            public void onClick(int position) {
                                final Intent intent = new Intent(getContext(),
                                        FoodDetailActivity.class);
                                Log.d(getClass().getSimpleName(), "onClick: ");
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("food", distinctItems.get(position).getFood());
                                intent.putExtra("foodBundle", bundle);
                                startActivityForResult(intent, REQUEST_ORDER);
                            }

                            @Override
                            public void onLongClick(int position) {
                                if(getActivity() != null) {
                                    AlertDialog dialog
                                            = new AlertDialog.Builder(getActivity())
                                            .setMessage(R.string.delete_item_message)
                                            .setTitle(R.string.remove_item)
                                            .setPositiveButton("OK", (dialogInterface, i) -> {
                                                CardItem deletedItem =
                                                        distinctItems.remove(position);
                                                adapter.notifyItemRemoved(position);
                                                adapter.notifyItemRangeChanged(position,
                                                        adapter.getItemCount());

                                                db.delete(FoodOrderScheme.TABLE_NAME,
                                                        FoodOrderScheme.Cols.ORDER_ID
                                                                + " = ? AND "
                                                                + FoodOrderScheme.Cols.FOOD_ID
                                                                + " = ?",
                                                        new String[]{"1",
                                                                String.valueOf(deletedItem.getFood()
                                                                        .getId())});

                                                ContentValues values = new ContentValues();
                                                totalPrice -= deletedItem.getFood().getPrice()
                                                        * deletedItem.getQuantity();
                                                totalTime -= deletedItem.getFood().getTimeOfCooking()
                                                        * deletedItem.getQuantity();
                                                bill.setText(String.format(Locale.getDefault(),
                                                        "%.2f$",totalPrice));

                                                values.put(OrderScheme.Cols.TOTAL_PRICE, totalPrice);
                                                values.put(OrderScheme.Cols.TOTAL_TIME, totalTime);

                                                db.update(OrderScheme.TABLE_NAME,
                                                        values,
                                                        OrderScheme.Cols.ID + " = ?",
                                                        new String[]{"1"});
                                                if (distinctItems.isEmpty()){
                                                    FragmentTransaction ft = getChildFragmentManager()
                                                            .beginTransaction();
                                                    ft.replace(R.id.order,
                                                            new NoOrderFragment());
                                                    ft.addToBackStack(null);
                                                    ft.commit();
                                                }

                                            })
                                            .setNegativeButton("Cancel", (dialogInterface, i) -> {

                                            })
                                            .create();

                                    dialog.show();

                                }
                            }
                        });
                    }
                }
            }
        }catch (SQLiteException e){
            Log.d(getClass().getSimpleName(), "onCreateView: " + e.getMessage());
        }

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        if(getView() != null) {
            recyclerView = getView().findViewById(R.id.card_recycler);
            if(recyclerView != null){
                recyclerView.setAdapter(adapter);
                bill = getView().findViewById(R.id.bill);
                if(bill != null){
                    bill.setText(String.format(Locale.getDefault(),"%.2f$", totalPrice));
                }
            }

             orderButton = getView().findViewById(R.id.order_button);
            if(orderButton != null) {
                orderButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        orderButton.setEnabled(false);

                        Intent intent = new Intent(getContext(), DelayedMessageService.class);
                        intent.putExtra(DelayedMessageService.EXTRA_MESSAGE,
                                getResources().getString(R.string.response));
                        intent.putExtra(DelayedMessageService.TIME_OF_WAITING, totalTime);
                        if (getActivity() != null) {
                            getActivity().startService(intent);
                            double time = 0;
                            double price = 0;
                            ContentValues cv = new ContentValues();
                            cv.put(OrderScheme.Cols.TOTAL_TIME, time);
                            cv.put(OrderScheme.Cols.TOTAL_PRICE, price);
                            db.update(OrderScheme.TABLE_NAME, cv, "_id = 1",
                                    null);

                            db.delete(FoodOrderScheme.TABLE_NAME, null, null);
                            FragmentTransaction ft = getChildFragmentManager().beginTransaction();
                            ft.replace(R.id.order, new NoOrderFragment());
                            ft.addToBackStack(null);
                            ft.commit();
                        }
                    }
                });
            }
        }


    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        cursor.close();
        db.close();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(getClass().getSimpleName(), "onActivityResult: requestCode "
                + requestCode + "resultCode " + resultCode);

        if(resultCode == RESULT_OK){
            switch (requestCode){
                case REQUEST_ORDER:
                    CardItem item = data.getParcelableExtra("foodItem");
                    Food food = item.getFood();
                    int quantity = item.getQuantity();

                    totalPrice += food.getPrice() * quantity;
                    totalTime += food.getTimeOfCooking() * quantity;

                    bill.setText(String.format(Locale.getDefault(), "%.2f$", totalPrice));
                    cardItems.clear();
                    cardItems.addAll(distinctItems);
                    cardItems.add(item);
                    unifyItems();
                    adapter.notifyDataSetChanged();
                    break;
            }
        } else {
            Log.d(getClass().getSimpleName(), "onActivityResult: wrong result");
        }
    }

    private void unifyItems(){
        boolean containsItem;
        distinctItems.clear();

        for (int i = 0; i < cardItems.size(); i++) {
            containsItem = false;
            for (int j = 0; j < distinctItems.size(); j++) {
                if(cardItems.get(i).getFood().getId()
                        == distinctItems.get(j).getFood().getId() && i != j){
                    containsItem = true;
                    break;
                }
            }
            if(!containsItem){
                distinctItems.add(cardItems.get(i));
            } else {
                for (int j = 0; j < distinctItems.size(); j++) {
                    if(cardItems.get(i).getFood().getId()
                            == distinctItems.get(j).getFood().getId() && i != j){
                        distinctItems.get(j).setQuantity(distinctItems
                                .get(j).getQuantity() + cardItems.get(i)
                                .getQuantity());
                        break;
                    }
                }
            }
        }
    }
}
