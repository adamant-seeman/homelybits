package com.example.homelybites.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.homelybites.R;
import com.example.homelybites.activities.BecomeCookActivity;
import com.example.homelybites.activities.ChangeAddressActivity;
import com.example.homelybites.activities.CustomerLoginActivity;
import com.example.homelybites.activities.HelpCenterActivity;
import com.example.homelybites.activities.OrderHistoryActivity;
import com.example.homelybites.utils.FirebaseHelper;
import com.google.android.material.button.MaterialButton;

public class ProfileFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView tvName = view.findViewById(R.id.tvName);
        TextView tvEmail = view.findViewById(R.id.tvEmail);
        TextView tvOrdersCount = view.findViewById(R.id.tvOrdersCount);
        TextView tvRating = view.findViewById(R.id.tvRating);
        TextView tvFavCount = view.findViewById(R.id.tvFavCount);

        // Load user data
        FirebaseHelper helper = FirebaseHelper.getInstance();
        if (helper.getCurrentUser() != null) {
            helper.getUser(helper.getCurrentUser().getUid(), task -> {
                if (task.isSuccessful() && task.getResult().exists() && isAdded()) {
                    tvName.setText(task.getResult().getString("name"));
                    tvEmail.setText(task.getResult().getString("email"));
                    Long orders = task.getResult().getLong("ordersCount");
                    tvOrdersCount.setText(String.valueOf(orders != null ? orders : 0));
                    Double rating = task.getResult().getDouble("rating");
                    tvRating.setText(String.format("%.1f", rating != null ? rating : 0.0));
                    Long favs = task.getResult().getLong("favoritesCount");
                    tvFavCount.setText(String.valueOf(favs != null ? favs : 0));
                }
            });
        }

        // Navigation
        view.findViewById(R.id.cardMyOrders).setOnClickListener(v ->
                startActivity(new Intent(getActivity(), OrderHistoryActivity.class)));
        view.findViewById(R.id.cardHelpCenter).setOnClickListener(v ->
                startActivity(new Intent(getActivity(), HelpCenterActivity.class)));
        view.findViewById(R.id.cardChangeAddress).setOnClickListener(v ->
                startActivity(new Intent(getActivity(), ChangeAddressActivity.class)));
        view.findViewById(R.id.cardBecomeCook).setOnClickListener(v ->
                startActivity(new Intent(getActivity(), BecomeCookActivity.class)));

        view.findViewById(R.id.btnLogout).setOnClickListener(v -> {
            helper.signOut();
            startActivity(new Intent(getActivity(), CustomerLoginActivity.class));
            if (getActivity() != null) getActivity().finish();
        });
    }
}
