package com.example.homelybites.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.homelybites.databinding.ActivityChangeAddressBinding;
import com.example.homelybites.utils.FirebaseHelper;

public class ChangeAddressActivity extends AppCompatActivity {

    private ActivityChangeAddressBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChangeAddressBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnBack.setOnClickListener(v -> finish());

        // Load existing address
        FirebaseHelper helper = FirebaseHelper.getInstance();
        if (helper.getCurrentUser() != null) {
            helper.getUser(helper.getCurrentUser().getUid(), task -> {
                if (task.isSuccessful() && task.getResult().exists()) {
                    String address = task.getResult().getString("address");
                    if (address != null) {
                        String[] parts = address.split(", ");
                        if (parts.length >= 1) binding.etAddressLine1.setText(parts[0]);
                        if (parts.length >= 2) binding.etAddressLine2.setText(parts[1]);
                        if (parts.length >= 3) binding.etCity.setText(parts[2]);
                        if (parts.length >= 4) binding.etPincode.setText(parts[3]);
                    }
                }
            });
        }

        binding.btnSave.setOnClickListener(v -> {
            String line1 = binding.etAddressLine1.getText().toString().trim();
            String line2 = binding.etAddressLine2.getText().toString().trim();
            String city = binding.etCity.getText().toString().trim();
            String pincode = binding.etPincode.getText().toString().trim();

            if (TextUtils.isEmpty(line1)) {
                Toast.makeText(this, "Please enter address", Toast.LENGTH_SHORT).show();
                return;
            }

            String fullAddress = line1;
            if (!TextUtils.isEmpty(line2)) fullAddress += ", " + line2;
            if (!TextUtils.isEmpty(city)) fullAddress += ", " + city;
            if (!TextUtils.isEmpty(pincode)) fullAddress += ", " + pincode;

            binding.progressBar.setVisibility(View.VISIBLE);
            binding.btnSave.setEnabled(false);

            helper.updateUserField(helper.getCurrentUser().getUid(), "address", fullAddress, task -> {
                binding.progressBar.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    Toast.makeText(this, "Address saved!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    binding.btnSave.setEnabled(true);
                    Toast.makeText(this, "Failed to save address", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}
