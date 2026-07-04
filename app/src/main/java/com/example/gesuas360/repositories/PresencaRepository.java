package com.example.gesuas360.repositories;

import android.os.Handler;
import android.os.Looper;

import com.example.gesuas360.models.ConfirmacaoPresenca;

public class PresencaRepository {

    private static PresencaRepository instance;

    public interface PresencaCallback {
        void onSuccess(String mensagem);
        void onError(String mensagem);
    }

    public static synchronized PresencaRepository getInstance() {
        if (instance == null) instance = new PresencaRepository();
        return instance;
    }

    /**
     * Quando a API estiver pronta, substituir o corpo deste método por chamada Retrofit/Volley:
     *
     * POST /api/presenca/confirmar
     * Header: Authorization: Bearer {participante.token}
     * Body (JSON):
     * {
     *   "participanteId":    dados.getParticipanteId(),
     *   "participanteNome":  dados.getParticipanteNome(),
     *   "participanteEmail": dados.getParticipanteEmail(),
     *   "palestTitulo":      dados.getPalestTitulo(),
     *   "palestHorario":     dados.getPalestHorario(),
     *   "palestData":        dados.getPalestData(),
     *   "palestLocal":       dados.getPalestLocal(),
     *   "qrToken":           dados.getQrToken()
     * }
     * Resposta 200: { "confirmado": true, "mensagem": "Presença registrada com sucesso!" }
     * Resposta 400: { "erro": "QR code inválido ou expirado." }
     * Resposta 409: { "erro": "Presença já registrada para este participante." }
     */
    public void confirmarPresenca(ConfirmacaoPresenca dados, PresencaCallback callback) {
        if (dados.getQrToken() == null || dados.getQrToken().trim().isEmpty()) {
            callback.onError("QR code inválido. Tente novamente.");
            return;
        }

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            // Mock: qualquer QR não vazio é aceito. A API real validará o token.
            callback.onSuccess("Presença registrada em \"" + dados.getPalestTitulo() + "\"!");
        }, 1200);
    }
}
