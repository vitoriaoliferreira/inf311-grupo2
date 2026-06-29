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
import com.example.gesuas360.adapters.PalestranteFavoritoAdapter;
import com.example.gesuas360.models.Palestrante;
import com.example.gesuas360.repositories.PalestranteRepository;

import java.util.List;

public class PalestrantesFragment extends BaseFragment {

    public static final String ARG_PALESTRANTE = "palestrante";

    private PalestranteFavoritoAdapter favAdapter;
    private View cardFavoritos;
    private TextView tvContagemPalestrantes;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_palestrantes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        PalestranteRepository repo = PalestranteRepository.getInstance();
        List<Palestrante> todos = repo.getPalestrantes();

        tvContagemPalestrantes = view.findViewById(R.id.tv_contagem_palestrantes);
        tvContagemPalestrantes.setText(todos.size() + " palestrantes");

        configurarListaPrincipal(view, todos);
        configurarSecaoFavoritos(view, repo);
        configurarFooter(view);
    }

    @Override
    public void onResume() {
        super.onResume();
        PalestranteRepository repo = PalestranteRepository.getInstance();
        List<Palestrante> favoritos = repo.getPalesrantesFavoritos();
        if (favAdapter != null) favAdapter.updateData(favoritos);
        if (cardFavoritos != null) {
            cardFavoritos.setVisibility(favoritos.isEmpty() ? View.GONE : View.VISIBLE);
        }
    }

    private void configurarListaPrincipal(View view, List<Palestrante> todos) {
        PalestranteAdapter adapter = new PalestranteAdapter(todos, palestrante -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable(ARG_PALESTRANTE, palestrante);
            Navigation.findNavController(view).navigate(R.id.action_palestrantes_to_detalhes, bundle);
        });

        RecyclerView rvPalestrantes = view.findViewById(R.id.rvPalestrantes);
        rvPalestrantes.setLayoutManager(new LinearLayoutManager(getContext()));
        rvPalestrantes.setAdapter(adapter);
    }

    private void configurarSecaoFavoritos(View view, PalestranteRepository repo) {
        cardFavoritos = view.findViewById(R.id.card_palestrantes_favoritos);
        List<Palestrante> favoritos = repo.getPalesrantesFavoritos();

        favAdapter = new PalestranteFavoritoAdapter(favoritos, (palestrante, selecionado) -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable(ARG_PALESTRANTE, palestrante);
            Navigation.findNavController(view).navigate(R.id.action_palestrantes_to_detalhes, bundle);
        });

        RecyclerView rvFav = view.findViewById(R.id.rv_palestrantes_favoritos);
        rvFav.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rvFav.setAdapter(favAdapter);

        cardFavoritos.setVisibility(favoritos.isEmpty() ? View.GONE : View.VISIBLE);

        view.findViewById(R.id.tv_ver_todos_favoritos).setOnClickListener(v ->
                Navigation.findNavController(view).navigate(R.id.action_palestrantes_to_favoritos));
    }

    private void configurarFooter(View view) {
        view.findViewById(R.id.cv_favoritos_footer).setOnClickListener(v ->
                Navigation.findNavController(view).navigate(R.id.action_palestrantes_to_favoritos));
    }

    @Override
    protected String getTitulo() {
        return "Palestrantes";
    }
}
