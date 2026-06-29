package com.example.gesuas360.models;

public class Participante {
    private String id;
    private String nome;
    private String email;
    private String token;
    private String telefone;
    private String cidade;

    public Participante(String id, String nome, String email, String token) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.token = token;
    }

    public String getId() { return id; }
    public String getNome() { return nome; }
    public String getEmail() { return email; }
    public String getToken() { return token; }
    public String getTelefone() { return telefone; }
    public String getCidade() { return cidade; }

    public void setEmail(String email) { this.email = email; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
    public void setCidade(String cidade) { this.cidade = cidade; }
}
