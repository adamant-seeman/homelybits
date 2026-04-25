package com.example.homelybites.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.homelybites.R;
import com.example.homelybites.databinding.ActivityAddDishBinding;
import com.example.homelybites.models.Dish;
import com.example.homelybites.utils.FirebaseHelper;

public class AddDishActivity extends AppCompatActivity {

    private ActivityAddDishBinding binding;
    private final String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddDishBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnBack.setOnClickListener(v -> finish());
        binding.spinnerDay.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, days));

        binding.btnSave.setOnClickListener(v -> {
            String name = binding.etDishName.getText().toString().trim();
            String priceStr = binding.etPrice.getText().toString().trim();
            String servingsStr = binding.etServings.getText().toString().trim();
            String day = binding.spinnerDay.getSelectedItem().toString();

            if (TextUtils.isEmpty(name) || TextUtils.isEmpty(priceStr) || TextUtils.isEmpty(servingsStr)) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            binding.progressBar.setVisibility(View.VISIBLE);
            binding.btnSave.setEnabled(false);

            FirebaseHelper helper = FirebaseHelper.getInstance();
            String dishId = helper.generateId("dishes");
            Dish dish = new Dish(dishId, helper.getCurrentUser().getUid(), name,
                    Double.parseDouble(priceStr), day, Integer.parseInt(servingsStr));

            helper.saveDish(dish, task -> {
                binding.progressBar.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    Toast.makeText(this, "Dish added!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    binding.btnSave.setEnabled(true);
                    Toast.makeText(this, "Failed to add dish", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}
