package com.example.homelybites.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Order implements Serializable {
    private String orderId;
    private String customerId;
    private String customerName;
    private String kitchenId;
    private String kitchenName;
    private List<OrderItem> items;
    private double totalAmount;
    private String status; // "waiting", "accepted", "ongoing", "delivered", "rejected"
    private String address;
    private String paymentMethod;
    private long timestamp;

    public Order() {
        this.items = new ArrayList<>();
    }

    // Inner class for order items
    public static class OrderItem implements Serializable {
        private String dishId;
        private String dishName;
        private double price;
        private int quantity;

        public OrderItem() {}

        public OrderItem(String dishId, String dishName, double price, int quantity) {
            this.dishId = dishId;
            this.dishName = dishName;
            this.price = price;
            this.quantity = quantity;
        }

        public String getDishId() { return dishId; }
        public void setDishId(String dishId) { this.dishId = dishId; }
        public String getDishName() { return dishName; }
        public void setDishName(String dishName) { this.dishName = dishName; }
        public double getPrice() { return price; }
        public void setPrice(double price) { this.price = price; }
        public int getQuantity() { return quantity; }
        public void setQuantity(int quantity) { this.quantity = quantity; }
    }

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getKitchenId() { return kitchenId; }
    public void setKitchenId(String kitchenId) { this.kitchenId = kitchenId; }

    public String getKitchenName() { return kitchenName; }
    public void setKitchenName(String kitchenName) { this.kitchenName = kitchenName; }

    public List<OrderItem> getItems() { return items; }
    public void setItems(List<OrderItem> items) { this.items = items; }

    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
}
