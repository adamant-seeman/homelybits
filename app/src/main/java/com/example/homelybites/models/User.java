package com.example.homelybites.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable {
    private String uid;
    private String name;
    private String email;
    private String phone;
    private String role; // "customer" or "cook"
    private String address;
    private String profileImageUrl;
    private String kitchenName;
    private String speciality;
    private String fssaiNumber;
    private int ordersCount;
    private int favoritesCount;
    private float rating;
    private List<String> favoriteKitchenIds;

    public User() {
        this.favoriteKitchenIds = new ArrayList<>();
    }

    public User(String uid, String name, String email, String phone, String role) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.role = role;
        this.favoriteKitchenIds = new ArrayList<>();
    }

    // Getters and Setters
    public String getUid() { return uid; }
    public void setUid(String uid) { this.uid = uid; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getProfileImageUrl() { return profileImageUrl; }
    public void setProfileImageUrl(String profileImageUrl) { this.profileImageUrl = profileImageUrl; }

    public String getKitchenName() { return kitchenName; }
    public void setKitchenName(String kitchenName) { this.kitchenName = kitchenName; }

    public String getSpeciality() { return speciality; }
    public void setSpeciality(String speciality) { this.speciality = speciality; }

    public String getFssaiNumber() { return fssaiNumber; }
    public void setFssaiNumber(String fssaiNumber) { this.fssaiNumber = fssaiNumber; }

    public int getOrdersCount() { return ordersCount; }
    public void setOrdersCount(int ordersCount) { this.ordersCount = ordersCount; }

    public int getFavoritesCount() { return favoritesCount; }
    public void setFavoritesCount(int favoritesCount) { this.favoritesCount = favoritesCount; }

    public float getRating() { return rating; }
    public void setRating(float rating) { this.rating = rating; }

    public List<String> getFavoriteKitchenIds() { return favoriteKitchenIds; }
    public void setFavoriteKitchenIds(List<String> favoriteKitchenIds) { this.favoriteKitchenIds = favoriteKitchenIds; }
}
