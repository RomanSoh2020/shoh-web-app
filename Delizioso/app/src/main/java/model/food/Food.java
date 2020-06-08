package model.food;

import java.io.Serializable;
import java.util.List;


public abstract class Food implements Serializable {
    private int id;
    protected String name;
    private String description;
    private int imageResource;
    private double timeOfCooking;
    private double price;
    private List<String> composition;


    public abstract String getType();

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getTimeOfCooking() {
        return timeOfCooking;
    }

    public void setTimeOfCooking(double timeOfCooking) {
        this.timeOfCooking = timeOfCooking;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public List<String> getComposition() {
        return composition;
    }
    public String getCompostionAsString(){
        StringBuilder temp = new StringBuilder();

        if(composition != null){
            for (int i = 0; i < composition.size(); i++) {
                temp.append(composition.get(i)).append(", ");
            }

            return temp.substring(0, temp.length() - 2);
        }

        return null;
    }
    public void setComposition(List<String> composition) {
        this.composition = composition;
    }

    public int getImageResource() {
        return imageResource;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }


    public static class Builder{
        Food food;

        public Builder(Food food) {
            this.food = food;
        }

        public Builder withId(int id){
            food.id = id;
            return this;
        }
        public Builder withName(String name){
            food.name = name;
            return this;
        }

        public Builder withDescription(String description){
            food.description = description;
            return this;
        }

        public Builder withImageResource(int imageResource){
            food.imageResource = imageResource;
            return this;
        }

        public Builder withComposition(List<String> composition){
            food.composition = composition;
            return this;
        }

        public Builder withTimeOfCooking(double time){
            food.timeOfCooking = time;
            return this;
        }

        public Builder withPrice(double price){
            food.price = price;
            return this;
        }

        public Food build(){
            return food;
        }
    }
}
