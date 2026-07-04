package com.example.gesuas360;

import com.example.gesuas360.models.Participante;

public class SessaoUsuario {
    private static SessaoUsuario instance;
    private Participante participante;

    private SessaoUsuario() {}

    public static synchronized SessaoUsuario getInstance() {
        if (instance == null) instance = new SessaoUsuario();
        return instance;
    }

    public void setParticipante(Participante p) { participante = p; }
    public Participante getParticipante() { return participante; }
    public void limpar() { participante = null; }
}
