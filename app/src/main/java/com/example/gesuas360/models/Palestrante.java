package com.example.gesuas360.models;

public class Palestrante {
    private String nome;
    private String cargo;
    private String biografia;
    private boolean favorito;

    public Palestrante(String nome, String cargo, String biografia) {
        this(nome, cargo, biografia, false); // chama o novo construtor
    }

    public Palestrante(String nome, String cargo, String biografia, boolean favorito) {
        this.nome = nome;
        this.cargo = cargo;
        this.biografia = biografia;
        this.favorito = false;
    }

    public String getNome() {
        return nome;
    }

    public String getCargo() {
        return cargo;
    }

    public String getBiografia() {
        return biografia;
    }

    public boolean isFavorito() { return  favorito; }

    public void setFavorito(boolean favorito) { this.favorito = favorito; }
}
