package com.example.gesuas360.repositories;

import com.example.gesuas360.models.Palestrante;

import java.util.ArrayList;
import java.util.List;

public class PalestranteRepository {
    private static PalestranteRepository instance;
    private final List<Palestrante> palestrantes;

    private PalestranteRepository() {
        palestrantes = new ArrayList<>();
        palestrantes.add(new Palestrante("Maria Silva", "Especialista em Gestão Pública",
                "Maria Silva é mestre em Políticas Públicas e possui mais de 15 anos de atuação no SUAS. " +
                        "Tem focado suas pesquisas na eficiência da gestão municipal e no impacto social dos programas de transferência de renda."));

        palestrantes.add(new Palestrante("José da Silva", "Gestor Municipal",
                "José da Silva atua na gestão pública há 20 anos, tendo liderado diversas secretarias de assistência social. " +
                        "É referência em planejamento estratégico para cidades de médio porte."));

        palestrantes.add(new Palestrante("Tânia Maria", "Assistente Social",
                "Tânia Maria é assistente social com especialização em terapia familiar. " +
                        "Sua trajetória é marcada pela defesa dos direitos da criança e do adolescente e pelo fortalecimento de vínculos comunitários."));

        palestrantes.add(new Palestrante("Paulo Souza", "Especialista em SUAS",
                "Paulo Souza é consultor e professor universitário. " +
                        "Contribuiu na elaboração de diversas normativas do SUAS e viaja o Brasil capacitando novas equipes técnicas."));

        palestrantes.add(new Palestrante("Abigail Torres", "Doutora em Serviço Social",
                "Abigail Torres é doutora pela PUC e autora de diversos livros sobre a história da assistência social no Brasil. " +
                        "Sua fala é focada na ética profissional e nos desafios contemporâneos do serviço social."));
    }

    public static synchronized PalestranteRepository getInstance() {
        if (instance == null) {
            instance = new PalestranteRepository();
        }
        return instance;
    }

    public List<Palestrante> getPalestrantes() {
        return palestrantes;
    }

    public void toggleFavorito(Palestrante palestrante) {
        palestrante.setFavorito(!palestrante.isFavorito());
    }

    public List<Palestrante> getFavoritos() {
        List<Palestrante> favoritos = new ArrayList<>();
        for(Palestrante p : palestrantes) {
            if(p.isFavorito()) {
                favoritos.add(p);
            }
        }
        return favoritos;
    }

    public Palestrante getPalestrantePorNome(String nome) {
        for (Palestrante p : palestrantes) {
            if (p.getNome().equalsIgnoreCase(nome)) {
                return p;
            }
        }
        return null;
    }
}