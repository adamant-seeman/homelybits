package com.example.homelybites.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homelybites.R;
import com.example.homelybites.activities.KitchenDetailActivity;
import com.example.homelybites.adapters.KitchenAdapter;
import com.example.homelybites.models.Kitchen;
import com.example.homelybites.utils.FirebaseHelper;

import java.util.ArrayList;
import java.util.List;

public class FavoritesFragment extends Fragment {

    private RecyclerView rvFavorites;
    private TextView tvNoFavorites;
    private List<Kitchen> favoriteKitchens = new ArrayList<>();
    private KitchenAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favorites, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvFavorites = view.findViewById(R.id.rvFavorites);
        tvNoFavorites = view.findViewById(R.id.tvNoFavorites);

        adapter = new KitchenAdapter(favoriteKitchens, kitchen -> {
            Intent intent = new Intent(getActivity(), KitchenDetailActivity.class);
            intent.putExtra("kitchenId", kitchen.getKitchenId());
            intent.putExtra("kitchenName", kitchen.getName());
            startActivity(intent);
        });

        rvFavorites.setLayoutManager(new LinearLayoutManager(getContext()));
        rvFavorites.setAdapter(adapter);
        loadFavorites();
    }

    private void loadFavorites() {
        FirebaseHelper helper = FirebaseHelper.getInstance();
        if (helper.getCurrentUser() == null) return;

        helper.getUser(helper.getCurrentUser().getUid(), task -> {
            if (task.isSuccessful() && task.getResult().exists() && isAdded()) {
                List<String> favIds = (List<String>) task.getResult().get("favoriteKitchenIds");
                if (favIds != null && !favIds.isEmpty()) {
                    favoriteKitchens.clear();
                    for (String kitchenId : favIds) {
                        helper.getKitchen(kitchenId, kitchenTask -> {
                            if (kitchenTask.isSuccessful() && kitchenTask.getResult().exists()) {
                                Kitchen kitchen = kitchenTask.getResult().toObject(Kitchen.class);
                                if (kitchen != null) {
                                    favoriteKitchens.add(kitchen);
                                    adapter.notifyDataSetChanged();
                                    tvNoFavorites.setVisibility(View.GONE);
                                }
                            }
                        });
                    }
                } else {
                    tvNoFavorites.setVisibility(View.VISIBLE);
                }
            }
        });
    }
}
