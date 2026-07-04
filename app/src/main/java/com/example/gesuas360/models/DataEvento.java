package com.example.gesuas360.models;

public class DataEvento {
    private String dia;
    private String mes;
    private String label;

    public DataEvento(String dia, String mes, String label) {
        this.dia = dia;
        this.mes = mes;
        this.label = label;
    }

    public String getDia() { return dia; }
    public String getMes() { return mes; }
    public String getLabel() { return label; }
}
