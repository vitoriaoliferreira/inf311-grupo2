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
import com.example.gesuas360.adapters.PalestranteAdapter;
import com.example.gesuas360.models.Palestrante;
import com.example.gesuas360.repositories.PalestranteRepository;

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

        List<Palestrante> lista = PalestranteRepository.getInstance().getPalestrantes();
        
        PalestranteAdapter adapter = new PalestranteAdapter(lista, palestrante -> {
            Navigation.findNavController(view).navigate(R.id.action_palestrantes_to_detalhes);
        });
        
        rvPalestrantes.setAdapter(adapter);
    }

    @Override
    protected String getTitulo() {
        return "Palestrantes";
    }
}
