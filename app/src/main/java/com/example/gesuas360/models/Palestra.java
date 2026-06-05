package com.example.gesuas360.models;

public class Palestra {
    private String horario;
    private String local;
    private String titulo;
    private String descricao;
    private String palestranteNome;
    private String palestranteBio;
    private boolean isFavorito;

    public Palestra(String horario, String local, String titulo, String descricao, String palestranteNome, String palestranteBio, boolean isFavorito) {
        this.horario = horario;
        this.local = local;
        this.titulo = titulo;
        this.descricao = descricao;
        this.palestranteNome = palestranteNome;
        this.palestranteBio = palestranteBio;
        this.isFavorito = isFavorito;
    }

    public String getHorario() { return horario; }
    public String getLocal() { return local; }
    public String getTitulo() { return titulo; }
    public String getDescricao() { return descricao; }
    public String getPalestranteNome() { return palestranteNome; }
    public String getPalestranteBio() { return palestranteBio; }
    
    public boolean isFavorito() { 
        return isFavorito; 
    }

    public void setFavorito(boolean favorito) {
        this.isFavorito = favorito;
    }
}
