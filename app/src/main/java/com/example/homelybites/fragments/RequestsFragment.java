package com.example.homelybites.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homelybites.R;
import com.example.homelybites.adapters.OrderAdapter;
import com.example.homelybites.models.Order;
import com.example.homelybites.utils.FirebaseHelper;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class RequestsFragment extends Fragment {

    private RecyclerView rvRequests;
    private TextView tvNoRequests;
    private List<Order> orders = new ArrayList<>();
    private OrderAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_requests, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvRequests = view.findViewById(R.id.rvRequests);
        tvNoRequests = view.findViewById(R.id.tvNoRequests);

        adapter = new OrderAdapter(orders, true, new OrderAdapter.OnOrderActionListener() {
            @Override
            public void onAccept(Order order) {
                FirebaseHelper.getInstance().updateOrderStatus(order.getOrderId(), "accepted", task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(getContext(), "Order accepted!", Toast.LENGTH_SHORT).show();
                        loadOrders();
                    }
                });
            }

            @Override
            public void onReject(Order order) {
                FirebaseHelper.getInstance().updateOrderStatus(order.getOrderId(), "rejected", task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(getContext(), "Order rejected", Toast.LENGTH_SHORT).show();
                        loadOrders();
                    }
                });
            }
        });

        rvRequests.setLayoutManager(new LinearLayoutManager(getContext()));
        rvRequests.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadOrders();
    }

    private void loadOrders() {
        FirebaseHelper helper = FirebaseHelper.getInstance();
        if (helper.getCurrentUser() == null) return;

        String kitchenId = helper.getCurrentUser().getUid();
        helper.getPendingOrdersByKitchen(kitchenId, task -> {
            if (task.isSuccessful() && isAdded()) {
                orders.clear();
                for (DocumentSnapshot doc : task.getResult()) {
                    Order order = doc.toObject(Order.class);
                    if (order != null) orders.add(order);
                }
                adapter.notifyDataSetChanged();
                tvNoRequests.setVisibility(orders.isEmpty() ? View.VISIBLE : View.GONE);
                rvRequests.setVisibility(orders.isEmpty() ? View.GONE : View.VISIBLE);
            }
        });
    }
}
