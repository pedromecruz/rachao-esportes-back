package com.projeto.rachao.service;

import com.projeto.rachao.controller.*;
import com.projeto.rachao.entidades.*;
import com.projeto.rachao.repository.*;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PartidaService {

    private final PartidaRepository partidaRepository;
    private final JogadorRepository jogadorRepository;
    private final EsportesRepository esportesRepository;

    public PartidaService(
            PartidaRepository partidaRepository,
            JogadorRepository jogadorRepository,
            EsportesRepository esportesRepository) {

        this.partidaRepository = partidaRepository;
        this.jogadorRepository = jogadorRepository;
        this.esportesRepository = esportesRepository;
    }

    public Partida criarPartida(PartidaRequest dados) {

        Jogador host = jogadorRepository
                .findById(dados.jogadorHostId())
                .orElseThrow(() -> new RuntimeException("Host não encontrado"));

        Esportes esporte = esportesRepository
                .findById(dados.esporteId())
                .orElseThrow(() -> new RuntimeException("Esporte não encontrado"));

        if (dados.qtdTimes() == null || dados.qtdTimes() < 2) {
            throw new IllegalArgumentException(
                    "A partida deve possuir pelo menos 2 times");
        }

        // Criar a partida
        Partida partida = new Partida();

        partida.setJogadorHost(host);
        partida.setEsporte(esporte);
        partida.setQtdTimes(dados.qtdTimes());
        partida.setDataHora(dados.dataHora());
        partida.setLocal(dados.local());

        return partida;
    }

}