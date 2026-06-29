package com.example.gesuas360.adapters;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.widget.ImageViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gesuas360.R;
import com.example.gesuas360.models.Palestra;

import java.util.List;

public class ProgramacaoAdapter extends RecyclerView.Adapter<ProgramacaoAdapter.ViewHolder> {

    private List<Palestra> palestras;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Palestra palestra);
    }

    public ProgramacaoAdapter(List<Palestra> palestras, OnItemClickListener listener) {
        this.palestras = palestras;
        this.listener = listener;
    }

    public void updateData(List<Palestra> novaLista) {
        this.palestras = novaLista;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_programacao, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Palestra palestra = palestras.get(position);

        holder.tvHorario.setText(palestra.getHorario());
        holder.tvLocal.setText(palestra.getLocal());
        holder.tvTitulo.setText(palestra.getTitulo());

        if (palestra.getDescricao() != null && !palestra.getDescricao().isEmpty()) {
            holder.tvDescricao.setVisibility(View.VISIBLE);
            holder.tvDescricao.setText(palestra.getDescricao());
        } else {
            holder.tvDescricao.setVisibility(View.GONE);
        }

        boolean temPalestrante = palestra.getPalestranteNome() != null
                && !palestra.getPalestranteNome().isEmpty();

        if (temPalestrante) {
            holder.layoutPalestrante.setVisibility(View.VISIBLE);
            holder.tvPalestranteNome.setText(palestra.getPalestranteNome());
            if (holder.tvPalestranteCargo != null) {
                holder.tvPalestranteCargo.setText(palestra.getPalestranteBio());
            }
        } else {
            holder.layoutPalestrante.setVisibility(View.GONE);
        }

        atualizarIconeFavorito(holder.ivBookmark, palestra.isFavorito());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onItemClick(palestra);
        });

        holder.ivBookmark.setOnClickListener(v -> {
            palestra.setFavorito(!palestra.isFavorito());
            atualizarIconeFavorito(holder.ivBookmark, palestra.isFavorito());
        });
    }

    private void atualizarIconeFavorito(ImageView iv, boolean favorito) {
        if (favorito) {
            iv.setImageResource(R.drawable.ic_bookmark);
            ImageViewCompat.setImageTintList(iv, ColorStateList.valueOf(Color.parseColor("#4CAF50")));
            iv.setAlpha(1f);
        } else {
            iv.setImageResource(R.drawable.ic_bookmark_border);
            ImageViewCompat.setImageTintList(iv, ColorStateList.valueOf(Color.parseColor("#888888")));
            iv.setAlpha(1f);
        }
    }

    @Override
    public int getItemCount() {
        return palestras.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvHorario, tvLocal, tvTitulo, tvDescricao, tvPalestranteNome, tvPalestranteCargo;
        View layoutPalestrante;
        ImageView ivBookmark;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvHorario = itemView.findViewById(R.id.tv_horario);
            tvLocal = itemView.findViewById(R.id.tv_local);
            tvTitulo = itemView.findViewById(R.id.tv_titulo);
            tvDescricao = itemView.findViewById(R.id.tv_descricao);
            tvPalestranteNome = itemView.findViewById(R.id.tv_palestrante_nome);
            tvPalestranteCargo = itemView.findViewById(R.id.tv_palestrante_cargo);
            layoutPalestrante = itemView.findViewById(R.id.layoutPalestrante);
            ivBookmark = itemView.findViewById(R.id.iv_bookmark);
        }
    }
}
