package com.example.homelybites.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {

    private EditText etSearch;
    private RecyclerView rvSearchResults;
    private TextView tvNoResults;
    private List<Kitchen> searchResults = new ArrayList<>();
    private KitchenAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etSearch = view.findViewById(R.id.etSearch);
        rvSearchResults = view.findViewById(R.id.rvSearchResults);
        tvNoResults = view.findViewById(R.id.tvNoResults);

        adapter = new KitchenAdapter(searchResults, kitchen -> {
            Intent intent = new Intent(getActivity(), KitchenDetailActivity.class);
            intent.putExtra("kitchenId", kitchen.getKitchenId());
            intent.putExtra("kitchenName", kitchen.getName());
            startActivity(intent);
        });

        rvSearchResults.setLayoutManager(new LinearLayoutManager(getContext()));
        rvSearchResults.setAdapter(adapter);

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                String query = s.toString().trim();
                if (query.length() >= 2) {
                    performSearch(query);
                } else {
                    searchResults.clear();
                    adapter.notifyDataSetChanged();
                    tvNoResults.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void performSearch(String query) {
        FirebaseHelper.getInstance().searchKitchens(query, task -> {
            if (task.isSuccessful() && isAdded()) {
                searchResults.clear();
                for (DocumentSnapshot doc : task.getResult()) {
                    Kitchen kitchen = doc.toObject(Kitchen.class);
                    if (kitchen != null) searchResults.add(kitchen);
                }
                adapter.notifyDataSetChanged();
                tvNoResults.setVisibility(searchResults.isEmpty() ? View.VISIBLE : View.GONE);
            }
        });
    }
}
