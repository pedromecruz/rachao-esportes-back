package com.projeto.rachao.service;

import com.projeto.rachao.entidades.Esportes;
import com.projeto.rachao.repository.EsportesRepository;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class EsporteService {
    private final EsportesRepository esportes;

    public EsporteService(EsportesRepository esportes) {
        this.esportes = esportes;
    }

    @Transactional(readOnly = true)
    public List<Esportes> listar() { return esportes.findAll(); }

    @Transactional(readOnly = true)
    public Esportes buscar(Long id) {
        return esportes.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Esporte não encontrado."));
    }
}
