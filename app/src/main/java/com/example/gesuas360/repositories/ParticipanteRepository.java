package com.example.gesuas360.repositories;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.Nullable;

import com.example.gesuas360.models.Participante;

public class ParticipanteRepository {

    private static ParticipanteRepository instance;

    public interface AtualizarCallback {
        void onSuccess();
        void onError(String mensagem);
    }

    public static synchronized ParticipanteRepository getInstance() {
        if (instance == null) instance = new ParticipanteRepository();
        return instance;
    }

    /**
     * PUT /api/participante/email
     * Authorization: Bearer {participante.getToken()}
     * Body: { "email": email }
     */
    public void atualizarEmail(String email, Participante participante, AtualizarCallback callback) {
        if (email == null || email.trim().isEmpty()) {
            callback.onError("Informe um e-mail.");
            return;
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches()) {
            callback.onError("E-mail inválido.");
            return;
        }
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            participante.setEmail(email.trim());
            callback.onSuccess();
        }, 800);
    }

    /**
     * PUT /api/participante/telefone
     * Authorization: Bearer {participante.getToken()}
     * Body: { "telefone": telefone }
     */
    public void atualizarTelefone(String telefone, Participante participante, AtualizarCallback callback) {
        String limpo = telefone == null ? "" : telefone.replaceAll("[^0-9]", "");
        if (limpo.length() < 8) {
            callback.onError("Informe um telefone válido.");
            return;
        }
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            participante.setTelefone(telefone.trim());
            callback.onSuccess();
        }, 800);
    }

    /**
     * PUT /api/participante/cidade
     * Authorization: Bearer {participante.getToken()}
     * Body: { "cidade": cidade }
     */
    public void atualizarCidade(String cidade, Participante participante, AtualizarCallback callback) {
        if (cidade == null || cidade.trim().isEmpty()) {
            callback.onError("Informe uma cidade.");
            return;
        }
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            participante.setCidade(cidade.trim());
            callback.onSuccess();
        }, 800);
    }

    /**
     * PUT /api/participante/fcm-token
     * Authorization: Bearer {participante.getToken()}
     * Body: { "fcmToken": fcmToken }
     * Response 200: { "ok": true }
     *
     * A API backend usa este token (junto com a FCM Server Key do projeto Firebase)
     * para enviar push notifications segmentadas por participante.
     * A FCM Server Key NUNCA vai no app — fica somente no backend.
     */
    public void registrarFcmToken(String fcmToken, Participante participante,
                                   @Nullable AtualizarCallback callback) {
        if (callback != null) {
            new Handler(Looper.getMainLooper()).postDelayed(callback::onSuccess, 300);
        }
    }
}
