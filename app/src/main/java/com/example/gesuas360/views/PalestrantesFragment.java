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
import com.example.gesuas360.adapters.PalestranteAdapter;
import com.example.gesuas360.models.Palestrante;

import java.util.ArrayList;
import java.util.List;

public class PalestrantesFragment extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_palestrantes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView rvPalestrantes = view.findViewById(R.id.rvPalestrantes);
        rvPalestrantes.setLayoutManager(new LinearLayoutManager(getContext()));

        List<Palestrante> mockData = new ArrayList<>();
        mockData.add(new Palestrante("Maria Silva", "Especialista em Gestão Pública"));
        mockData.add(new Palestrante("José da Silva", "Gestor Municipal"));
        mockData.add(new Palestrante("Tânia Maria", "Especialista em Gestão Pública"));
        mockData.add(new Palestrante("Paulo Souza", "Especialista em Gestão Pública"));
        mockData.add(new Palestrante("Abigail Torres", "Especialista em Gestão Pública"));

        rvPalestrantes.setAdapter(new PalestranteAdapter(mockData));
    }

    @Override
    protected String getTitulo() {
        return "Palestrantes";
    }
}
