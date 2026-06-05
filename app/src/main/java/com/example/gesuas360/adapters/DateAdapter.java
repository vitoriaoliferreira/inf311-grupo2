package com.example.gesuas360.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gesuas360.R;

import java.util.List;

public class DateAdapter extends RecyclerView.Adapter<DateAdapter.ViewHolder> {

    private List<String> dias;
    private int selectedPosition = 0;

    public DateAdapter(List<String> dias) {
        this.dias = dias;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_data, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String dia = dias.get(position);
        holder.tvDia.setText(dia);
        holder.tvMes.setText("Maio");
        
        if (position == selectedPosition) {
            holder.itemView.setBackgroundResource(R.drawable.bg_date_selected);
            holder.tvDia.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.white));
            holder.tvMes.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.white));
        } else {
            holder.itemView.setBackgroundResource(R.drawable.bg_card_rounded);
            holder.tvDia.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.text_dark));
            holder.tvMes.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), android.R.color.darker_gray));
        }
    }

    @Override
    public int getItemCount() {
        return dias.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvDia, tvMes;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDia = itemView.findViewById(R.id.tv_dia);
            tvMes = itemView.findViewById(R.id.tv_mes);
        }
    }
}
