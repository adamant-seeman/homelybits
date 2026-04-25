package com.example.homelybites.adapters;

import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homelybites.R;
import com.example.homelybites.models.Order;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {

    private final List<Order> orders;
    private boolean showActions;
    private OnOrderActionListener listener;

    public interface OnOrderActionListener {
        void onAccept(Order order);
        void onReject(Order order);
    }

    public OrderAdapter(List<Order> orders, boolean showActions, OnOrderActionListener listener) {
        this.orders = orders;
        this.showActions = showActions;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_order, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Order order = orders.get(position);
        holder.tvOrderId.setText("#" + order.getOrderId().substring(0, Math.min(8, order.getOrderId().length())));
        holder.tvCustomerName.setText(order.getCustomerName() != null ? order.getCustomerName() : order.getKitchenName());
        holder.tvAmount.setText(String.format("₹%.0f", order.getTotalAmount()));

        // Items summary
        StringBuilder itemsText = new StringBuilder();
        if (order.getItems() != null) {
            for (int i = 0; i < order.getItems().size(); i++) {
                if (i > 0) itemsText.append(", ");
                itemsText.append(order.getItems().get(i).getDishName());
            }
        }
        holder.tvItems.setText(itemsText.toString());

        // Status
        holder.tvStatus.setText(order.getStatus() != null ? order.getStatus().toUpperCase() : "");
        int statusColor = getStatusColor(holder.itemView, order.getStatus());
        GradientDrawable statusBg = new GradientDrawable();
        statusBg.setCornerRadius(12);
        statusBg.setColor(statusColor);
        holder.tvStatus.setBackground(statusBg);

        // Actions
        if (showActions && "waiting".equals(order.getStatus())) {
            holder.layoutActions.setVisibility(View.VISIBLE);
            holder.btnAccept.setOnClickListener(v -> {
                if (listener != null) listener.onAccept(order);
            });
            holder.btnReject.setOnClickListener(v -> {
                if (listener != null) listener.onReject(order);
            });
        } else {
            holder.layoutActions.setVisibility(View.GONE);
        }
    }

    private int getStatusColor(View view, String status) {
        if (status == null) return ContextCompat.getColor(view.getContext(), R.color.text_secondary);
        switch (status) {
            case "waiting": return ContextCompat.getColor(view.getContext(), R.color.status_waiting);
            case "accepted": return ContextCompat.getColor(view.getContext(), R.color.status_accepted);
            case "ongoing": return ContextCompat.getColor(view.getContext(), R.color.status_ongoing);
            case "delivered": return ContextCompat.getColor(view.getContext(), R.color.status_delivered);
            case "rejected": return ContextCompat.getColor(view.getContext(), R.color.status_rejected);
            default: return ContextCompat.getColor(view.getContext(), R.color.text_secondary);
        }
    }

    @Override
    public int getItemCount() { return orders.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrderId, tvCustomerName, tvItems, tvAmount, tvStatus;
        LinearLayout layoutActions;
        MaterialButton btnAccept, btnReject;

        ViewHolder(View itemView) {
            super(itemView);
            tvOrderId = itemView.findViewById(R.id.tvOrderId);
            tvCustomerName = itemView.findViewById(R.id.tvCustomerName);
            tvItems = itemView.findViewById(R.id.tvItems);
            tvAmount = itemView.findViewById(R.id.tvAmount);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            layoutActions = itemView.findViewById(R.id.layoutActions);
            btnAccept = itemView.findViewById(R.id.btnAccept);
            btnReject = itemView.findViewById(R.id.btnReject);
        }
    }
}
