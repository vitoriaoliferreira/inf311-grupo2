package com.example.gesuas360.models;

public class Mensagem {
    private String categoria;
    private String titulo;
    private String corpo;
    private String horario;
    private boolean isAviso;

    public Mensagem(String categoria, String titulo, String corpo, String horario, boolean isAviso) {
        this.categoria = categoria;
        this.titulo = titulo;
        this.corpo = corpo;
        this.horario = horario;
        this.isAviso = isAviso;
    }

    public String getCategoria() { return categoria; }
    public String getTitulo() { return titulo; }
    public String getCorpo() { return corpo; }
    public String getHorario() { return horario; }
    public boolean isAviso() { return isAviso; }
}
