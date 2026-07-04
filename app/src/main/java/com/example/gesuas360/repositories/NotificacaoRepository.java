package com.example.gesuas360.repositories;

import android.os.Handler;
import android.os.Looper;

import com.example.gesuas360.models.Notificacao;

import java.util.ArrayList;
import java.util.List;

public class NotificacaoRepository {

    private static NotificacaoRepository instance;
    private final List<Notificacao> notificacoes = new ArrayList<>();
    private final List<NotificacoesListener> listeners = new ArrayList<>();

    public interface NotificacoesListener {
        void onNovaNotificacao();
    }

    private NotificacaoRepository() {
        // Mock inicial — substituir por GET /api/notificacoes ao integrar a API.
        // Authorization: Bearer {participante.token}
        // Response: [ { "id", "titulo", "corpo", "horario", "tipo", "lida", "timestamp" } ]
        notificacoes.add(new Notificacao(
                "Mudança de Horário",
                "A palestra das 15h teve seu horário alterado para às 15:30h.",
                "10:30", Notificacao.TIPO_ALERTA));
        notificacoes.add(new Notificacao(
                "Você tem mensagens não lidas",
                "10 mensagens não lidas.",
                "09:15", Notificacao.TIPO_MENSAGEM));
        notificacoes.add(new Notificacao(
                "Seu comentário ganhou um like",
                "O palestrante reagiu ao seu comentário. Veja todas as reações.",
                "Ontem", Notificacao.TIPO_PALESTRA));
    }

    public static synchronized NotificacaoRepository getInstance() {
        if (instance == null) instance = new NotificacaoRepository();
        return instance;
    }

    // ── Leitura ───────────────────────────────────────────────────────────────

    public List<Notificacao> getNotificacoes() {
        return notificacoes;
    }

    public int getContadorNaoLidas() {
        int count = 0;
        for (Notificacao n : notificacoes) {
            if (!n.isLida()) count++;
        }
        return count;
    }

    // ── Escrita ───────────────────────────────────────────────────────────────

    /** Chamado pelo GeSuasMessagingService ao receber push FCM. */
    public void addNotificacao(Notificacao notificacao) {
        notificacoes.add(0, notificacao);
        notificarListeners();
    }

    /**
     * PUT /api/notificacoes/{id}/lida
     * Authorization: Bearer {participante.token}
     */
    public void marcarComoLida(String id) {
        for (Notificacao n : notificacoes) {
            if (n.getId().equals(id)) {
                n.setLida(true);
                break;
            }
        }
        notificarListeners();
    }

    /**
     * PUT /api/notificacoes/lidas
     * Authorization: Bearer {participante.token}
     * Body: { "todas": true }
     */
    public void marcarTodasComoLidas() {
        for (Notificacao n : notificacoes) {
            n.setLida(true);
        }
        notificarListeners();
    }

    // ── Listeners (badge em tempo real) ──────────────────────────────────────

    public void addListener(NotificacoesListener listener) {
        if (!listeners.contains(listener)) listeners.add(listener);
    }

    public void removeListener(NotificacoesListener listener) {
        listeners.remove(listener);
    }

    private void notificarListeners() {
        new Handler(Looper.getMainLooper()).post(() -> {
            for (NotificacoesListener l : new ArrayList<>(listeners)) {
                l.onNovaNotificacao();
            }
        });
    }
}
