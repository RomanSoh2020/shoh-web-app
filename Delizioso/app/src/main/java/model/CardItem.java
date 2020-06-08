package model;

import android.os.Parcel;
import android.os.Parcelable;

import model.food.Food;

public class CardItem implements Parcelable {
    private Food food;
    private int quantity;

    public CardItem(Food food, int quantity) {
        this.food = food;
        this.quantity = quantity;
    }

    protected CardItem(Parcel in) {
        food = (Food)in.readSerializable();
        quantity = in.readInt();
    }

    public static final Creator<CardItem> CREATOR = new Creator<CardItem>() {
        @Override
        public CardItem createFromParcel(Parcel in) {
            return new CardItem(in);
        }

        @Override
        public CardItem[] newArray(int size) {
            return new CardItem[size];
        }
    };

    public Food getFood() {
        return food;
    }

    public void setFood(Food food) {
        this.food = food;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeSerializable(food);
        parcel.writeInt(quantity);
    }
}
