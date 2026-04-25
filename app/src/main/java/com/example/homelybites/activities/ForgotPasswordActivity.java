package com.example.homelybites.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.homelybites.databinding.ActivityForgotPasswordBinding;
import com.example.homelybites.utils.FirebaseHelper;

public class ForgotPasswordActivity extends AppCompatActivity {

    private ActivityForgotPasswordBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityForgotPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnBack.setOnClickListener(v -> finish());

        binding.btnReset.setOnClickListener(v -> {
            String email = binding.etEmail.getText().toString().trim();
            if (TextUtils.isEmpty(email)) {
                Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show();
                return;
            }

            binding.progressBar.setVisibility(View.VISIBLE);
            binding.btnReset.setEnabled(false);

            FirebaseHelper.getInstance().sendPasswordReset(email, task -> {
                binding.progressBar.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    binding.layoutSuccess.setVisibility(View.VISIBLE);
                    binding.btnReset.setVisibility(View.GONE);
                } else {
                    binding.btnReset.setEnabled(true);
                    Toast.makeText(this, "Error: " + task.getException().getMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}
