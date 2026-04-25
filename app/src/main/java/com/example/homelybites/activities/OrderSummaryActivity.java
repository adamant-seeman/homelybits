package com.example.homelybites.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.homelybites.R;
import com.example.homelybites.databinding.ActivityOrderSummaryBinding;
import com.example.homelybites.models.Order;
import com.example.homelybites.utils.FirebaseHelper;

import java.util.ArrayList;
import java.util.List;

public class OrderSummaryActivity extends AppCompatActivity {

    private ActivityOrderSummaryBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrderSummaryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String kitchenId = getIntent().getStringExtra("kitchenId");
        String kitchenName = getIntent().getStringExtra("kitchenName");
        double totalAmount = getIntent().getDoubleExtra("totalAmount", 0);
        ArrayList<String> itemNames = getIntent().getStringArrayListExtra("itemNames");
        double[] itemPrices = getIntent().getDoubleArrayExtra("itemPrices");

        binding.tvKitchenName.setText(kitchenName);
        binding.tvTotal.setText(String.format("₹%.0f", totalAmount));
        binding.btnBack.setOnClickListener(v -> finish());

        // Display items
        if (itemNames != null) {
            for (int i = 0; i < itemNames.size(); i++) {
                LinearLayout row = new LinearLayout(this);
                row.setOrientation(LinearLayout.HORIZONTAL);
                row.setPadding(0, 8, 0, 8);

                TextView tvName = new TextView(this);
                tvName.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
                tvName.setText(itemNames.get(i));
                tvName.setTextSize(15);
                tvName.setTextColor(getColor(R.color.text_primary));

                TextView tvPrice = new TextView(this);
                tvPrice.setText(String.format("₹%.0f", itemPrices != null && i < itemPrices.length ? itemPrices[i] : 0));
                tvPrice.setTextSize(15);
                tvPrice.setTextColor(getColor(R.color.primary));

                row.addView(tvName);
                row.addView(tvPrice);
                binding.layoutItems.addView(row);

                // Divider
                if (i < itemNames.size() - 1) {
                    View divider = new View(this);
                    divider.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1));
                    divider.setBackgroundColor(getColor(R.color.divider));
                    binding.layoutItems.addView(divider);
                }
            }
        }

        // Load saved address
        FirebaseHelper helper = FirebaseHelper.getInstance();
        if (helper.getCurrentUser() != null) {
            helper.getUser(helper.getCurrentUser().getUid(), task -> {
                if (task.isSuccessful() && task.getResult().exists()) {
                    String address = task.getResult().getString("address");
                    if (address != null) binding.etAddress.setText(address);
                }
            });
        }

        binding.btnPlaceOrder.setOnClickListener(v -> {
            String address = binding.etAddress.getText().toString().trim();
            if (TextUtils.isEmpty(address)) {
                Toast.makeText(this, "Please enter delivery address", Toast.LENGTH_SHORT).show();
                return;
            }

            String paymentMethod = binding.rbUPI.isChecked() ? "UPI / PhonePe" : "Cash on Delivery";

            Order order = new Order();
            order.setOrderId(helper.generateOrderId());
            order.setCustomerId(helper.getCurrentUser().getUid());
            order.setKitchenId(kitchenId);
            order.setKitchenName(kitchenName);
            order.setTotalAmount(totalAmount);
            order.setAddress(address);
            order.setPaymentMethod(paymentMethod);
            order.setStatus("waiting");
            order.setTimestamp(System.currentTimeMillis());

            // Build order items
            List<Order.OrderItem> items = new ArrayList<>();
            if (itemNames != null) {
                for (int i = 0; i < itemNames.size(); i++) {
                    items.add(new Order.OrderItem(
                            "", itemNames.get(i),
                            itemPrices != null && i < itemPrices.length ? itemPrices[i] : 0, 1));
                }
            }
            order.setItems(items);

            // Get customer name
            helper.getUser(helper.getCurrentUser().getUid(), userTask -> {
                if (userTask.isSuccessful() && userTask.getResult().exists()) {
                    order.setCustomerName(userTask.getResult().getString("name"));
                }
                helper.saveOrder(order, saveTask -> {
                    if (saveTask.isSuccessful()) {
                        Toast.makeText(this, "Order placed successfully!", Toast.LENGTH_LONG).show();
                        finish();
                    } else {
                        Toast.makeText(this, "Failed to place order", Toast.LENGTH_SHORT).show();
                    }
                });
            });
        });
    }
}
