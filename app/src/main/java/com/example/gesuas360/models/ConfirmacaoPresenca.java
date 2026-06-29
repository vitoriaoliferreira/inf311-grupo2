package com.example.gesuas360.models;

public class ConfirmacaoPresenca {
    private final String participanteId;
    private final String participanteNome;
    private final String participanteEmail;
    private final String palestTitulo;
    private final String palestHorario;
    private final String palestData;
    private final String palestLocal;
    private final String qrToken;

    public ConfirmacaoPresenca(String participanteId, String participanteNome,
                                String participanteEmail, String palestTitulo,
                                String palestHorario, String palestData,
                                String palestLocal, String qrToken) {
        this.participanteId    = participanteId;
        this.participanteNome  = participanteNome;
        this.participanteEmail = participanteEmail;
        this.palestTitulo      = palestTitulo;
        this.palestHorario     = palestHorario;
        this.palestData        = palestData;
        this.palestLocal       = palestLocal;
        this.qrToken           = qrToken;
    }

    public String getParticipanteId()    { return participanteId; }
    public String getParticipanteNome()  { return participanteNome; }
    public String getParticipanteEmail() { return participanteEmail; }
    public String getPalestTitulo()      { return palestTitulo; }
    public String getPalestHorario()     { return palestHorario; }
    public String getPalestData()        { return palestData; }
    public String getPalestLocal()       { return palestLocal; }
    public String getQrToken()           { return qrToken; }
}
