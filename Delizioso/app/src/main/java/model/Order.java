package model;

import java.util.List;

import model.food.Food;

public class Order {
    private List<Food> orderedFood;
    private int id;
    private double totalPrice;
    private double totalTime;

    public Order(int id, List<Food> orderedFood) {
        this.id = id;
        this.orderedFood = orderedFood;
        setTotalPrice();
        setTotalTime();
    }

    public List<Food> getOrderedFood() {
        return orderedFood;
    }


    public int getId() {
        return id;
    }

    public void setOrderedFood(List<Food> orderedFood) {
        this.orderedFood = orderedFood;
        setTotalPrice();
        setTotalTime();
    }

    private void setTotalPrice() {
        if(orderedFood != null){
            for (int i = 0; i < orderedFood.size(); i++) {
                totalPrice += orderedFood.get(i).getPrice();
            }
        } else{
            totalPrice = 0;
        }
    }

    private void setTotalTime() {
        if(orderedFood != null){
            for (int i = 0; i < orderedFood.size(); i++) {
                totalTime += orderedFood.get(i).getTimeOfCooking();
            }
        } else {
            totalTime = 0;
        }
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public double getTotalTime() {
        return totalTime;
    }
}
