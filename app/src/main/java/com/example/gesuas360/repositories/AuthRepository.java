package com.example.gesuas360.repositories;

import android.os.Handler;
import android.os.Looper;

import com.example.gesuas360.models.Participante;

import java.util.HashMap;
import java.util.Map;

/**
 * Mock do endpoint de autenticação.
 *
 * Endpoint esperado (a implementar):
 *   POST /api/auth/login
 *   Body: { "email": "...", "senha": "..." }
 *   200 OK: { "id": "...", "nome": "...", "email": "...", "token": "..." }
 *   401 Unauthorized: { "mensagem": "Credenciais inválidas." }
 */
public class AuthRepository {

    public interface AuthCallback {
        void onSuccess(Participante participante);
        void onError(String mensagem);
    }

    // Participantes cadastrados no mock (reflete o banco de dados do evento)
    private static final Map<String, String[]> MOCK_PARTICIPANTES = new HashMap<>();

    static {
        // formato: email -> { senha, id, nome }
        MOCK_PARTICIPANTES.put("maria.silva@email.com",   new String[]{"suas2024", "P001", "Maria Silva"});
        MOCK_PARTICIPANTES.put("joao.souza@email.com",    new String[]{"suas2024", "P002", "João Souza"});
        MOCK_PARTICIPANTES.put("ana.costa@prefeitura.gov", new String[]{"gesuas1", "P003", "Ana Costa"});
        MOCK_PARTICIPANTES.put("admin@suas360.com",       new String[]{"admin123", "P000", "Administrador"});
    }

    private static final long DELAY_SIMULADO_MS = 1200;

    /**
     * Simula a chamada POST /api/auth/login.
     * Quando a API estiver pronta, substitua o corpo deste método por um
     * Retrofit/Volley call apontando para o endpoint real.
     */
    public void login(String email, String senha, AuthCallback callback) {
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            String[] dados = MOCK_PARTICIPANTES.get(email.toLowerCase().trim());

            if (dados == null) {
                callback.onError("E-mail não encontrado.");
                return;
            }

            if (!dados[0].equals(senha)) {
                callback.onError("Senha incorreta.");
                return;
            }

            // Simula o token JWT que viria da API
            String tokenSimulado = "mock-jwt-token-" + dados[1];
            Participante participante = new Participante(dados[1], dados[2], email, tokenSimulado);
            callback.onSuccess(participante);

        }, DELAY_SIMULADO_MS);
    }
}
