package com.example.gesuas360.views;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.gesuas360.R;
import com.example.gesuas360.repositories.NotificacaoRepository;

public abstract class BaseFragment extends Fragment {

    private TextView tvBadgeNotif;
    private NotificacaoRepository.NotificacoesListener notifListener;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvBadgeNotif = view.findViewById(R.id.tv_badge_notificacoes);
        setupHeader(view);
    }

    @Override
    public void onResume() {
        super.onResume();
        atualizarBadge();
        if (exibirBotaoNotificacoes()) {
            notifListener = this::atualizarBadge;
            NotificacaoRepository.getInstance().addListener(notifListener);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (notifListener != null) {
            NotificacaoRepository.getInstance().removeListener(notifListener);
            notifListener = null;
        }
    }

    private void setupHeader(View view) {
        TextView tvTitulo = view.findViewById(R.id.tvTituloCabecalho);
        if (tvTitulo != null) tvTitulo.setText(getTitulo());

        View btnVoltar = view.findViewById(R.id.btnVoltarCabecalho);
        if (btnVoltar != null) {
            if (exibirBotaoVoltar()) {
                btnVoltar.setVisibility(View.VISIBLE);
                btnVoltar.setOnClickListener(v -> Navigation.findNavController(v).popBackStack());
            } else {
                btnVoltar.setVisibility(View.GONE);
            }
        }

        View wrapperNotif = view.findViewById(R.id.fl_notif_badge_wrapper);
        View btnNotif     = view.findViewById(R.id.btnNotificacoesCabecalho);

        if (exibirBotaoNotificacoes()) {
            // Wrapper recebe click para cobrir a área inteira (incluindo badge)
            if (wrapperNotif != null) {
                wrapperNotif.setVisibility(View.VISIBLE);
                wrapperNotif.setOnClickListener(v ->
                        Navigation.findNavController(v).navigate(R.id.notificacoesFragment));
            }
            if (btnNotif != null) {
                btnNotif.setVisibility(View.VISIBLE);
                btnNotif.setOnClickListener(v ->
                        Navigation.findNavController(v).navigate(R.id.notificacoesFragment));
            }
        } else {
            if (wrapperNotif != null) wrapperNotif.setVisibility(View.GONE);
            if (btnNotif != null) btnNotif.setVisibility(View.GONE);
            if (tvBadgeNotif != null) tvBadgeNotif.setVisibility(View.GONE);
        }
    }

    private void atualizarBadge() {
        if (tvBadgeNotif == null || !exibirBotaoNotificacoes()) return;
        int count = NotificacaoRepository.getInstance().getContadorNaoLidas();
        if (count > 0) {
            tvBadgeNotif.setVisibility(View.VISIBLE);
            tvBadgeNotif.setText(count > 9 ? "9+" : String.valueOf(count));
        } else {
            tvBadgeNotif.setVisibility(View.GONE);
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
