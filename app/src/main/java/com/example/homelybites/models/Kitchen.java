package com.example.homelybites.models;

import java.io.Serializable;

public class Kitchen implements Serializable {
    private String kitchenId;
    private String name;
    private String cookUid;
    private String cookName;
    private String imageUrl;
    private String description;
    private String speciality;
    private String address;
    private float rating;
    private float distance;
    private int totalOrders;
    private int totalReviews;

    public Kitchen() {}

    public Kitchen(String kitchenId, String name, String cookUid) {
        this.kitchenId = kitchenId;
        this.name = name;
        this.cookUid = cookUid;
    }

    public String getKitchenId() { return kitchenId; }
    public void setKitchenId(String kitchenId) { this.kitchenId = kitchenId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCookUid() { return cookUid; }
    public void setCookUid(String cookUid) { this.cookUid = cookUid; }

    public String getCookName() { return cookName; }
    public void setCookName(String cookName) { this.cookName = cookName; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getSpeciality() { return speciality; }
    public void setSpeciality(String speciality) { this.speciality = speciality; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public float getRating() { return rating; }
    public void setRating(float rating) { this.rating = rating; }

    public float getDistance() { return distance; }
    public void setDistance(float distance) { this.distance = distance; }

    public int getTotalOrders() { return totalOrders; }
    public void setTotalOrders(int totalOrders) { this.totalOrders = totalOrders; }

    public int getTotalReviews() { return totalReviews; }
    public void setTotalReviews(int totalReviews) { this.totalReviews = totalReviews; }
}
