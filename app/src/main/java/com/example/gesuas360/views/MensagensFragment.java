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

import java.util.ArrayList;
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

        List<Mensagem> mockData = new ArrayList<>();
        mockData.add(new Mensagem("AVISO", "Bem vindos ao SUAS 360!", 
                "Sejam todos muito bem-vindos ao SUAS 360. Aproveitem cada momento!", "10:30", true));
        mockData.add(new Mensagem("CHAT - ORGANIZAÇÃO", "Dúvidas Gerais", 
                "Olá! Em caso de dúvidas, estamos à disposição neste chat.", "10:30", false));

        rvMensagens.setAdapter(new MensagemAdapter(mockData));
    }

    @Override
    protected String getTitulo() {
        return "Mensagens";
    }
}
