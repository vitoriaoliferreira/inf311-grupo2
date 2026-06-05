package com.example.gesuas360.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.gesuas360.R;

public class DetalhesPalestraFragment extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detalhes_palestra, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView tvTitulo = view.findViewById(R.id.tvDetalheTitulo);
        TextView tvHorario = view.findViewById(R.id.tvDetalheHorario);
        TextView tvSala = view.findViewById(R.id.tvDetalheSala);
        TextView tvDescricao = view.findViewById(R.id.tvDetalheDescricao);
        TextView tvNomePalestrante = view.findViewById(R.id.tvDetalheNomePalestrante);
        TextView tvCargoPalestrante = view.findViewById(R.id.tvDetalheCargoPalestrante);

        tvTitulo.setText("Vínculos que Protegem");
        tvHorario.setText("14:00");
        tvSala.setText("Sala A");
        tvDescricao.setText("Conexões humanas que sustentam a proteção social no SUAS. " +
                "Nesta palestra, exploraremos como o fortalecimento dos vínculos familiares " +
                "e comunitários é essencial para a eficácia das políticas públicas de assistência social.");
        
        tvNomePalestrante.setText("Mariana Torres");
        tvCargoPalestrante.setText("Assistente Social, Mestre e Doutora em Serviço Social pela PUC/SP");

        View btnConfirmar = view.findViewById(R.id.btnConfirmarPresenca);
        if (btnConfirmar != null) {
            btnConfirmar.setOnClickListener(v -> 
                Toast.makeText(getContext(), "Presença confirmada!", Toast.LENGTH_SHORT).show());
        }
    }

    @Override
    protected String getTitulo() {
        return "Detalhes da Palestra";
    }

    @Override
    protected boolean exibirBotaoVoltar() {
        return true;
    }
}
