package com.projeto.rachao.config;

import com.projeto.rachao.entidades.Esportes;
import com.projeto.rachao.entidades.Esportes.EsportesLista;
import com.projeto.rachao.repository.EsportesRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EsportesCargaInicial {
    @Bean
    CommandLineRunner cadastrarEsportes(EsportesRepository repository) {
        return args -> {
            for (EsportesLista nome : EsportesLista.values()) {
                if (!repository.existsByEsporte(nome)) {
                    Esportes esporte = new Esportes();
                    esporte.setEsporte(nome);
                    repository.save(esporte);
                }
            }
        };
    }
}
