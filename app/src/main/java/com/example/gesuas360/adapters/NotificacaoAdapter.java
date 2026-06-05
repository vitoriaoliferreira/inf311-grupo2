package com.example.gesuas360.adapters;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gesuas360.R;
import com.example.gesuas360.models.Notificacao;

import java.util.List;

public class NotificacaoAdapter extends RecyclerView.Adapter<NotificacaoAdapter.ViewHolder> {

    private List<Notificacao> notificacoes;

    public NotificacaoAdapter(List<Notificacao> notificacoes) {
        this.notificacoes = notificacoes;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notificacao, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Notificacao notificacao = notificacoes.get(position);

        holder.tvTitulo.setText(notificacao.getTitulo());
        holder.tvCorpo.setText(notificacao.getCorpo());
        holder.tvHorario.setText(notificacao.getHorario());

        switch (notificacao.getTipo()) {
            case 0:
                holder.ivIcon.setImageResource(R.drawable.ic_info);
                holder.layoutIcon.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#15438B")));
                break;
            case 1:
                holder.ivIcon.setImageResource(R.drawable.ic_chat);
                holder.layoutIcon.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#689F38")));
                break;
            case 2:
                holder.ivIcon.setImageResource(R.drawable.ic_star);
                holder.layoutIcon.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#15438B")));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return notificacoes.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitulo, tvCorpo, tvHorario;
        ImageView ivIcon;
        View layoutIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitulo = itemView.findViewById(R.id.tv_notif_title);
            tvCorpo = itemView.findViewById(R.id.tv_notif_body);
            tvHorario = itemView.findViewById(R.id.tv_notif_time);
            ivIcon = itemView.findViewById(R.id.iv_notif_icon);
            layoutIcon = itemView.findViewById(R.id.fl_notif_icon_container);
        }
    }
}
