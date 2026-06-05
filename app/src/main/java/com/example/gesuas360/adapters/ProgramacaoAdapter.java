package com.example.gesuas360.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gesuas360.R;
import com.example.gesuas360.models.Palestra;

import java.util.List;

public class ProgramacaoAdapter extends RecyclerView.Adapter<ProgramacaoAdapter.ViewHolder> {

    private List<Palestra> palestras;

    public ProgramacaoAdapter(List<Palestra> palestras) {
        this.palestras = palestras;
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

        if (palestra.getPalestranteNome() != null && !palestra.getPalestranteNome().isEmpty()) {
            holder.layoutPalestrante.setVisibility(View.VISIBLE);
            holder.tvPalestranteNome.setText(palestra.getPalestranteNome());
            holder.tvPalestranteBio.setText(palestra.getPalestranteBio());
        } else {
            holder.layoutPalestrante.setVisibility(View.GONE);
        }

        holder.ivFavorito.setImageResource(palestra.isFavorito() ? 
                android.R.drawable.btn_star_big_on : android.R.drawable.btn_star_big_off);
    }

    @Override
    public int getItemCount() {
        return palestras.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvHorario, tvLocal, tvTitulo, tvDescricao, tvPalestranteNome, tvPalestranteBio;
        View layoutPalestrante;
        ImageView ivFavorito;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvHorario = itemView.findViewById(R.id.tv_horario);
            tvLocal = itemView.findViewById(R.id.tv_local);
            tvTitulo = itemView.findViewById(R.id.tv_titulo);
            tvDescricao = itemView.findViewById(R.id.tv_descricao);
            tvPalestranteNome = itemView.findViewById(R.id.tv_palestrante_nome);
            tvPalestranteBio = itemView.findViewById(R.id.tv_palestrante_bio);
            layoutPalestrante = itemView.findViewById(R.id.ll_palestrante);
            ivFavorito = itemView.findViewById(R.id.iv_favorito);
        }
    }
}
