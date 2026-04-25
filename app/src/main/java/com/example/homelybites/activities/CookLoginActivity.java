package com.example.homelybites.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.homelybites.R;
import com.example.homelybites.databinding.ActivityCookLoginBinding;
import com.example.homelybites.models.Kitchen;
import com.example.homelybites.models.User;
import com.example.homelybites.utils.FirebaseHelper;

public class CookLoginActivity extends AppCompatActivity {

    private ActivityCookLoginBinding binding;
    private FirebaseHelper firebaseHelper;
    private boolean isSignUpMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCookLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseHelper = FirebaseHelper.getInstance();

        setupListeners();
    }

    private void setupListeners() {
        binding.btnLogin.setOnClickListener(v -> {
            if (isSignUpMode) performSignUp();
            else performLogin();
        });

        binding.tvToggleMode.setOnClickListener(v -> toggleMode());

        binding.tvForgotPassword.setOnClickListener(v ->
                startActivity(new Intent(this, ForgotPasswordActivity.class)));

        binding.btnCustomerMode.setOnClickListener(v -> {
            startActivity(new Intent(this, CustomerLoginActivity.class));
            finish();
        });
    }

    private void toggleMode() {
        isSignUpMode = !isSignUpMode;
        if (isSignUpMode) {
            binding.tvTitle.setText("Cook Sign Up");
            binding.btnLogin.setText(R.string.signup);
            binding.tvToggleMode.setText(R.string.already_have_account);
            binding.tilName.setVisibility(View.VISIBLE);
            binding.tilPhone.setVisibility(View.VISIBLE);
            binding.tilKitchenName.setVisibility(View.VISIBLE);
            binding.tvForgotPassword.setVisibility(View.GONE);
        } else {
            binding.tvTitle.setText("Cook Login");
            binding.btnLogin.setText(R.string.login);
            binding.tvToggleMode.setText(R.string.dont_have_account);
            binding.tilName.setVisibility(View.GONE);
            binding.tilPhone.setVisibility(View.GONE);
            binding.tilKitchenName.setVisibility(View.GONE);
            binding.tvForgotPassword.setVisibility(View.VISIBLE);
        }
    }

    private void performLogin() {
        String email = binding.etEmail.getText().toString().trim();
        String password = binding.etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        showLoading(true);
        firebaseHelper.signIn(email, password, task -> {
            showLoading(false);
            if (task.isSuccessful()) {
                startActivity(new Intent(this, CookDashboardActivity.class));
                finish();
            } else {
                Toast.makeText(this, "Login failed: " + task.getException().getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void performSignUp() {
        String email = binding.etEmail.getText().toString().trim();
        String name = binding.etName.getText().toString().trim();
        String phone = binding.etPhone.getText().toString().trim();
        String kitchenName = binding.etKitchenName.getText().toString().trim();
        String password = binding.etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(name) || TextUtils.isEmpty(phone)
                || TextUtils.isEmpty(kitchenName) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 6) {
            Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
            return;
        }

        showLoading(true);
        firebaseHelper.signUp(email, password, task -> {
            if (task.isSuccessful()) {
                String uid = task.getResult().getUser().getUid();
                User user = new User(uid, name, email, phone, "cook");
                user.setKitchenName(kitchenName);

                firebaseHelper.saveUser(user, saveTask -> {
                    if (saveTask.isSuccessful()) {
                        // Create kitchen
                        Kitchen kitchen = new Kitchen(uid, kitchenName, uid);
                        kitchen.setCookName(name);
                        firebaseHelper.saveKitchen(kitchen, kitchenTask -> {
                            showLoading(false);
                            if (kitchenTask.isSuccessful()) {
                                startActivity(new Intent(this, CookDashboardActivity.class));
                                finish();
                            } else {
                                Toast.makeText(this, "Error creating kitchen", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        showLoading(false);
                        Toast.makeText(this, "Error saving profile", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                showLoading(false);
                Toast.makeText(this, "Sign up failed: " + task.getException().getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showLoading(boolean show) {
        binding.progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        binding.btnLogin.setEnabled(!show);
    }
}
