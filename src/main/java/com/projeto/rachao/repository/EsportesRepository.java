package com.projeto.rachao.repository;

import com.projeto.rachao.entidades.Esportes;
import com.projeto.rachao.entidades.Esportes.EsportesLista;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EsportesRepository extends JpaRepository<Esportes, Long> {
    boolean existsByEsporte(EsportesLista esporte);
}
