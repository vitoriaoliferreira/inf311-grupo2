package com.example.gesuas360.adapters;

import android.content.Context; // Import adicionado
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView; // Import adicionado
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat; // Import adicionado
import androidx.recyclerview.widget.RecyclerView;

import com.example.gesuas360.R;
import com.example.gesuas360.models.Palestrante;

import java.util.List;

public class PalestranteAdapter extends RecyclerView.Adapter<PalestranteAdapter.ViewHolder> {

    private List<Palestrante> palestrantes;
    private OnItemClickListener listener;
    private OnFavoritoClickListener favoritoListener;

    public interface OnItemClickListener {
        void onItemClick(Palestrante palestrante);
    }

    public interface OnFavoritoClickListener {
        void onFavoritoClick(Palestrante palestrante, int position);
    }

    public PalestranteAdapter(List<Palestrante> palestrantes, OnItemClickListener listener, OnFavoritoClickListener favoritoListener) {
        this.palestrantes = palestrantes;
        this.listener = listener;
        this.favoritoListener = favoritoListener;
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

        Context context = holder.itemView.getContext();

        // atualiza ícone favorito
        if (palestrante.isFavorito()) {
            holder.ivFavorito.setImageResource(R.drawable.ic_bookmark);
            holder.ivFavorito.setColorFilter(Color.parseColor("#4CAF50"));
        } else {
            holder.ivFavorito.setImageResource(R.drawable.ic_bookmark_border);
            holder.ivFavorito.setColorFilter(Color.parseColor("#9E9E9E"));
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(palestrante);
            }
        });

        holder.ivFavorito.setOnClickListener(v -> {
            if (favoritoListener != null) {
                favoritoListener.onFavoritoClick(palestrante, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return palestrantes.size();
    }

    // Método para atualizar a lista (usado quando o filtro mudar)
    public void updateList(List<Palestrante> novaLista) {
        this.palestrantes = novaLista;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNome, tvCargo;
        ImageView ivFavorito;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNome = itemView.findViewById(R.id.tv_nome_palestrante);
            tvCargo = itemView.findViewById(R.id.tv_cargo_palestrante);
            ivFavorito = itemView.findViewById(R.id.iv_favorito_palestrante);
        }
    }
}