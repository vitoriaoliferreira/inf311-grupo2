package com.example.gesuas360.models;

import java.util.UUID;

public class Notificacao {

    public static final int TIPO_INFO = 0;
    public static final int TIPO_MENSAGEM = 1;
    public static final int TIPO_PALESTRA = 2;
    public static final int TIPO_ALERTA = 3;

    private final String id;
    private String titulo;
    private String corpo;
    private String horario;
    private int tipo;
    private boolean lida;
    private long timestamp;

    public Notificacao(String titulo, String corpo, String horario, int tipo) {
        this.id        = UUID.randomUUID().toString();
        this.titulo    = titulo;
        this.corpo     = corpo;
        this.horario   = horario;
        this.tipo      = tipo;
        this.lida      = false;
        this.timestamp = System.currentTimeMillis();
    }

    public String getId()             { return id; }
    public String getTitulo()         { return titulo; }
    public String getCorpo()          { return corpo; }
    public String getHorario()        { return horario; }
    public int getTipo()              { return tipo; }
    public boolean isLida()           { return lida; }
    public long getTimestamp()        { return timestamp; }
    public void setLida(boolean lida) { this.lida = lida; }
}
