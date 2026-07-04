package com.example.gesuas360.adapters;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gesuas360.R;
import com.example.gesuas360.models.Notificacao;
import com.example.gesuas360.repositories.NotificacaoRepository;

import java.util.List;

public class NotificacaoAdapter extends RecyclerView.Adapter<NotificacaoAdapter.ViewHolder> {

    private List<Notificacao> notificacoes;

    public NotificacaoAdapter(List<Notificacao> notificacoes) {
        this.notificacoes = notificacoes;
    }

    public void updateData(List<Notificacao> lista) {
        this.notificacoes = lista;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_notificacao, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Notificacao notif = notificacoes.get(position);

        holder.tvTitulo.setText(notif.getTitulo());
        holder.tvCorpo.setText(notif.getCorpo());
        holder.tvHorario.setText(notif.getHorario());

        switch (notif.getTipo()) {
            case Notificacao.TIPO_MENSAGEM:
                holder.ivIcon.setImageResource(R.drawable.ic_chat);
                holder.layoutIcon.setBackgroundTintList(
                        ColorStateList.valueOf(Color.parseColor("#689F38")));
                break;
            case Notificacao.TIPO_PALESTRA:
                holder.ivIcon.setImageResource(R.drawable.ic_star);
                holder.layoutIcon.setBackgroundTintList(
                        ColorStateList.valueOf(Color.parseColor("#15438B")));
                break;
            case Notificacao.TIPO_ALERTA:
                holder.ivIcon.setImageResource(R.drawable.ic_notifications);
                holder.layoutIcon.setBackgroundTintList(
                        ColorStateList.valueOf(Color.parseColor("#E64A19")));
                break;
            default: // TIPO_INFO
                holder.ivIcon.setImageResource(R.drawable.ic_info);
                holder.layoutIcon.setBackgroundTintList(
                        ColorStateList.valueOf(Color.parseColor("#15438B")));
                break;
        }

        if (notif.isLida()) {
            holder.itemView.setBackgroundColor(Color.TRANSPARENT);
            holder.tvTitulo.setTypeface(null, Typeface.NORMAL);
            holder.tvTitulo.setTextColor(Color.parseColor("#000000"));
        } else {
            holder.itemView.setBackgroundColor(Color.parseColor("#F0F4FF"));
            holder.tvTitulo.setTypeface(null, Typeface.BOLD);
            holder.tvTitulo.setTextColor(Color.parseColor("#15438B"));
        }

        holder.itemView.setOnClickListener(v -> {
            if (!notif.isLida()) {
                NotificacaoRepository.getInstance().marcarComoLida(notif.getId());
                int pos = holder.getAdapterPosition();
                if (pos != RecyclerView.NO_ID) notifyItemChanged(pos);
            }
        });
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
            tvTitulo   = itemView.findViewById(R.id.tv_notif_title);
            tvCorpo    = itemView.findViewById(R.id.tv_notif_body);
            tvHorario  = itemView.findViewById(R.id.tv_notif_time);
            ivIcon     = itemView.findViewById(R.id.iv_notif_icon);
            layoutIcon = itemView.findViewById(R.id.fl_notif_icon_container);
        }
    }
}
