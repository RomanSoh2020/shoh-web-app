package com.bignerdranch.android.delizioso.view.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bignerdranch.android.delizioso.R;

import java.util.List;
import java.util.Locale;

import model.CardItem;
import model.food.Food;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder>{
    private List<CardItem> cardItems;
    private IClickable listener;

    public CardAdapter(List<CardItem> cardItems, IClickable listener) {
        this.cardItems = cardItems;
        this.listener = listener;
        setHasStableIds(true);
    }

    public List<CardItem> getCardItems() {
        return cardItems;
    }

    public void setCardItems(List<CardItem> cardItems) {
        this.cardItems = cardItems;
        notifyDataSetChanged();
    }

    public IClickable getListener() {
        return listener;
    }

    public void setListener(IClickable listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View root = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_list_item,
                viewGroup, false);

        return new ViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder,  int i) {
        View root = viewHolder.item;
        CardItem item = cardItems.get(i);
        Food food = item.getFood();

        AppCompatTextView quantity = root.findViewById(R.id.quantity);
        AppCompatTextView total = root.findViewById(R.id.total);
        AppCompatImageView image = root.findViewById(R.id.food_image);
        AppCompatTextView name = root.findViewById(R.id.food_name);
        AppCompatTextView price = root.findViewById(R.id.food_price);

        quantity.setText(String.valueOf(item.getQuantity()));
        total.setText(String.format(Locale.getDefault(),
                "%.2f$",food.getPrice() * item.getQuantity()));
        image.setImageResource(food.getImageResource());
        name.setText(food.getName());
        price.setText(String.format(Locale.getDefault(),"%.2f$", food.getPrice()));
        LinearLayoutCompat layoutCompat = root.findViewById(R.id.item_linear_layout);
        layoutCompat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener != null) {
                    Log.d("item ", "onClick: clicked" );
                    listener.onClick(i);
                }
            }
        });

        layoutCompat.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                listener.onLongClick(i);
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return cardItems.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    public static class ViewHolder extends FoodAdapter.ViewHolder{
        View item;

        public ViewHolder(View item) {
            super(item);
            this.item = item;
        }
    }
}
