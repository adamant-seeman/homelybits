package com.example.homelybites.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.homelybites.databinding.ActivityReliabilityScoreBinding;
import com.example.homelybites.models.Order;
import com.example.homelybites.models.Review;
import com.example.homelybites.utils.FirebaseHelper;
import com.google.firebase.firestore.DocumentSnapshot;

public class ReliabilityScoreActivity extends AppCompatActivity {

    private ActivityReliabilityScoreBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityReliabilityScoreBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnBack.setOnClickListener(v -> finish());

        binding.btnLogout.setOnClickListener(v -> {
            FirebaseHelper.getInstance().signOut();
            startActivity(new Intent(this, CustomerLoginActivity.class));
            finish();
        });

        loadStats();
    }

    private void loadStats() {
        FirebaseHelper helper = FirebaseHelper.getInstance();
        if (helper.getCurrentUser() == null) return;
        String kitchenId = helper.getCurrentUser().getUid();

        // Load orders count
        helper.getOrdersByKitchen(kitchenId, task -> {
            if (task.isSuccessful()) {
                int totalOrders = task.getResult().size();
                int deliveredOrders = 0;
                for (DocumentSnapshot doc : task.getResult()) {
                    if ("delivered".equals(doc.getString("status"))) deliveredOrders++;
                }
                binding.tvTotalOrders.setText(String.valueOf(totalOrders));
                float completionRate = totalOrders > 0 ? (float) deliveredOrders / totalOrders * 5 : 0;
                binding.progressScore.setProgress(totalOrders > 0 ? (int)(completionRate * 20) : 0);
            }
        });

        // Load reviews
        helper.getReviewsByKitchen(kitchenId, task -> {
            if (task.isSuccessful()) {
                float totalRating = 0;
                int count = 0;
                for (DocumentSnapshot doc : task.getResult()) {
                    Double r = doc.getDouble("rating");
                    if (r != null) { totalRating += r.floatValue(); count++; }
                }
                float avg = count > 0 ? totalRating / count : 0;
                binding.tvAvgRating.setText(String.format("%.1f", avg));
                binding.tvScore.setText(String.format("%.1f", avg));
                binding.progressScore.setProgress((int)(avg * 20));
            }
        });
    }
}
