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
import com.example.gesuas360.adapters.PalestranteFavoritoAdapter;
import com.example.gesuas360.adapters.ProgramacaoAdapter;
import com.example.gesuas360.models.DataEvento;
import com.example.gesuas360.models.Palestra;
import com.example.gesuas360.models.Palestrante;
import com.example.gesuas360.repositories.PalestraRepository;
import com.example.gesuas360.repositories.PalestranteRepository;

import java.util.ArrayList;
import java.util.List;

public class ProgramacaoFragment extends BaseFragment {

    private static final String ARG_PALESTRA = "palestra";

    private ProgramacaoAdapter programacaoAdapter;
    private PalestranteFavoritoAdapter favAdapter;
    private View cardFavProg;
    private TextView tvFiltroAtivo;
    private TextView tvLabel;

    private String diaAtual = "";
    private String palestranteFiltro = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_programacao, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        PalestraRepository repo = PalestraRepository.getInstance();
        List<DataEvento> diasDoEvento = repo.getDiasDoEvento();

        tvLabel = view.findViewById(R.id.tv_current_date_label);
        tvFiltroAtivo = view.findViewById(R.id.tv_filtro_ativo);
        cardFavProg = view.findViewById(R.id.card_palestrantes_fav_prog);

        if (!diasDoEvento.isEmpty()) {
            diaAtual = diasDoEvento.get(0).getDia();
            tvLabel.setText(diasDoEvento.get(0).getLabel());
        }

        configurarProgramacao(view, repo);
        configurarDatas(view, repo, diasDoEvento);
        configurarFavoritosSection(view);
    }

    @Override
    public void onResume() {
        super.onResume();
        atualizarSecaoFavoritos();
    }

    private void configurarProgramacao(View view, PalestraRepository repo) {
        programacaoAdapter = new ProgramacaoAdapter(
                repo.getPalestrasByData(diaAtual),
                palestra -> navegarParaDetalhes(view, palestra)
        );

        RecyclerView rvProgramacao = view.findViewById(R.id.rv_programacao);
        rvProgramacao.setLayoutManager(new LinearLayoutManager(getContext()));
        rvProgramacao.setAdapter(programacaoAdapter);
    }

    private void configurarDatas(View view, PalestraRepository repo, List<DataEvento> diasDoEvento) {
        DateAdapter dateAdapter = new DateAdapter(diasDoEvento, data -> {
            diaAtual = data.getDia();
            tvLabel.setText(data.getLabel());
            palestranteFiltro = null;
            if (favAdapter != null) favAdapter.updateData(
                    PalestranteRepository.getInstance().getPalesrantesFavoritos());
            atualizarListaProgramacao(repo);
            atualizarIndicadorFiltro();
        });

        RecyclerView rvDates = view.findViewById(R.id.rv_dates);
        rvDates.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rvDates.setAdapter(dateAdapter);
    }

    private void configurarFavoritosSection(View view) {
        List<Palestrante> favoritos = PalestranteRepository.getInstance().getPalesrantesFavoritos();

        favAdapter = new PalestranteFavoritoAdapter(favoritos, (palestrante, selecionado) -> {
            palestranteFiltro = selecionado ? palestrante.getNome() : null;
            atualizarListaProgramacao(PalestraRepository.getInstance());
            atualizarIndicadorFiltro();
        });

        RecyclerView rvFav = view.findViewById(R.id.rv_fav_prog);
        rvFav.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rvFav.setAdapter(favAdapter);

        cardFavProg.setVisibility(favoritos.isEmpty() ? View.GONE : View.VISIBLE);
    }

    private void atualizarSecaoFavoritos() {
        if (favAdapter == null || cardFavProg == null) return;
        List<Palestrante> favoritos = PalestranteRepository.getInstance().getPalesrantesFavoritos();
        favAdapter.updateData(favoritos);
        palestranteFiltro = null;
        cardFavProg.setVisibility(favoritos.isEmpty() ? View.GONE : View.VISIBLE);
        atualizarListaProgramacao(PalestraRepository.getInstance());
        atualizarIndicadorFiltro();
    }

    private void atualizarListaProgramacao(PalestraRepository repo) {
        if (programacaoAdapter == null) return;
        List<Palestra> base = repo.getPalestrasByData(diaAtual);
        if (palestranteFiltro != null) {
            List<Palestra> filtradas = new ArrayList<>();
            for (Palestra p : base) {
                if (palestranteFiltro.equals(p.getPalestranteNome())) filtradas.add(p);
            }
            programacaoAdapter.updateData(filtradas);
        } else {
            programacaoAdapter.updateData(base);
        }
    }

    private void atualizarIndicadorFiltro() {
        if (tvFiltroAtivo != null) {
            tvFiltroAtivo.setVisibility(palestranteFiltro != null ? View.VISIBLE : View.GONE);
        }
    }

    private void navegarParaDetalhes(View view, Palestra palestra) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_PALESTRA, palestra);
        Navigation.findNavController(view).navigate(R.id.action_programacao_to_detalhes, bundle);
    }

    @Override
    protected String getTitulo() {
        return "Programação";
    }
}
