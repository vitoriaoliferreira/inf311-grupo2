package com.example.gesuas360.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gesuas360.R;
import com.example.gesuas360.adapters.ProgramacaoAdapter;
import com.example.gesuas360.repositories.PalestraRepository;

public class DetalhesPalestranteFragment extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detalhes_palestrante, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Mapeamento dos componentes de UI
        TextView tvNome = view.findViewById(R.id.tvNomeDetalhe);
        TextView tvCargo = view.findViewById(R.id.tvCargoDetalhe);
        TextView tvBio = view.findViewById(R.id.tvBioDetalhe);
        RecyclerView rvPalestras = view.findViewById(R.id.rvPalestrasPalestrante);

        // Populando com Mock Data (Simulando o primeiro palestrante)
        tvNome.setText("Maria Silva");
        tvCargo.setText("Especialista em Gestão Pública");
        tvBio.setText("Maria Silva é mestre em Políticas Públicas e possui mais de 15 anos de atuação no SUAS. " +
                "Tem focado suas pesquisas na eficiência da gestão municipal e no impacto social dos programas de transferência de renda.");

        // Configuração da lista de palestras vinculadas ao palestrante
        if (rvPalestras != null) {
            rvPalestras.setLayoutManager(new LinearLayoutManager(getContext()));
            // Mostrando apenas a palestra que ela ministra (a 3ª do repositório)
            rvPalestras.setAdapter(new ProgramacaoAdapter(
                    PalestraRepository.getInstance().getPalestras().subList(2, 3), 
                    null // Sem clique interno para evitar recursão infinita nesta demo
            ));
        }
    }

    @Override
    protected String getTitulo() {
        return "Detalhes do Palestrante";
    }

    @Override
    protected boolean exibirBotaoVoltar() {
        return true;
    }
}
