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
import com.example.gesuas360.models.Palestra;
import com.example.gesuas360.models.Palestrante;
import com.example.gesuas360.repositories.PalestraRepository;

import java.util.List;

public class DetalhesPalestranteFragment extends BaseFragment {

    public static final String ARG_PALESTRANTE = "palestrante";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detalhes_palestrante, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = getArguments();
        if (args == null) return;

        Palestrante palestrante = (Palestrante) args.getSerializable(ARG_PALESTRANTE);
        if (palestrante == null) return;

        TextView tvNome = view.findViewById(R.id.tvNomeDetalhe);
        TextView tvCargo = view.findViewById(R.id.tvCargoDetalhe);
        TextView tvBio = view.findViewById(R.id.tvBioDetalhe);
        RecyclerView rvPalestras = view.findViewById(R.id.rvPalestrasPalestrante);

        tvNome.setText(palestrante.getNome());
        tvCargo.setText(palestrante.getCargo());
        tvBio.setText(palestrante.getBiografia());

        List<Palestra> palestrasDoFalante = PalestraRepository.getInstance()
                .getPalestrasDoPalestrante(palestrante.getNome());

        if (rvPalestras != null) {
            rvPalestras.setLayoutManager(new LinearLayoutManager(getContext()));
            rvPalestras.setAdapter(new ProgramacaoAdapter(palestrasDoFalante, null));

            TextView tvLabelPalestras = view.findViewById(R.id.tvLabelPalestras);
            if (tvLabelPalestras != null) {
                tvLabelPalestras.setVisibility(palestrasDoFalante.isEmpty() ? View.GONE : View.VISIBLE);
            }
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
