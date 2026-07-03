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
import com.example.gesuas360.models.Mensagem;

import java.util.List;

public class MensagemAdapter extends RecyclerView.Adapter<MensagemAdapter.ViewHolder> {

    private List<Mensagem> mensagens;

    public MensagemAdapter(List<Mensagem> mensagens) {
        this.mensagens = mensagens;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mensagem, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Mensagem mensagem = mensagens.get(position);
        
        holder.tvCategoria.setText(mensagem.getCategoria());
        holder.tvTitulo.setText(mensagem.getTitulo());
        holder.tvCorpo.setText(mensagem.getCorpo());
        holder.tvHorario.setText(mensagem.getHorario());

        if (mensagem.isAviso()) {
            holder.imgIconeFundo.setImageResource(R.drawable.ic_campaign);
            holder.imgIconeFundo.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#15438B")));
        } else {
            holder.imgIconeFundo.setImageResource(R.drawable.ic_chat);
            holder.imgIconeFundo.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#689F38")));
        }
    }

    @Override
    public int getItemCount() {
        return mensagens.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvCategoria, tvTitulo, tvCorpo, tvHorario;
        ImageView imgIconeFundo;

        public ViewHolder(@NonNull View itemVixew) {
            super(itemVixew);

            tvCategoria = itemVixew.findViewById(R.id.tv_categoria);
            tvTitulo = itemVixew.findViewById(R.id.tv_titulo);
            tvCorpo = itemVixew.findViewById(R.id.tv_corpo);
            tvHorario = itemVixew.findViewById(R.id.tv_horario);
            imgIconeFundo = itemVixew.findViewById(R.id.imgIconeFundo);
        }
    }
}
