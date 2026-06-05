package com.example.gesuas360.models;

public class Notificacao {
    private String titulo;
    private String corpo;
    private String horario;
    private int tipo;

    public Notificacao(String titulo, String corpo, String horario, int tipo) {
        this.titulo = titulo;
        this.corpo = corpo;
        this.horario = horario;
        this.tipo = tipo;
    }

    public String getTitulo() { return titulo; }
    public String getCorpo() { return corpo; }
    public String getHorario() { return horario; }
    public int getTipo() { return tipo; }
}
