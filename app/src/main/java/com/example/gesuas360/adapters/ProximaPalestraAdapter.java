package com.example.gesuas360.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gesuas360.R;
import com.example.gesuas360.models.Palestra;

import java.util.List;

public class ProximaPalestraAdapter extends RecyclerView.Adapter<ProximaPalestraAdapter.ViewHolder> {

    private List<Palestra> palestras;

    public ProximaPalestraAdapter(List<Palestra> palestras) {
        this.palestras = palestras;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_proxima_palestra, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Palestra palestra = palestras.get(position);
        holder.tvHora.setText(palestra.getHorario());
        holder.tvData.setText("18/05");
        holder.tvTitulo.setText(palestra.getTitulo());
        holder.tvLocal.setText(palestra.getLocal());
        holder.tvPalestrante.setText(palestra.getPalestranteNome());
    }

    @Override
    public int getItemCount() {
        return palestras.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvHora, tvData, tvTitulo, tvLocal, tvPalestrante;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvHora = itemView.findViewById(R.id.tv_hora_proxima);
            tvData = itemView.findViewById(R.id.tv_data_proxima);
            tvTitulo = itemView.findViewById(R.id.tv_titulo_proxima);
            tvLocal = itemView.findViewById(R.id.tv_local_proxima);
            tvPalestrante = itemView.findViewById(R.id.tv_palestrante_proxima);
        }
    }
}
