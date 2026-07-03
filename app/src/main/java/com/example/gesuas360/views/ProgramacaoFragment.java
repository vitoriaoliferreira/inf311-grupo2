package com.example.gesuas360.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gesuas360.R;
import com.example.gesuas360.adapters.DateAdapter;
import com.example.gesuas360.adapters.ProgramacaoAdapter;
import com.example.gesuas360.models.Palestra;
import com.example.gesuas360.repositories.PalestraRepository;

import java.util.Arrays;
import java.util.List;

public class ProgramacaoFragment extends BaseFragment {
    private TextView tvCurrentDateLabel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_programacao, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvCurrentDateLabel = view.findViewById(R.id.tv_current_date_label);

        RecyclerView rvProgramacao = view.findViewById(R.id.rv_programacao);
        rvProgramacao.setLayoutManager(new LinearLayoutManager(getContext()));

        List<Palestra> lista = PalestraRepository.getInstance().getPalestrasByDay(11);

        ProgramacaoAdapter adapter = new ProgramacaoAdapter(lista, palestra -> {
            Navigation.findNavController(view).navigate(R.id.action_programacao_to_detalhes);
        });

        rvProgramacao.setAdapter(adapter);

        atualizarLabelData(11);

        RecyclerView rvDates = view.findViewById(R.id.rv_dates);
        rvDates.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rvDates.setAdapter(
                new DateAdapter(
                        Arrays.asList("11", "12", "13", "14"),
                        dia -> {
                            // Atualiza a lista de palestras
                            adapter.atualizarPalestras(
                                    PalestraRepository.getInstance().getPalestrasByDay(dia)
                            );
                            atualizarLabelData(dia);
                        }
                )
        );
    }

    private void atualizarLabelData(int dia) {
        if (tvCurrentDateLabel == null) return;
        String data;
        switch (dia) {
            case 11:
                data = "Segunda-feira, 11 de maio";
                break;
            case 12:
                data = "Terça-feira, 12 de maio";
                break;
            case 13:
                data = "Quarta-feira, 13 de maio";
                break;
            case 14:
                data = "Quinta-feira, 14 de maio";
                break;
            default:
                data = "Dia " + dia;
        }
        tvCurrentDateLabel.setText(data);
    }

    @Override
    protected String getTitulo() {
        return "Programação";
    }
}