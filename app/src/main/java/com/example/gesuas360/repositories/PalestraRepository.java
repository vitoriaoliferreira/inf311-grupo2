package com.example.gesuas360.repositories;

import com.example.gesuas360.models.Palestra;

import java.util.ArrayList;
import java.util.List;

public class PalestraRepository {
    private static PalestraRepository instance;

    // Listas separadas para cada dia (11 a 14)
    private List<Palestra> palestrasMockDay11;
    private List<Palestra> palestrasMockDay12;
    private List<Palestra> palestrasMockDay13;
    private List<Palestra> palestrasMockDay14;

    private PalestraRepository() {
        // ==================== DIA 11 ====================
        palestrasMockDay11 = new ArrayList<>();
        palestrasMockDay11.add(new Palestra(
                "09:00",
                "Auditório",
                "Credenciamento e boas-vindas",
                "Início das atividades e entrega de materiais.",
                "",
                "",
                false
        ));
        palestrasMockDay11.add(new Palestra(
                "10:00",
                "Auditório",
                "Abertura oficial do SUAS 360",
                "Presença de autoridades e apresentação do evento.",
                "",
                "",
                false
        ));
        palestrasMockDay11.add(new Palestra(
                "14:00",
                "Sala A",
                "Vínculos que Protegem",
                "Conexões humanas que sustentam a proteção social no SUAS",
                "Mariana Torres",
                "Assistente Social, Mestre e Doutora em Serviço Social pela PUC/SP",
                false
        ));

        // ==================== DIA 12 ====================
        palestrasMockDay12 = new ArrayList<>();
        palestrasMockDay12.add(new Palestra(
                "09:00",
                "Sala B",
                "CRAS em Ação: Prevenção e Fortalecimento",
                "Estratégias do CRAS para prevenção de violações de direitos e fortalecimento de vínculos comunitários.",
                "Carlos Alberto",
                "Assistente Social e Coordenador de CRAS há 12 anos",
                false
        ));
        palestrasMockDay12.add(new Palestra(
                "10:30",
                "Sala A",
                "Cadastro Único e Atualização de Dados",
                "Importância da gestão do CadÚnico para acesso aos programas sociais.",
                "Juliana Menezes",
                "Especialista em Cadastro Único pelo MDS",
                false
        ));
        palestrasMockDay12.add(new Palestra(
                "14:00",
                "Sala C",
                "Saúde Mental no SUAS",
                "Abordagem intersetorial para acolhimento de usuários em sofrimento psíquico.",
                "Dra. Paula Regina",
                "Psicóloga e Doutora em Saúde Coletiva",
                false
        ));

        // ==================== DIA 13 ====================
        palestrasMockDay13 = new ArrayList<>();
        palestrasMockDay13.add(new Palestra(
                "09:00",
                "Auditório",
                "População em Situação de Rua",
                "Políticas de abordagem social, acesso à documentação e cuidados no acolhimento.",
                "Marcos Vinícius",
                "Mestre em Serviço Social e Educador Social com 15 anos de experiência",
                false
        ));
        palestrasMockDay13.add(new Palestra(
                "10:30",
                "Sala B",
                "Proteção à Criança e ao Adolescente",
                "Ações do SUAS na garantia dos direitos previstos no ECA.",
                "Dra. Fernanda Lima",
                "Promotora da Vara da Infância e Juventude",
                false
        ));
        palestrasMockDay13.add(new Palestra(
                "14:00",
                "Sala A",
                "Gênero e Diversidade nos Serviços",
                "Atendimento humanizado e inclusivo à população LGBTQIA+ e mulheres em situação de violência.",
                "Karen Sampaio",
                "Socióloga e Especialista em Políticas de Gênero",
                false
        ));

        // ==================== DIA 14 ====================
        palestrasMockDay14 = new ArrayList<>();
        palestrasMockDay14.add(new Palestra(
                "09:00",
                "Sala C",
                "Inclusão Produtiva e Geração de Renda",
                "Oficinas e cursos profissionalizantes como ferramenta de autonomia para os usuários.",
                "André Oliveira",
                "Economista e Consultor do Ministério do Desenvolvimento Social",
                false
        ));
        palestrasMockDay14.add(new Palestra(
                "10:30",
                "Auditório",
                "Mesa Redonda: Desafios do SUAS 2026",
                "Debate sobre financiamento, gestão de pessoas, inovação social e intersetorialidade.",
                "Mesa com 5 Especialistas",
                "",
                false
        ));
        palestrasMockDay14.add(new Palestra(
                "14:00",
                "Auditório",
                "Encerramento e Carta Compromisso",
                "Leitura da carta com encaminhamentos e propostas para o fortalecimento do SUAS.",
                "",
                "",
                false
        ));
    }

    // Singleton
    public static synchronized PalestraRepository getInstance() {
        if (instance == null) {
            instance = new PalestraRepository();
        }
        return instance;

    }
    public List<Palestra> getPalestras() {
        List<Palestra> todas = new ArrayList<>();
        todas.addAll(palestrasMockDay11);
        todas.addAll(palestrasMockDay12);
        todas.addAll(palestrasMockDay13);
        todas.addAll(palestrasMockDay14);
        return todas;
    }

    // Retorna a lista de um dia específico (11 a 14)
    public List<Palestra> getPalestrasByDay(int day) {
        switch (day) {
            case 11: return palestrasMockDay11;
            case 12: return palestrasMockDay12;
            case 13: return palestrasMockDay13;
            case 14: return palestrasMockDay14;
            default: return new ArrayList<>();
        }
    }

    public List<Palestra> getPalestrasMockDay11() { return palestrasMockDay11; }
    public List<Palestra> getPalestrasMockDay12() { return palestrasMockDay12; }
    public List<Palestra> getPalestrasMockDay13() { return palestrasMockDay13; }
    public List<Palestra> getPalestrasMockDay14() { return palestrasMockDay14; }
}