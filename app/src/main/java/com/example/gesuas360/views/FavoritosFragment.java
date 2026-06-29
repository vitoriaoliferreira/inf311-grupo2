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
import com.example.gesuas360.adapters.PalestranteAdapter;
import com.example.gesuas360.adapters.ProgramacaoAdapter;
import com.example.gesuas360.models.Palestra;
import com.example.gesuas360.models.Palestrante;
import com.example.gesuas360.repositories.PalestraRepository;
import com.example.gesuas360.repositories.PalestranteRepository;

import java.util.List;

public class FavoritosFragment extends BaseFragment {

    private ProgramacaoAdapter palestrasAdapter;
    private PalestranteAdapter palestrantesAdapter;
    private TextView tvEmptyPalestras;
    private TextView tvEmptyPalestrantes;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favoritos, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvEmptyPalestras = view.findViewById(R.id.tv_empty_palestras);
        tvEmptyPalestrantes = view.findViewById(R.id.tv_empty_palestrantes);

        configurarPalestras(view);
        configurarPalestrantes(view);
    }

    @Override
    public void onResume() {
        super.onResume();
        atualizarListas();
    }

    private void configurarPalestras(View view) {
        List<Palestra> favoritas = PalestraRepository.getInstance().getPalestrasFavoritas();

        palestrasAdapter = new ProgramacaoAdapter(favoritas, palestra -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable("palestra", palestra);
            Navigation.findNavController(view).navigate(R.id.action_favoritos_to_detalhes_palestra, bundle);
        });

        RecyclerView rv = view.findViewById(R.id.rv_palestras_favoritas);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setAdapter(palestrasAdapter);

        atualizarEstadoVazio(tvEmptyPalestras, favoritas.isEmpty());
    }

    private void configurarPalestrantes(View view) {
        List<Palestrante> favoritos = PalestranteRepository.getInstance().getPalesrantesFavoritos();

        palestrantesAdapter = new PalestranteAdapter(favoritos, palestrante -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable("palestrante", palestrante);
            Navigation.findNavController(view).navigate(R.id.action_favoritos_to_detalhes_palestrante, bundle);
        });

        RecyclerView rv = view.findViewById(R.id.rv_palestrantes_favoritos);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setAdapter(palestrantesAdapter);

        atualizarEstadoVazio(tvEmptyPalestrantes, favoritos.isEmpty());
    }

    private void atualizarListas() {
        if (palestrasAdapter == null || palestrantesAdapter == null) return;

        List<Palestra> favoritas = PalestraRepository.getInstance().getPalestrasFavoritas();
        palestrasAdapter.updateData(favoritas);
        atualizarEstadoVazio(tvEmptyPalestras, favoritas.isEmpty());

        List<Palestrante> favoritos = PalestranteRepository.getInstance().getPalesrantesFavoritos();
        palestrantesAdapter.updateData(favoritos);
        atualizarEstadoVazio(tvEmptyPalestrantes, favoritos.isEmpty());
    }

    private void atualizarEstadoVazio(TextView tvEmpty, boolean listaVazia) {
        tvEmpty.setVisibility(listaVazia ? View.VISIBLE : View.GONE);
    }

    @Override
    protected String getTitulo() {
        return "Meus Favoritos";
    }
}
