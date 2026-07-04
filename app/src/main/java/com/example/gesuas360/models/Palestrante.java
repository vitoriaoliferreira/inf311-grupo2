package com.example.gesuas360.models;

import java.io.Serializable;

public class Palestrante implements Serializable {
    private String nome;
    private String cargo;
    private String biografia;

    public Palestrante(String nome, String cargo, String biografia) {
        this.nome = nome;
        this.cargo = cargo;
        this.biografia = biografia;
    }

    private boolean favorito = false;

    public String getNome() { return nome; }
    public String getCargo() { return cargo; }
    public String getBiografia() { return biografia; }
    public boolean isFavorito() { return favorito; }
    public void setFavorito(boolean favorito) { this.favorito = favorito; }
}
