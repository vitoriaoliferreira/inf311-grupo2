package com.example.gesuas360.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gesuas360.R;
import com.example.gesuas360.adapters.ProximaPalestraAdapter;
import com.example.gesuas360.models.Palestra;

import java.util.ArrayList;
import java.util.List;

public class InicioFragment extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_inicio, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        View btnNotif = view.findViewById(R.id.btnNotificacoesDash);
        if (btnNotif != null) {
            btnNotif.setOnClickListener(v -> 
                Navigation.findNavController(v).navigate(R.id.notificacoesFragment));
        }

        View btnVerProgramacao = view.findViewById(R.id.btnVerProgramacao);
        if (btnVerProgramacao != null) {
            btnVerProgramacao.setOnClickListener(v -> {
                com.google.android.material.bottomnavigation.BottomNavigationView bottomNav = 
                    requireActivity().findViewById(R.id.bottom_navigation);
                if (bottomNav != null) {
                    bottomNav.setSelectedItemId(R.id.programacaoFragment);
                } else {
                    Navigation.findNavController(v).navigate(R.id.programacaoFragment);
                }
            });
        }

        RecyclerView rvProximas = view.findViewById(R.id.rvProximasPalestras);
        if (rvProximas != null) {
            rvProximas.setLayoutManager(new LinearLayoutManager(getContext()));
            rvProximas.setAdapter(new ProximaPalestraAdapter(getMockProximas()));
        }
    }

    private List<Palestra> getMockProximas() {
        List<Palestra> list = new ArrayList<>();
        list.add(new Palestra("16:00", "Auditório do CCE", "Vigilância socioassistencial no SUAS", 
                "", "João Pereira", "", false));
        list.add(new Palestra("10:00", "Auditório do CCE", "Universidade GESUAS", 
                "", "José da Silva", "", false));
        return list;
    }

    @Override
    protected String getTitulo() {
        return "Início";
    }
}
