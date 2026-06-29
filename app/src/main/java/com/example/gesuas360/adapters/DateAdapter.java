package com.example.gesuas360.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gesuas360.R;
import com.example.gesuas360.models.DataEvento;

import java.util.List;

public class DateAdapter extends RecyclerView.Adapter<DateAdapter.ViewHolder> {

    public interface OnDateClickListener {
        void onDateClick(DataEvento data);
    }

    private final List<DataEvento> dias;
    private final OnDateClickListener listener;
    private int selectedPosition = 0;

    public DateAdapter(List<DataEvento> dias, OnDateClickListener listener) {
        this.dias = dias;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_data, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DataEvento data = dias.get(position);
        holder.tvDia.setText(data.getDia());
        holder.tvMes.setText(data.getMes());

        boolean selecionado = (position == selectedPosition);
        holder.layoutData.setSelected(selecionado);

        if (selecionado) {
            holder.tvDia.setTextColor(Color.WHITE);
            holder.tvMes.setTextColor(Color.WHITE);
        } else {
            holder.tvDia.setTextColor(Color.parseColor("#424242"));
            holder.tvMes.setTextColor(Color.parseColor("#888888"));
        }

        holder.itemView.setOnClickListener(v -> {
            int anterior = selectedPosition;
            selectedPosition = holder.getAdapterPosition();
            notifyItemChanged(anterior);
            notifyItemChanged(selectedPosition);
            if (listener != null) listener.onDateClick(data);
        });
    }

    @Override
    public int getItemCount() {
        return dias.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        View layoutData;
        TextView tvDia, tvMes;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            layoutData = itemView.findViewById(R.id.layoutData);
            tvDia = itemView.findViewById(R.id.tv_dia);
            tvMes = itemView.findViewById(R.id.tv_mes);
        }
    }
}
