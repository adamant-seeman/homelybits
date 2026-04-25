package com.example.homelybites.models;

import java.io.Serializable;

public class Review implements Serializable {
    private String reviewId;
    private String orderId;
    private String customerId;
    private String customerName;
    private String kitchenId;
    private float rating;
    private String comment;
    private long timestamp;

    public Review() {}

    public Review(String reviewId, String orderId, String customerId, String kitchenId, float rating, String comment) {
        this.reviewId = reviewId;
        this.orderId = orderId;
        this.customerId = customerId;
        this.kitchenId = kitchenId;
        this.rating = rating;
        this.comment = comment;
        this.timestamp = System.currentTimeMillis();
    }

    public String getReviewId() { return reviewId; }
    public void setReviewId(String reviewId) { this.reviewId = reviewId; }

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getKitchenId() { return kitchenId; }
    public void setKitchenId(String kitchenId) { this.kitchenId = kitchenId; }

    public float getRating() { return rating; }
    public void setRating(float rating) { this.rating = rating; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
}
