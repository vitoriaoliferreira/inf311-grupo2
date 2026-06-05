package com.example.gesuas360.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gesuas360.R;
import com.example.gesuas360.models.Palestrante;

import java.util.List;

public class PalestranteAdapter extends RecyclerView.Adapter<PalestranteAdapter.ViewHolder> {

    private List<Palestrante> palestrantes;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Palestrante palestrante);
    }

    public PalestranteAdapter(List<Palestrante> palestrantes, OnItemClickListener listener) {
        this.palestrantes = palestrantes;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_palestrante, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Palestrante palestrante = palestrantes.get(position);
        holder.tvNome.setText(palestrante.getNome());
        holder.tvCargo.setText(palestrante.getCargo());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(palestrante);
            }
        });
    }

    @Override
    public int getItemCount() {
        return palestrantes.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNome, tvCargo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNome = itemView.findViewById(R.id.tv_nome_palestrante);
            tvCargo = itemView.findViewById(R.id.tv_cargo_palestrante);
        }
    }
}
