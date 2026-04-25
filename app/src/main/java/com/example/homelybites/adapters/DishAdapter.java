package com.example.homelybites.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homelybites.R;
import com.example.homelybites.models.Dish;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class DishAdapter extends RecyclerView.Adapter<DishAdapter.ViewHolder> {

    private final List<Dish> dishes;
    private OnDishActionListener listener;

    public interface OnDishActionListener {
        void onAddToOrder(Dish dish);
    }

    public DishAdapter(List<Dish> dishes, OnDishActionListener listener) {
        this.dishes = dishes;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_dish, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Dish dish = dishes.get(position);
        holder.tvDishName.setText(dish.getName());
        holder.tvPrice.setText(String.format("₹%.0f", dish.getPrice()));
        holder.tvDishDay.setText(dish.getDay() != null ? dish.getDay() : "");
        holder.tvServings.setText(dish.getServings() > 0 ? dish.getServings() + " servings left" : "");
        holder.btnAdd.setOnClickListener(v -> {
            if (listener != null) listener.onAddToOrder(dish);
        });
    }

    @Override
    public int getItemCount() { return dishes.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvDishName, tvPrice, tvDishDay, tvServings;
        MaterialButton btnAdd;

        ViewHolder(View itemView) {
            super(itemView);
            tvDishName = itemView.findViewById(R.id.tvDishName);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvDishDay = itemView.findViewById(R.id.tvDishDay);
            tvServings = itemView.findViewById(R.id.tvServings);
            btnAdd = itemView.findViewById(R.id.btnAdd);
        }
    }
}
