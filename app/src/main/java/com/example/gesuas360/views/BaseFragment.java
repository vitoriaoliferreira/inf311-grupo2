package com.example.gesuas360.views;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.gesuas360.R;

public abstract class BaseFragment extends Fragment {

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupHeader(view);
    }

    private void setupHeader(View view) {
        TextView tvTitulo = view.findViewById(R.id.tvTituloCabecalho);
        if (tvTitulo != null) {
            tvTitulo.setText(getTitulo());
        }

        View btnVoltar = view.findViewById(R.id.btnVoltarCabecalho);
        if (btnVoltar != null) {
            if (exibirBotaoVoltar()) {
                btnVoltar.setVisibility(View.VISIBLE);
                btnVoltar.setOnClickListener(v -> Navigation.findNavController(v).popBackStack());
            } else {
                btnVoltar.setVisibility(View.GONE);
            }
        }

        View btnNotif = view.findViewById(R.id.btnNotificacoesCabecalho);
        if (btnNotif != null) {
            if (exibirBotaoNotificacoes()) {
                btnNotif.setVisibility(View.VISIBLE);
                btnNotif.setOnClickListener(v -> 
                    Navigation.findNavController(v).navigate(R.id.notificacoesFragment));
            } else {
                btnNotif.setVisibility(View.GONE);
            }
        }
    }

    protected abstract String getTitulo();

    protected boolean exibirBotaoVoltar() {
        return false;
    }

    protected boolean exibirBotaoNotificacoes() {
        return true;
    }
}
