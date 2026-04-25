package com.example.homelybites.activities;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.homelybites.databinding.ActivityOrderHistoryBinding;
import com.example.homelybites.adapters.OrderAdapter;
import com.example.homelybites.models.Order;
import com.example.homelybites.utils.FirebaseHelper;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class OrderHistoryActivity extends AppCompatActivity {

    private ActivityOrderHistoryBinding binding;
    private List<Order> orders = new ArrayList<>();
    private OrderAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrderHistoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnBack.setOnClickListener(v -> finish());

        adapter = new OrderAdapter(orders, false, null);
        binding.rvOrders.setLayoutManager(new LinearLayoutManager(this));
        binding.rvOrders.setAdapter(adapter);

        loadOrders();
    }

    private void loadOrders() {
        FirebaseHelper helper = FirebaseHelper.getInstance();
        if (helper.getCurrentUser() == null) return;

        String uid = helper.getCurrentUser().getUid();

        // Try as customer first
        helper.getOrdersByCustomer(uid, task -> {
            if (task.isSuccessful()) {
                orders.clear();
                for (DocumentSnapshot doc : task.getResult()) {
                    Order order = doc.toObject(Order.class);
                    if (order != null) orders.add(order);
                }

                // If no customer orders, try as kitchen
                if (orders.isEmpty()) {
                    helper.getOrdersByKitchen(uid, kitchenTask -> {
                        if (kitchenTask.isSuccessful()) {
                            for (DocumentSnapshot doc : kitchenTask.getResult()) {
                                Order order = doc.toObject(Order.class);
                                if (order != null) orders.add(order);
                            }
                        }
                        updateUI();
                    });
                } else {
                    updateUI();
                }
            }
        });
    }

    private void updateUI() {
        adapter.notifyDataSetChanged();
        binding.tvNoOrders.setVisibility(orders.isEmpty() ? View.VISIBLE : View.GONE);
        binding.rvOrders.setVisibility(orders.isEmpty() ? View.GONE : View.VISIBLE);
    }
}
