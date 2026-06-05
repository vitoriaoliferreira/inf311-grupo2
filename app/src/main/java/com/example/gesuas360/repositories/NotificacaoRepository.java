package com.example.gesuas360.repositories;

import com.example.gesuas360.models.Notificacao;

import java.util.ArrayList;
import java.util.List;

public class NotificacaoRepository {
    private static NotificacaoRepository instance;
    private final List<Notificacao> notificacoes;

    private NotificacaoRepository() {
        notificacoes = new ArrayList<>();
        notificacoes.add(new Notificacao(
                "Mudança de Horário",
                "A palestra das 15h teve seu horário alterado para às 15:30h",
                "10:30",
                0
        ));
        notificacoes.add(new Notificacao(
                "Você tem mensagens não lidas",
                "10 mensagens não lidas",
                "09:15",
                1
        ));
        notificacoes.add(new Notificacao(
                "Seu comentário ganhou um like do palestrante",
                "Veja todas as reações",
                "Ontem",
                2
        ));
    }

    public static synchronized NotificacaoRepository getInstance() {
        if (instance == null) {
            instance = new NotificacaoRepository();
        }
        return instance;
    }

    public List<Notificacao> getNotificacoes() {
        return notificacoes;
    }
}
