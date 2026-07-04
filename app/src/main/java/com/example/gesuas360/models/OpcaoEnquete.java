package com.example.gesuas360.models;

import java.io.Serializable;
import java.util.UUID;

/** Uma opção de resposta de uma {@link Enquete}. */
public class OpcaoEnquete implements Serializable {

    private final String id;
    private final String texto;
    private int votos;

    public OpcaoEnquete(String id, String texto, int votos) {
        this.id    = id != null ? id : UUID.randomUUID().toString();
        this.texto = texto;
        this.votos = votos;
    }

    public OpcaoEnquete(String texto) {
        this(null, texto, 0);
    }

    public String getId()   { return id; }
    public String getTexto() { return texto; }
    public int getVotos()   { return votos; }

    public void setVotos(int votos)   { this.votos = votos; }
    public void incrementarVoto()     { this.votos++; }
}
