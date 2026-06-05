package com.example.gesuas360.models;

public class Palestrante {
    private String nome;
    private String cargo;
    private String biografia;

    public Palestrante(String nome, String cargo, String biografia) {
        this.nome = nome;
        this.cargo = cargo;
        this.biografia = biografia;
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
}
