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
import com.example.gesuas360.adapters.NotificacaoAdapter;
import com.example.gesuas360.models.Notificacao;
import com.example.gesuas360.repositories.NotificacaoRepository;

import java.util.List;

public class NotificacoesFragment extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notificacoes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView rvNotificacoes = view.findViewById(R.id.rvNotificacoes);
        rvNotificacoes.setLayoutManager(new LinearLayoutManager(getContext()));
        
        List<Notificacao> lista = NotificacaoRepository.getInstance().getNotificacoes();
        rvNotificacoes.setAdapter(new NotificacaoAdapter(lista));
    }

    @Override
    protected String getTitulo() {
        return "Notificações";
    }

    @Override
    protected boolean exibirBotaoVoltar() {
        return true;
    }

    @Override
    protected boolean exibirBotaoNotificacoes() {
        return false;
    }
}
