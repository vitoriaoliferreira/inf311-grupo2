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
import com.example.gesuas360.adapters.NotificacaoAdapter;
import com.example.gesuas360.repositories.NotificacaoRepository;

public class NotificacoesFragment extends BaseFragment {

    private NotificacaoAdapter adapter;
    private TextView tvEmpty;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notificacoes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvEmpty = view.findViewById(R.id.tv_empty_notificacoes);

        RecyclerView rv = view.findViewById(R.id.rvNotificacoes);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new NotificacaoAdapter(NotificacaoRepository.getInstance().getNotificacoes());
        rv.setAdapter(adapter);

        view.findViewById(R.id.btn_read_all_notif).setOnClickListener(v -> {
            NotificacaoRepository.getInstance().marcarTodasComoLidas();
            adapter.notifyDataSetChanged();
            atualizarEstadoVazio();
        });

        atualizarEstadoVazio();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adapter != null) {
            adapter.updateData(NotificacaoRepository.getInstance().getNotificacoes());
            atualizarEstadoVazio();
        }
    }

    private void atualizarEstadoVazio() {
        if (tvEmpty == null) return;
        boolean vazio = NotificacaoRepository.getInstance().getNotificacoes().isEmpty();
        tvEmpty.setVisibility(vazio ? View.VISIBLE : View.GONE);
    }

    @Override
    protected String getTitulo() { return "Notificações"; }

    @Override
    protected boolean exibirBotaoVoltar() { return true; }

    @Override
    protected boolean exibirBotaoNotificacoes() { return false; }
}
