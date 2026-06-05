package com.example.gesuas360.repositories;

import com.example.gesuas360.models.Palestra;

import java.util.ArrayList;
import java.util.List;

public class PalestraRepository {
    private static PalestraRepository instance;
    private List<Palestra> palestrasMock;

    private PalestraRepository() {
        palestrasMock = new ArrayList<>();
        palestrasMock.add(new Palestra(
                "09:00",
                "Auditório",
                "Credenciamento e boas-vindas",
                "Início das atividades e entrega de materiais.",
                "",
                "",
                false
        ));
        palestrasMock.add(new Palestra(
                "10:00",
                "Auditório",
                "Abertura oficial do SUAS 360",
                "Presença de autoridades e apresentação do evento.",
                "",
                "",
                false
        ));
        palestrasMock.add(new Palestra(
                "14:00",
                "Sala A",
                "Vínculos que Protegem",
                "Conexões humanas que sustentam a proteção social no SUAS",
                "Mariana Torres",
                "Assistente Social, Mestre e Doutora em Serviço Social pela PUC/SP",
                false
        ));
    }

    public static synchronized PalestraRepository getInstance() {
        if (instance == null) {
            instance = new PalestraRepository();
        }
        return instance;
    }

    public List<Palestra> getPalestras() {
        return palestrasMock;
    }
}
