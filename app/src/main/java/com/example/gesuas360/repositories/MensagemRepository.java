package com.example.gesuas360.repositories;

import com.example.gesuas360.models.Mensagem;

import java.util.ArrayList;
import java.util.List;

public class MensagemRepository {
    private static MensagemRepository instance;
    private final List<Mensagem> mensagens;

    private MensagemRepository() {
        mensagens = new ArrayList<>();
        mensagens.add(new Mensagem(
                "AVISO",
                "Bem vindos ao SUAS 360!",
                "Sejam todos muito bem-vindos ao SUAS 360. Aproveitem cada momento!",
                "10:30",
                true
        ));
        mensagens.add(new Mensagem(
                "CHAT - ORGANIZAÇÃO",
                "Dúvidas Gerais",
                "Olá! Em caso de dúvidas, estamos à disposição neste chat.",
                "10:30",
                false
        ));
    }

    public static synchronized MensagemRepository getInstance() {
        if (instance == null) {
            instance = new MensagemRepository();
        }
        return instance;
    }

    public List<Mensagem> getMensagens() {
        return mensagens;
    }
}
