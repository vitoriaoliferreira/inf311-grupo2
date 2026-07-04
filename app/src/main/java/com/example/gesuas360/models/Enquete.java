package com.example.gesuas360.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Enquete vinculada a uma palestra (ou geral, quando {@code palestraTitulo} é nulo).
 *
 * O vínculo com a palestra é feito por {@code palestraTitulo} — o mesmo padrão de
 * ligação por texto usado no restante do app (ex.: palestrante ↔ palestra por nome).
 * Quando o backend expuser um identificador estável de palestra, este campo passa a
 * carregar o {@code palestraId}.
 */
public class Enquete implements Serializable {

    private final String id;
    private final String palestraTitulo;
    private final String pergunta;
    private final List<OpcaoEnquete> opcoes;
    private final String encerraEm;   // rótulo pronto para exibição, ex.: "Encerra em 2h 15m"

    private boolean ativa;
    private boolean jaVotou;
    private String  opcaoVotadaId;

    public Enquete(String id, String palestraTitulo, String pergunta,
                   List<OpcaoEnquete> opcoes, String encerraEm, boolean ativa) {
        this.id             = id != null ? id : UUID.randomUUID().toString();
        this.palestraTitulo = palestraTitulo;
        this.pergunta       = pergunta;
        this.opcoes         = opcoes != null ? opcoes : new ArrayList<>();
        this.encerraEm      = encerraEm;
        this.ativa          = ativa;
    }

    public String getId()                    { return id; }
    public String getPalestraTitulo()        { return palestraTitulo; }
    public String getPergunta()              { return pergunta; }
    public List<OpcaoEnquete> getOpcoes()    { return opcoes; }
    public String getEncerraEm()             { return encerraEm; }
    public boolean isAtiva()                 { return ativa; }
    public boolean isJaVotou()               { return jaVotou; }
    public String getOpcaoVotadaId()         { return opcaoVotadaId; }

    public void setAtiva(boolean ativa)              { this.ativa = ativa; }
    public void setJaVotou(boolean jaVotou)          { this.jaVotou = jaVotou; }
    public void setOpcaoVotadaId(String opcaoVotadaId) { this.opcaoVotadaId = opcaoVotadaId; }

    public boolean isGeral() {
        return palestraTitulo == null || palestraTitulo.isEmpty();
    }

    public int getTotalVotos() {
        int total = 0;
        for (OpcaoEnquete o : opcoes) total += o.getVotos();
        return total;
    }

    /** Percentual (0–100) de votos da opção sobre o total. */
    public int getPercentual(OpcaoEnquete opcao) {
        int total = getTotalVotos();
        if (total == 0) return 0;
        return Math.round(opcao.getVotos() * 100f / total);
    }
}
