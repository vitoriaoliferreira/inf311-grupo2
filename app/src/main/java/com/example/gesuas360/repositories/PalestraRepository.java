package com.example.gesuas360.repositories;

import com.example.gesuas360.models.DataEvento;
import com.example.gesuas360.models.Palestra;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PalestraRepository {
    private static PalestraRepository instance;
    private final List<Palestra> palestrasMock;

    private PalestraRepository() {
        palestrasMock = new ArrayList<>();

        // Dia 11
        palestrasMock.add(new Palestra("09:00", "11", "Hall de Entrada",
                "Credenciamento", "Recepção e entrega de materiais e credenciais do evento.",
                "", "", false));
        palestrasMock.add(new Palestra("10:00", "11", "Auditório Principal",
                "Abertura Oficial do SUAS 360", "Presença de autoridades e apresentação da programação completa do evento.",
                "", "", false));
        palestrasMock.add(new Palestra("14:00", "11", "Sala A",
                "Vínculos que Protegem", "Conexões humanas que sustentam a proteção social no SUAS. Exploraremos como o fortalecimento dos vínculos familiares e comunitários é essencial para a eficácia das políticas públicas.",
                "Mariana Torres", "Assistente Social, Mestre e Doutora em Serviço Social pela PUC/SP", false));
        palestrasMock.add(new Palestra("16:00", "11", "Auditório Principal",
                "O SUAS e os Desafios Contemporâneos", "Análise das normativas e da implementação do SUAS nos municípios brasileiros, com foco nos desafios da gestão descentralizada.",
                "Paulo Souza", "Consultor e professor universitário, especialista em SUAS", false));

        // Dia 12
        palestrasMock.add(new Palestra("09:00", "12", "Sala B",
                "Gestão Municipal e Eficiência no SUAS", "Como otimizar recursos e ampliar o impacto das políticas de assistência social em municípios de todos os portes.",
                "Maria Silva", "Especialista em Gestão Pública", false));
        palestrasMock.add(new Palestra("14:00", "12", "Auditório Principal",
                "Liderança e Planejamento Estratégico", "Experiências concretas de gestão pública em municípios de médio porte, com destaque para os resultados alcançados.",
                "José da Silva", "Gestor Municipal", false));
        palestrasMock.add(new Palestra("16:00", "12", "Sala A",
                "Fortalecendo Vínculos Comunitários", "A atuação do assistente social na construção de redes de proteção e na articulação de recursos territoriais.",
                "Tânia Maria", "Assistente Social", false));

        // Dia 13
        palestrasMock.add(new Palestra("09:00", "13", "Auditório Principal",
                "História da Assistência Social no Brasil", "Da filantropia ao direito: uma trajetória de conquistas e desafios que moldaram o SUAS como o conhecemos hoje.",
                "Abigail Torres", "Doutora em Serviço Social", false));
        palestrasMock.add(new Palestra("14:00", "13", "Sala B",
                "Ética Profissional no Serviço Social", "Princípios fundamentais e dilemas éticos na prática cotidiana do serviço social nos equipamentos do SUAS.",
                "Abigail Torres", "Doutora em Serviço Social", false));
        palestrasMock.add(new Palestra("16:00", "13", "Sala A",
                "Mesa-Redonda: SUAS 360 em Debate", "Discussão coletiva sobre os avanços, lacunas e perspectivas do sistema de assistência social.",
                "Paulo Souza", "Consultor e professor universitário, especialista em SUAS", false));

        // Dia 14
        palestrasMock.add(new Palestra("09:00", "14", "Auditório Principal",
                "Keynote: O Futuro do SUAS", "Perspectivas, inovações e desafios para os próximos anos da assistência social no Brasil.",
                "Mariana Torres", "Assistente Social, Mestre e Doutora em Serviço Social pela PUC/SP", false));
        palestrasMock.add(new Palestra("17:00", "14", "Auditório Principal",
                "Encerramento e Premiações", "Cerimônia de encerramento, reconhecimento das contribuições e entrega de certificados.",
                "", "", false));
    }

    public static synchronized PalestraRepository getInstance() {
        if (instance == null) instance = new PalestraRepository();
        return instance;
    }

    public List<Palestra> getPalestras() {
        return palestrasMock;
    }

    public List<Palestra> getPalestrasByData(String dia) {
        List<Palestra> resultado = new ArrayList<>();
        for (Palestra p : palestrasMock) {
            if (dia.equals(p.getData())) resultado.add(p);
        }
        return resultado;
    }

    /**
     * Retorna os dias do evento derivados das palestras cadastradas.
     * Quando a API estiver pronta, substituir pelo endpoint GET /api/evento/dias
     * que devolve: [{ "dia": "11", "mes": "Maio", "label": "Segunda-Feira, 11 de Maio" }, ...]
     */
    public List<DataEvento> getDiasDoEvento() {
        Map<String, DataEvento> diasMap = new LinkedHashMap<>();
        for (Palestra p : palestrasMock) {
            if (!diasMap.containsKey(p.getData())) {
                diasMap.put(p.getData(), criarDataEvento(p.getData()));
            }
        }
        return new ArrayList<>(diasMap.values());
    }

    private DataEvento criarDataEvento(String dia) {
        switch (dia) {
            case "11": return new DataEvento("11", "Maio", "Segunda-Feira, 11 de Maio");
            case "12": return new DataEvento("12", "Maio", "Terça-Feira, 12 de Maio");
            case "13": return new DataEvento("13", "Maio", "Quarta-Feira, 13 de Maio");
            case "14": return new DataEvento("14", "Maio", "Quinta-Feira, 14 de Maio");
            default:   return new DataEvento(dia, "Maio", dia + " de Maio");
        }
    }

    public List<Palestra> getPalestrasFavoritas() {
        List<Palestra> favoritas = new ArrayList<>();
        for (Palestra p : palestrasMock) {
            if (p.isFavorito()) favoritas.add(p);
        }
        return favoritas;
    }

    public List<Palestra> getPalestrasDoPalestrante(String nomePalestrante) {
        List<Palestra> resultado = new ArrayList<>();
        for (Palestra p : palestrasMock) {
            if (nomePalestrante != null && nomePalestrante.equals(p.getPalestranteNome())) {
                resultado.add(p);
            }
        }
        return resultado;
    }
}
