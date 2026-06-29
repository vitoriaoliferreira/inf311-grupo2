package com.example.gesuas360.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gesuas360.R;
import com.example.gesuas360.models.Palestrante;

import java.util.List;

public class PalestranteFavoritoAdapter extends RecyclerView.Adapter<PalestranteFavoritoAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(Palestrante palestrante, boolean selecionado);
    }

    private List<Palestrante> palestrantes;
    private final OnItemClickListener listener;
    private int selectedPosition = -1;

    public PalestranteFavoritoAdapter(List<Palestrante> palestrantes, OnItemClickListener listener) {
        this.palestrantes = palestrantes;
        this.listener = listener;
    }

    public void updateData(List<Palestrante> novaLista) {
        this.palestrantes = novaLista;
        selectedPosition = -1;
        notifyDataSetChanged();
    }

    @Nullable
    public Palestrante getSelecionado() {
        if (selectedPosition >= 0 && selectedPosition < palestrantes.size()) {
            return palestrantes.get(selectedPosition);
        }
        return null;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_palestrante_compact, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Palestrante palestrante = palestrantes.get(position);
        holder.tvNome.setText(palestrante.getNome());

        boolean selecionado = position == selectedPosition;
        if (selecionado) {
            holder.container.setBackgroundResource(R.drawable.bg_palestrante_compact_selecionado);
            holder.tvNome.setTextColor(Color.parseColor("#2E7D32"));
        } else {
            holder.container.setBackground(null);
            holder.tvNome.setTextColor(Color.parseColor("#212121"));
        }

        holder.itemView.setOnClickListener(v -> {
            int anterior = selectedPosition;
            if (selectedPosition == position) {
                selectedPosition = -1;
            } else {
                selectedPosition = position;
            }
            notifyItemChanged(anterior);
            notifyItemChanged(position);
            if (listener != null) listener.onItemClick(palestrante, selectedPosition == position);
        });
    }

    @Override
    public int getItemCount() {
        return palestrantes.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        View container;
        TextView tvNome;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            container = itemView.findViewById(R.id.container_compact);
            tvNome = itemView.findViewById(R.id.tv_nome_compact);
        }
    }
}
