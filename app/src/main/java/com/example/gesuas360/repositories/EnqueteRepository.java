package com.example.gesuas360.repositories;

import android.os.Handler;
import android.os.Looper;

import com.example.gesuas360.models.Enquete;
import com.example.gesuas360.models.OpcaoEnquete;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Fonte de dados das enquetes do evento.
 *
 * As enquetes são vinculadas às palestras pelo título ({@link Enquete#getPalestraTitulo()}).
 * Enquanto a API não estiver integrada, os dados abaixo são mockados.
 *
 * Endpoints (ver README, seção 4.8):
 *   GET  /api/enquetes                          → getEnquetesAtivas()
 *   GET  /api/palestras/{palestraId}/enquetes   → getEnquetesByPalestra()
 *   POST /api/enquetes/{enqueteId}/votar        → votar()
 */
public class EnqueteRepository {

    private static EnqueteRepository instance;
    private final List<Enquete> enquetes = new ArrayList<>();

    public interface EnqueteCallback {
        void onSuccess(String mensagem);
        void onError(String mensagem);
    }

    private EnqueteRepository() {
        // Mock inicial — substituir por GET /api/enquetes ao integrar a API.
        // Response: [ { "id", "palestraTitulo", "pergunta", "encerraEm", "ativa",
        //               "jaVotou", "opcaoVotadaId",
        //               "opcoes": [ { "id", "texto", "votos" } ] } ]

        enquetes.add(new Enquete(
                "ENQ001", "Vínculos que Protegem",
                "Qual aspecto dos vínculos comunitários você considera mais urgente?",
                new ArrayList<>(Arrays.asList(
                        new OpcaoEnquete("OP1", "Rede de proteção familiar", 12),
                        new OpcaoEnquete("OP2", "Articulação territorial", 8),
                        new OpcaoEnquete("OP3", "Participação social", 5))),
                "Encerra em 1h 20m", true));

        enquetes.add(new Enquete(
                "ENQ002", "O SUAS e os Desafios Contemporâneos",
                "Qual o maior desafio da gestão descentralizada do SUAS?",
                new ArrayList<>(Arrays.asList(
                        new OpcaoEnquete("OP1", "Financiamento", 20),
                        new OpcaoEnquete("OP2", "Capacitação de equipes", 15),
                        new OpcaoEnquete("OP3", "Integração de sistemas", 9))),
                "Encerra em 2h 15m", true));

        // Enquete geral (não vinculada a uma palestra específica).
        enquetes.add(new Enquete(
                "ENQ003", null,
                "Qual tema você mais deseja se aprofundar?",
                new ArrayList<>(Arrays.asList(
                        new OpcaoEnquete("OP1", "Proteção social básica", 30),
                        new OpcaoEnquete("OP2", "Proteção social especial", 22),
                        new OpcaoEnquete("OP3", "Gestão e financiamento", 18),
                        new OpcaoEnquete("OP4", "Vigilância socioassistencial", 11))),
                "Encerra em 2h 15m", true));
    }

    public static synchronized EnqueteRepository getInstance() {
        if (instance == null) instance = new EnqueteRepository();
        return instance;
    }

    // ── Leitura ─────────────────────────────────────────────────────────────────

    /** GET /api/enquetes — todas as enquetes ativas do evento. */
    public List<Enquete> getEnquetesAtivas() {
        List<Enquete> ativas = new ArrayList<>();
        for (Enquete e : enquetes) {
            if (e.isAtiva()) ativas.add(e);
        }
        return ativas;
    }

    /**
     * GET /api/palestras/{palestraId}/enquetes — enquetes ativas de uma palestra.
     * O vínculo é feito pelo título da palestra (ver {@link Enquete}).
     */
    public List<Enquete> getEnquetesByPalestra(String palestraTitulo) {
        List<Enquete> resultado = new ArrayList<>();
        if (palestraTitulo == null) return resultado;
        for (Enquete e : enquetes) {
            if (e.isAtiva() && palestraTitulo.equals(e.getPalestraTitulo())) {
                resultado.add(e);
            }
        }
        return resultado;
    }

    public Enquete getEnqueteById(String enqueteId) {
        for (Enquete e : enquetes) {
            if (e.getId().equals(enqueteId)) return e;
        }
        return null;
    }

    // ── Escrita ─────────────────────────────────────────────────────────────────

    /**
     * POST /api/enquetes/{enqueteId}/votar
     * Header: Authorization: Bearer {participante.token}
     * Body:   { "opcaoId": "OP1" }
     * Response 200: { "ok": true, "totalVotos": 26 }
     * Response 409: { "erro": "Participante já votou nesta enquete." }
     *
     * O mock aplica o voto localmente para refletir o resultado na hora.
     */
    public void votar(String enqueteId, String opcaoId, EnqueteCallback callback) {
        Enquete enquete = getEnqueteById(enqueteId);
        if (enquete == null) {
            callback.onError("Enquete não encontrada.");
            return;
        }
        if (enquete.isJaVotou()) {
            callback.onError("Você já votou nesta enquete.");
            return;
        }

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            for (OpcaoEnquete o : enquete.getOpcoes()) {
                if (o.getId().equals(opcaoId)) {
                    o.incrementarVoto();
                    enquete.setJaVotou(true);
                    enquete.setOpcaoVotadaId(opcaoId);
                    callback.onSuccess("Voto registrado. Obrigado por participar!");
                    return;
                }
            }
            callback.onError("Opção inválida.");
        }, 600);
    }
}
