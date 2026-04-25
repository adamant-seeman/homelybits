package com.example.homelybites.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.homelybites.databinding.ActivityRatingBinding;
import com.example.homelybites.models.Review;
import com.example.homelybites.utils.FirebaseHelper;

public class RatingActivity extends AppCompatActivity {

    private ActivityRatingBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRatingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String kitchenId = getIntent().getStringExtra("kitchenId");
        String kitchenName = getIntent().getStringExtra("kitchenName");
        String orderId = getIntent().getStringExtra("orderId");

        binding.tvKitchenName.setText(kitchenName != null ? kitchenName : "Kitchen");
        binding.btnBack.setOnClickListener(v -> finish());

        binding.btnSubmit.setOnClickListener(v -> {
            float rating = binding.ratingBar.getRating();
            String comment = binding.etReview.getText().toString().trim();

            if (rating == 0) {
                Toast.makeText(this, "Please select a rating", Toast.LENGTH_SHORT).show();
                return;
            }

            binding.progressBar.setVisibility(View.VISIBLE);
            binding.btnSubmit.setEnabled(false);

            FirebaseHelper helper = FirebaseHelper.getInstance();
            String reviewId = helper.generateId("reviews");
            Review review = new Review(reviewId, orderId,
                    helper.getCurrentUser().getUid(), kitchenId, rating, comment);

            helper.getUser(helper.getCurrentUser().getUid(), userTask -> {
                if (userTask.isSuccessful() && userTask.getResult().exists()) {
                    review.setCustomerName(userTask.getResult().getString("name"));
                }
                helper.saveReview(review, task -> {
                    binding.progressBar.setVisibility(View.GONE);
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Review submitted!", Toast.LENGTH_LONG).show();
                        finish();
                    } else {
                        binding.btnSubmit.setEnabled(true);
                        Toast.makeText(this, "Failed to submit review", Toast.LENGTH_SHORT).show();
                    }
                });
            });
        });
    }
}
