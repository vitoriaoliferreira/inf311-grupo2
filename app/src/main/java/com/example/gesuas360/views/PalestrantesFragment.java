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
import com.example.gesuas360.repositories.PalestranteRepository;
import androidx.navigation.Navigation;
// IMPORT NECESSÁRIO PARA A NAVEGAÇÃO SEGURA
import androidx.navigation.fragment.NavHostFragment;

import java.util.List;

public class PalestrantesFragment extends BaseFragment {

    private RecyclerView rvPalestrantes;
    private PalestranteAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_palestrantes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvPalestrantes = view.findViewById(R.id.rvPalestrantes);
        rvPalestrantes.setLayoutManager(new LinearLayoutManager(getContext()));
        configurarAdapter();

        View cvFavoritosFooter = view.findViewById(R.id.cv_favoritos_footer);
        cvFavoritosFooter.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_palestrantes_to_favoritos);
        });
    }

    private void configurarAdapter() {
        List<Palestrante> todosPalestrantes = PalestranteRepository.getInstance().getPalestrantes();

        adapter = new PalestranteAdapter(
                todosPalestrantes,
                palestrante -> {
                    Bundle caixaDeDados = new Bundle();
                    caixaDeDados.putString("nomePalestrante", palestrante.getNome());

                    // CORREÇÃO: Navegação segura usando o contexto do próprio Fragment
                    NavHostFragment.findNavController(this)
                            .navigate(R.id.action_palestrantes_to_detalhes, caixaDeDados);
                },
                (palestrante, position) -> {
                    PalestranteRepository.getInstance().toggleFavorito(palestrante);
                    adapter.updateList(PalestranteRepository.getInstance().getPalestrantes());
                }
        );

        // CORREÇÃO CRÍTICA: Faltava setar o adapter no RecyclerView!
        rvPalestrantes.setAdapter(adapter);
    }

    @Override
    protected String getTitulo() {
        return "Palestrantes";
    }
}