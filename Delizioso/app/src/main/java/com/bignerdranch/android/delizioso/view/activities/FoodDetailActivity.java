package com.bignerdranch.android.delizioso.view.activities;

import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bignerdranch.android.delizioso.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import controller.database.DeliziosoDatabaseHelper;
import controller.database.DeliziosoDatabaseScheme;
import model.CardItem;
import model.food.Food;

public class FoodDetailActivity extends AppCompatActivity {
    private static double totalTime = 0;
    private static double totalPrice = 0;
    private static int totalFoods = 1;
    private static SQLiteOpenHelper helper;

    private AppCompatImageView purchaseButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);

        helper = new DeliziosoDatabaseHelper(FoodDetailActivity.this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final Resources resources = getResources();

        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        final Food food = (Food) getIntent().getBundleExtra("foodBundle").get("food");


        AppCompatImageView foodImage = findViewById(R.id.food_image);
        AppCompatTextView foodName = findViewById(R.id.food_name);
        AppCompatTextView composition = findViewById(R.id.composition);
        AppCompatTextView description = findViewById(R.id.description);

        purchaseButton = findViewById(R.id.add_to_order);
        final AppCompatEditText numberInput = findViewById(R.id.number_of_product);
        final AppCompatTextView foodPrice = findViewById(R.id.food_price);


        purchaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(FoodDetailActivity.this, "Your order was updated",
                        Toast.LENGTH_SHORT).show();
                purchaseButton.setEnabled(false);
                new addToOrder().execute(food);
                CardItem item = new CardItem(food, totalFoods);
                Intent intent = new Intent();
                intent.putExtra("foodItem", item);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        if(food != null) {
            foodImage.setImageResource(food.getImageResource());
            foodName.setText(food.getName());
            foodPrice.setText(String.format(resources.getString(R.string.price) + " %.2f$",
                    food.getPrice()));


            numberInput.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    totalPrice = totalFoods * food.getPrice();
                    totalTime = totalFoods * food.getTimeOfCooking();
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    String input = String.valueOf(charSequence);

                    if(!input.isEmpty()){
                        Pattern pattern = Pattern.compile("^[1-9]+[0-9]*$");
                        Matcher matcher = pattern.matcher(input);
                        if(matcher.find()){
                            long number = Long.valueOf(input);
                            foodPrice.setText((String.format(resources.getString(R.string.price)
                                            + " %.2f", food.getPrice() * number)));
                        } else{
                            numberInput.setText("1");
                        }
                    } else {
                        numberInput.setText("1");
                    }

                    totalPrice = totalFoods * food.getPrice();
                    totalTime = totalFoods * food.getTimeOfCooking();
                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if(!editable.toString().isEmpty()) {
                        totalFoods = Integer.valueOf(editable.toString());
                    } else {
                        totalFoods = 1;
                    }
                    totalPrice = totalFoods * food.getPrice();
                    totalTime = totalFoods * food.getTimeOfCooking();
                }
            });

            composition.setText(String.format("%s %s",
                    composition.getText(),
                    food.getCompostionAsString())
            );

            description.setText(food.getDescription());
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        purchaseButton.setEnabled(true);
    }


    public static class addToOrder extends AsyncTask<Food, Void, Boolean>{

        private Food food;
        private ContentValues values;

        @Override
        protected void onPreExecute() {
            values = new ContentValues();

        }

        @Override
        protected Boolean doInBackground(Food... foods) {
            this.food = foods[0];

            try{
                SQLiteDatabase db = helper.getWritableDatabase();
                Cursor cursor = db.query(DeliziosoDatabaseScheme.OrderScheme.TABLE_NAME,
                        new String[]{DeliziosoDatabaseScheme.OrderScheme.Cols.ID,
                                DeliziosoDatabaseScheme.OrderScheme.Cols.TOTAL_PRICE,
                                DeliziosoDatabaseScheme.OrderScheme.Cols.TOTAL_TIME},
                        DeliziosoDatabaseScheme.OrderScheme.Cols.ID + " = ?",
                        new String[]{"1"},
                        null,null,null);

                if(cursor.moveToFirst()){

                    int foodId = food.getId();
                    int orderId = 1;
                    values.put(DeliziosoDatabaseScheme.FoodOrderScheme.Cols.FOOD_ID, foodId);
                    values.put(DeliziosoDatabaseScheme.FoodOrderScheme.Cols.ORDER_ID, orderId);
                    values.put(DeliziosoDatabaseScheme.FoodOrderScheme.Cols.QUANTITY, totalFoods);

                    db.insert(DeliziosoDatabaseScheme.FoodOrderScheme.TABLE_NAME,
                                null, values);

                    values.clear();
                    totalTime = food.getTimeOfCooking() * totalFoods;
                    totalPrice = food.getPrice() * totalFoods;

                    values.put(DeliziosoDatabaseScheme.OrderScheme.Cols.TOTAL_TIME,
                            totalTime + cursor.getDouble(2));
                    values.put(DeliziosoDatabaseScheme.OrderScheme.Cols.TOTAL_PRICE,
                            totalPrice + cursor.getDouble(1));
                    db.update(DeliziosoDatabaseScheme.OrderScheme.TABLE_NAME,
                            values,
                            DeliziosoDatabaseScheme.OrderScheme.Cols.ID + " = ?",
                            new String[]{String.valueOf(orderId)});
                    cursor.close();
                    db.close();

                }

            }catch (SQLiteException e){
                Log.d(getClass().getSimpleName(), "doInBackground: " + e.getMessage());
                return false;
            }

            return true;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if(!success){
                Log.d(getClass().getSimpleName(), "onPostExecute: " + "database unavailable");
            }
        }
    }
}
