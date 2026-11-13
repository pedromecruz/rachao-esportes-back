package com.projeto.rachao.service;

import com.projeto.rachao.dto.InscricaoDTO;
import com.projeto.rachao.entidades.*;
import com.projeto.rachao.repository.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class InscricaoService {
    private static final ZoneId FUSO = ZoneId.of("America/Sao_Paulo");
    private final InscricaoRepository inscricoes;
    private final PartidaRepository partidas;
    private final JogadorRepository jogadores;

    public InscricaoService(InscricaoRepository inscricoes, PartidaRepository partidas, JogadorRepository jogadores) {
        this.inscricoes = inscricoes;
        this.partidas = partidas;
        this.jogadores = jogadores;
    }

    @Transactional
    public InscricaoDTO entrar(Long partidaId, Long jogadorId) {
        // Travamento transacional para não vender mais vagas que a capacidade.
        Partida partida = partidas.bloquearPorId(partidaId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Partida não encontrada."));
        if (partida.getStatus() != StatusPartida.StatusAtual.PENDENTE
                || !partida.getDataHora().isAfter(LocalDateTime.now(FUSO))) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Esta partida não aceita inscrições.");
        }
        if (inscricoes.existsByPartidaIdAndJogadorId(partidaId, jogadorId)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Jogador já está inscrito.");
        }
        long capacidade = (long) partida.getQtdTimes() * partida.getQtdJogadoresTime();
        if (inscricoes.countByPartidaId(partidaId) >= capacidade) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Não há vagas disponíveis.");
        }
        Jogador jogador = jogadores.findById(jogadorId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Jogador não encontrado."));
        return converter(inscricoes.saveAndFlush(new Inscricao(partida, jogador)));
    }

    @Transactional
    public void sair(Long partidaId, Long jogadorId) {
        Partida partida = partidas.bloquearPorId(partidaId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Partida não encontrada."));
        if (partida.getJogadorHost().getId().equals(jogadorId)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "O organizador não pode sair da própria partida.");
        }
        if (partida.getStatus() != StatusPartida.StatusAtual.PENDENTE) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Não é possível sair desta partida.");
        }
        Inscricao inscricao = inscricoes.findByPartidaIdAndJogadorId(partidaId, jogadorId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Inscrição não encontrada."));
        inscricoes.delete(inscricao);
    }

    @Transactional(readOnly = true)
    public List<InscricaoDTO> listarDaPartida(Long partidaId) {
        if (!partidas.existsById(partidaId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Partida não encontrada.");
        }
        return inscricoes.findByPartidaIdOrderByIdAsc(partidaId).stream().map(this::converter).toList();
    }

    @Transactional(readOnly = true)
    public List<InscricaoDTO> listarDoJogador(Long jogadorId) {
        if (!jogadores.existsById(jogadorId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Jogador não encontrado.");
        }
        return inscricoes.findByJogadorIdOrderByIdAsc(jogadorId).stream().map(this::converter).toList();
    }

    private InscricaoDTO converter(Inscricao inscricao) {
        return new InscricaoDTO(inscricao.getId(), inscricao.getPartida().getId(),
                inscricao.getJogador().getId(), inscricao.getJogador().getNome());
    }
}
