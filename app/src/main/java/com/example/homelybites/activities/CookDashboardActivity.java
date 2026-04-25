package com.example.homelybites.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.homelybites.databinding.ActivityCookDashboardBinding;
import com.example.homelybites.fragments.MenuFragment;
import com.example.homelybites.fragments.RequestsFragment;
import com.google.android.material.tabs.TabLayoutMediator;

public class CookDashboardActivity extends AppCompatActivity {

    private ActivityCookDashboardBinding binding;
    private final String[] tabTitles = {"Requests", "Menu"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCookDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.viewPager.setAdapter(new FragmentStateAdapter(this) {
            @Override
            public int getItemCount() { return 2; }

            @Override
            public Fragment createFragment(int position) {
                return position == 0 ? new RequestsFragment() : new MenuFragment();
            }
        });

        new TabLayoutMediator(binding.tabLayout, binding.viewPager,
                (tab, position) -> tab.setText(tabTitles[position])).attach();

        binding.btnHistory.setOnClickListener(v ->
                startActivity(new Intent(this, OrderHistoryActivity.class)));

        binding.btnProfile.setOnClickListener(v ->
                startActivity(new Intent(this, ReliabilityScoreActivity.class)));
    }
}
