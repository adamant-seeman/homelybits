package com.example.homelybites.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.homelybites.databinding.ActivityBecomeCookBinding;
import com.example.homelybites.models.Kitchen;
import com.example.homelybites.utils.FirebaseHelper;

import java.util.HashMap;
import java.util.Map;

public class BecomeCookActivity extends AppCompatActivity {

    private ActivityBecomeCookBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBecomeCookBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnBack.setOnClickListener(v -> finish());

        binding.btnRegister.setOnClickListener(v -> {
            String kitchenName = binding.etKitchenName.getText().toString().trim();
            String address = binding.etAddress.getText().toString().trim();
            String speciality = binding.etSpeciality.getText().toString().trim();
            String fssai = binding.etFssai.getText().toString().trim();

            if (TextUtils.isEmpty(kitchenName) || TextUtils.isEmpty(address)) {
                Toast.makeText(this, "Kitchen name and address are required", Toast.LENGTH_SHORT).show();
                return;
            }

            binding.progressBar.setVisibility(View.VISIBLE);
            binding.btnRegister.setEnabled(false);

            FirebaseHelper helper = FirebaseHelper.getInstance();
            String uid = helper.getCurrentUser().getUid();

            // Update user role to cook
            Map<String, Object> updates = new HashMap<>();
            updates.put("role", "cook");
            updates.put("kitchenName", kitchenName);
            updates.put("speciality", speciality);
            updates.put("fssaiNumber", fssai);

            helper.getDb().collection("users").document(uid).update(updates).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Kitchen kitchen = new Kitchen(uid, kitchenName, uid);
                    kitchen.setAddress(address);
                    kitchen.setSpeciality(speciality);
                    helper.saveKitchen(kitchen, kitchenTask -> {
                        binding.progressBar.setVisibility(View.GONE);
                        if (kitchenTask.isSuccessful()) {
                            Toast.makeText(this, "Kitchen registered!", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(this, CookDashboardActivity.class));
                            finish();
                        } else {
                            binding.btnRegister.setEnabled(true);
                            Toast.makeText(this, "Failed to create kitchen", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    binding.progressBar.setVisibility(View.GONE);
                    binding.btnRegister.setEnabled(true);
                    Toast.makeText(this, "Failed to update profile", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}
