package com.example.gesuas360.models;

public class Palestrante {
    private String nome;
    private String cargo;

    public Palestrante(String nome, String cargo) {
        this.nome = nome;
        this.cargo = cargo;
    }

    public String getNome() {
        return nome;
    }

    public String getCargo() {
        return cargo;
    }
}
