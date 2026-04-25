package com.example.homelybites.models;

import java.io.Serializable;

public class Dish implements Serializable {
    private String dishId;
    private String kitchenId;
    private String name;
    private double price;
    private String day;
    private int servings;
    private String imageUrl;
    private String description;
    private boolean available;

    public Dish() {
        this.available = true;
    }

    public Dish(String dishId, String kitchenId, String name, double price, String day, int servings) {
        this.dishId = dishId;
        this.kitchenId = kitchenId;
        this.name = name;
        this.price = price;
        this.day = day;
        this.servings = servings;
        this.available = true;
    }

    public String getDishId() { return dishId; }
    public void setDishId(String dishId) { this.dishId = dishId; }

    public String getKitchenId() { return kitchenId; }
    public void setKitchenId(String kitchenId) { this.kitchenId = kitchenId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String getDay() { return day; }
    public void setDay(String day) { this.day = day; }

    public int getServings() { return servings; }
    public void setServings(int servings) { this.servings = servings; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }
}
