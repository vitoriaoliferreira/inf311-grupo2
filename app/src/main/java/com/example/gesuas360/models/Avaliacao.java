package com.example.gesuas360.models;

/**
 * Avaliação de uma palestra e do seu palestrante, preenchida pelo participante ao
 * final da palestra e enviada em {@code POST /api/avaliacoes}.
 *
 * As notas vão de 1 a 5. {@code notaPalestrante} é {@code 0} quando a palestra não
 * tem palestrante associado (ex.: credenciamento, abertura).
 */
public class Avaliacao {

    private final String participanteId;
    private final String participanteNome;
    private final String palestraTitulo;
    private final String palestraData;
    private final String palestranteNome;
    private final int    notaPalestra;
    private final int    notaPalestrante;
    private final String comentario;

    public Avaliacao(String participanteId, String participanteNome,
                     String palestraTitulo, String palestraData, String palestranteNome,
                     int notaPalestra, int notaPalestrante, String comentario) {
        this.participanteId   = participanteId;
        this.participanteNome = participanteNome;
        this.palestraTitulo   = palestraTitulo;
        this.palestraData     = palestraData;
        this.palestranteNome  = palestranteNome;
        this.notaPalestra     = notaPalestra;
        this.notaPalestrante  = notaPalestrante;
        this.comentario       = comentario;
    }

    public String getParticipanteId()   { return participanteId; }
    public String getParticipanteNome() { return participanteNome; }
    public String getPalestraTitulo()   { return palestraTitulo; }
    public String getPalestraData()     { return palestraData; }
    public String getPalestranteNome()  { return palestranteNome; }
    public int    getNotaPalestra()     { return notaPalestra; }
    public int    getNotaPalestrante()  { return notaPalestrante; }
    public String getComentario()       { return comentario; }
}
