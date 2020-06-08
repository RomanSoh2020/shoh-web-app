package com.bignerdranch.android.delizioso.view.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bignerdranch.android.delizioso.R;

import java.util.List;
import java.util.Locale;

import model.food.Food;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.ViewHolder> {
    private List<Food> foods;
    private IClickable listener;

    public FoodAdapter(List<Food> foods, IClickable listener) {
        this.foods = foods;
        this.listener = listener;
        setHasStableIds(true);
    }

    public List<Food> getFoods() {
        return foods;
    }

    public void setFoods(List<Food> foods) {
        this.foods = foods;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View root = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.food_list_item,
                viewGroup, false);

        return new ViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        View item = viewHolder.item;
        final int position = i;
        Food food = foods.get(position);


        AppCompatImageView foodImage = (AppCompatImageView) item.findViewById(R.id.food_image);
        AppCompatTextView foodName = (AppCompatTextView) item.findViewById(R.id.food_name);
        AppCompatTextView foodPrice = (AppCompatTextView) item.findViewById(R.id.food_price);

        foodImage.setImageResource(food.getImageResource());
        foodName.setText(food.getName());
        foodPrice.setText(String.format(Locale.getDefault(),
                "%.2f",food.getPrice()).concat("$"));

        item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener != null){
                    listener.onClick(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return foods.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        View item;

        public ViewHolder(View item) {
            super(item);
            this.item = item;
        }
    }
}
