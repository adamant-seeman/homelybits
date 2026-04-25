package com.example.homelybites.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homelybites.R;
import com.example.homelybites.models.Kitchen;

import java.util.List;

public class KitchenAdapter extends RecyclerView.Adapter<KitchenAdapter.ViewHolder> {

    private final List<Kitchen> kitchens;
    private OnKitchenClickListener listener;

    public interface OnKitchenClickListener {
        void onKitchenClick(Kitchen kitchen);
    }

    public KitchenAdapter(List<Kitchen> kitchens, OnKitchenClickListener listener) {
        this.kitchens = kitchens;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_kitchen, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Kitchen kitchen = kitchens.get(position);
        holder.tvKitchenName.setText(kitchen.getName());
        holder.tvRating.setText(String.format("%.1f", kitchen.getRating()));
        holder.tvDistance.setText(String.format("%.1f km away", kitchen.getDistance()));
        if (kitchen.getSpeciality() != null) {
            holder.tvSpeciality.setText(kitchen.getSpeciality());
        }
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onKitchenClick(kitchen);
        });
    }

    @Override
    public int getItemCount() { return kitchens.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgKitchen;
        TextView tvKitchenName, tvRating, tvDistance, tvSpeciality;

        ViewHolder(View itemView) {
            super(itemView);
            imgKitchen = itemView.findViewById(R.id.imgKitchen);
            tvKitchenName = itemView.findViewById(R.id.tvKitchenName);
            tvRating = itemView.findViewById(R.id.tvRating);
            tvDistance = itemView.findViewById(R.id.tvDistance);
            tvSpeciality = itemView.findViewById(R.id.tvSpeciality);
        }
    }
}
