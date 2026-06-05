package com.example.gesuas360.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gesuas360.R;
import com.example.gesuas360.adapters.DateAdapter;
import com.example.gesuas360.adapters.ProgramacaoAdapter;
import com.example.gesuas360.models.Palestra;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProgramacaoFragment extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_programacao, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView rvProgramacao = view.findViewById(R.id.rv_programacao);
        rvProgramacao.setLayoutManager(new LinearLayoutManager(getContext()));
        rvProgramacao.setAdapter(new ProgramacaoAdapter(getMockData()));

        RecyclerView rvDates = view.findViewById(R.id.rv_dates);
        rvDates.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rvDates.setAdapter(new DateAdapter(Arrays.asList("11", "12", "13", "14")));
    }

    @Override
    protected String getTitulo() {
        return "Programação";
    }

    private List<Palestra> getMockData() {
        List<Palestra> lista = new ArrayList<>();
        lista.add(new Palestra("09:00", "Auditório", "Credenciamento e boas-vindas", 
                "Início das atividades e entrega de materiais.", "", "", false));
        lista.add(new Palestra("10:00", "Auditório", "Abertura oficial do SUAS 360", 
                "Presença de autoridades e apresentação do evento.", "", "", false));
        lista.add(new Palestra("14:00", "Sala A", "Vínculos que Protegem", 
                "Conexões humanas que sustentam a proteção social no SUAS", 
                "Mariana Torres", "Assistente Social, Mestre e Doutora em Serviço Social pela PUC/SP", true));
        lista.add(new Palestra("16:00", "Sala A", "Capacitação em assistência", 
                "Conexões humanas que sustentam a proteção social no SUAS", 
                "Maria Silva", "Assistente Social, Mestre e Doutora em Serviço Social pela PUC/SP", false));
        return lista;
    }
}
