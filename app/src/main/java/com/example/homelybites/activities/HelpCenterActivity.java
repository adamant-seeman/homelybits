package com.example.homelybites.activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.homelybites.databinding.ActivityHelpCenterBinding;

public class HelpCenterActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityHelpCenterBinding binding = ActivityHelpCenterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.btnBack.setOnClickListener(v -> finish());
    }
}
