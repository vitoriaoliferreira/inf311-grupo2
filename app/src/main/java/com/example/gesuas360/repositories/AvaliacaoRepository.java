package com.example.gesuas360.repositories;

import android.os.Handler;
import android.os.Looper;

import com.example.gesuas360.models.Avaliacao;

/**
 * Envio das avaliações de palestra/palestrante feitas ao final da palestra.
 *
 * Endpoint (ver README, seção 4.9):
 *   POST /api/avaliacoes → enviarAvaliacao()
 */
public class AvaliacaoRepository {

    private static AvaliacaoRepository instance;

    public interface AvaliacaoCallback {
        void onSuccess(String mensagem);
        void onError(String mensagem);
    }

    public static synchronized AvaliacaoRepository getInstance() {
        if (instance == null) instance = new AvaliacaoRepository();
        return instance;
    }

    /**
     * POST /api/avaliacoes
     * Header: Authorization: Bearer {participante.token}
     * Body (JSON):
     * {
     *   "participanteId":   dados.getParticipanteId(),
     *   "participanteNome": dados.getParticipanteNome(),
     *   "palestraTitulo":   dados.getPalestraTitulo(),
     *   "palestraData":     dados.getPalestraData(),
     *   "palestranteNome":  dados.getPalestranteNome(),
     *   "notaPalestra":     dados.getNotaPalestra(),      // 1..5
     *   "notaPalestrante":  dados.getNotaPalestrante(),   // 1..5 (0 = sem palestrante)
     *   "comentario":       dados.getComentario()
     * }
     * Response 200: { "ok": true, "mensagem": "Avaliação registrada. Obrigado!" }
     * Response 409: { "erro": "Você já avaliou esta palestra." }
     *
     * Quando a API estiver pronta, substituir o corpo por chamada Retrofit/Volley,
     * mantendo as chamadas callback.onSuccess() / callback.onError().
     */
    public void enviarAvaliacao(Avaliacao dados, AvaliacaoCallback callback) {
        if (dados.getNotaPalestra() < 1) {
            callback.onError("Dê uma nota para a palestra antes de enviar.");
            return;
        }

        new Handler(Looper.getMainLooper()).postDelayed(() ->
                        callback.onSuccess("Avaliação registrada. Obrigado pelo seu feedback!"),
                900);
    }
}
