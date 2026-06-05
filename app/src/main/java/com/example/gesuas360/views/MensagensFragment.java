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
import com.example.gesuas360.adapters.MensagemAdapter;
import com.example.gesuas360.models.Mensagem;
import com.example.gesuas360.repositories.MensagemRepository;

import java.util.List;

public class MensagensFragment extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mensagens, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView rvMensagens = view.findViewById(R.id.rvMensagens);
        rvMensagens.setLayoutManager(new LinearLayoutManager(getContext()));

        List<Mensagem> lista = MensagemRepository.getInstance().getMensagens();
        rvMensagens.setAdapter(new MensagemAdapter(lista));
    }

    @Override
    protected String getTitulo() {
        return "Mensagens";
    }
}
