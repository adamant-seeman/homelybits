package com.example.homelybites.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homelybites.R;
import com.example.homelybites.activities.AddDishActivity;
import com.example.homelybites.adapters.DishAdapter;
import com.example.homelybites.models.Dish;
import com.example.homelybites.utils.FirebaseHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class MenuFragment extends Fragment {

    private RecyclerView rvMenu;
    private TextView tvNoMenu;
    private List<Dish> dishes = new ArrayList<>();
    private DishAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_menu, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvMenu = view.findViewById(R.id.rvMenu);
        tvNoMenu = view.findViewById(R.id.tvNoMenu);
        FloatingActionButton fab = view.findViewById(R.id.fabAddDish);

        adapter = new DishAdapter(dishes, dish ->
                Toast.makeText(getContext(), dish.getName(), Toast.LENGTH_SHORT).show());

        rvMenu.setLayoutManager(new LinearLayoutManager(getContext()));
        rvMenu.setAdapter(adapter);

        fab.setOnClickListener(v ->
                startActivity(new Intent(getActivity(), AddDishActivity.class)));
    }

    @Override
    public void onResume() {
        super.onResume();
        loadMenu();
    }

    private void loadMenu() {
        FirebaseHelper helper = FirebaseHelper.getInstance();
        if (helper.getCurrentUser() == null) return;

        helper.getDishesByKitchen(helper.getCurrentUser().getUid(), task -> {
            if (task.isSuccessful() && isAdded()) {
                dishes.clear();
                for (DocumentSnapshot doc : task.getResult()) {
                    Dish dish = doc.toObject(Dish.class);
                    if (dish != null) dishes.add(dish);
                }
                adapter.notifyDataSetChanged();
                tvNoMenu.setVisibility(dishes.isEmpty() ? View.VISIBLE : View.GONE);
                rvMenu.setVisibility(dishes.isEmpty() ? View.GONE : View.VISIBLE);
            }
        });
    }
}
