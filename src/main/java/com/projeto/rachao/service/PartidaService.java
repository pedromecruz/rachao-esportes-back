package com.projeto.rachao.service;

import com.projeto.rachao.dto.PartidaRequest;
import com.projeto.rachao.dto.PartidaDTO;
import com.projeto.rachao.entidades.*;
import com.projeto.rachao.repository.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Service
public class PartidaService {
    private static final ZoneId FUSO = ZoneId.of("America/Sao_Paulo");
    private final PartidaRepository partidas;
    private final JogadorRepository jogadores;
    private final EsportesRepository esportes;
    private final InscricaoRepository inscricoes;

    public PartidaService(PartidaRepository partidas, JogadorRepository jogadores,
            EsportesRepository esportes, InscricaoRepository inscricoes) {
        this.partidas = partidas;
        this.jogadores = jogadores;
        this.esportes = esportes;
        this.inscricoes = inscricoes;
    }

    @Transactional(readOnly = true)
    public List<PartidaDTO> listar() {
        return partidas.findAll().stream().map(this::converter).toList();
    }

    @Transactional(readOnly = true)
    public PartidaDTO buscar(Long id) {
        return converter(obter(id));
    }

    @Transactional
    public PartidaDTO criarPartida(
            PartidaRequest dados,
            Long jogadorAutenticadoId) {

        validarDados(dados);

        // Busca o jogador autenticado pelo token
        Jogador host = jogadores.findById(jogadorAutenticadoId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Organizador não encontrado."));

        // Busca o esporte selecionado
        Esportes esporte = buscarEsporte(dados.esporteId());

        // Cria a partida
        Partida partida = new Partida();

        preencher(partida, dados, esporte);

        // Define automaticamente o organizador
        partida.setJogadorHost(host);

        partida.setStatus(StatusPartida.StatusAtual.PENDENTE);

        // Salva a partida no banco
        Partida salva = partidas.saveAndFlush(partida);

        // Inscreve automaticamente o organizador
        inscricoes.saveAndFlush(new Inscricao(salva, host));

        return converter(salva);
    }

    @Transactional
    public PartidaDTO atualizar(Long id, PartidaRequest dados, Long jogadorAutenticadoId) {
        Partida partida = partidas.bloquearPorId(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Partida não encontrada."));
        validarOrganizador(partida, jogadorAutenticadoId);
        if (partida.getStatus() != StatusPartida.StatusAtual.PENDENTE) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Apenas partidas pendentes podem ser editadas.");
        }
        validarDados(dados);

        Esportes esporte = buscarEsporte(dados.esporteId());
        long capacidade = (long) dados.qtdTimes() * tamanhoTime(dados);
        if (capacidade < inscricoes.countByPartidaId(id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "A nova capacidade é menor que o número de jogadores inscritos.");
        }
        preencher(partida, dados, esporte);
        return converter(partidas.save(partida));
    }

    @Transactional
    public PartidaDTO cancelar(Long id, Long jogadorAutenticadoId) {
        Partida partida = partidas.bloquearPorId(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Partida não encontrada."));
        validarOrganizador(partida, jogadorAutenticadoId);
        if (partida.getStatus() != StatusPartida.StatusAtual.PENDENTE) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Somente partidas pendentes podem ser canceladas.");
        }
        partida.setStatus(StatusPartida.StatusAtual.CANCELADA);
        return converter(partidas.save(partida));
    }

    private void validarOrganizador(Partida partida, Long jogadorId) {
        if (!partida.getJogadorHost().getId().equals(jogadorId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Somente o organizador pode alterar esta partida.");
        }
    }

    private Esportes buscarEsporte(Long id) {
        return esportes.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Esporte não encontrado."));
    }

    private Partida obter(Long id) {
        return partidas.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Partida não encontrada."));
    }

    private int tamanhoTime(PartidaRequest dados) {
        return dados.qtdJogadoresTime() == null ? 5 : dados.qtdJogadoresTime();
    }

    private void validarDados(PartidaRequest dados) {
        if (dados == null || dados.esporteId() == null) {
            throw new IllegalArgumentException("O esporte é obrigatório.");
        }
        if (dados.qtdTimes() == null || dados.qtdTimes() < 2 || dados.qtdTimes() > 20) {
            throw new IllegalArgumentException("A partida deve ter entre 2 e 20 times.");
        }
        if (tamanhoTime(dados) < 1 || tamanhoTime(dados) > 50) {
            throw new IllegalArgumentException("Cada time deve ter entre 1 e 50 jogadores.");
        }
        if (dados.dataHora() == null || !dados.dataHora().isAfter(LocalDateTime.now(FUSO))) {
            throw new IllegalArgumentException("A data e hora da partida devem ser futuras (horário de São Paulo).");
        }
        if (dados.local() == null || dados.local().isBlank()) {
            throw new IllegalArgumentException("O local da partida é obrigatório.");
        }
    }

    private void preencher(Partida partida, PartidaRequest dados, Esportes esporte) {
        partida.setEsporte(esporte);
        partida.setQtdTimes(dados.qtdTimes());
        partida.setQtdJogadoresTime(tamanhoTime(dados));
        partida.setDataHora(dados.dataHora());
        partida.setLocal(dados.local().trim());
    }

    private PartidaDTO converter(Partida partida) {
        long inscritos = inscricoes.countByPartidaId(partida.getId());
        long capacidade = (long) partida.getQtdTimes() * partida.getQtdJogadoresTime();
        return new PartidaDTO(partida.getId(), partida.getJogadorHost().getId(),
                partida.getJogadorHost().getNome(), partida.getEsporte().getId(),
                partida.getEsporte().getEsporte(), partida.getQtdTimes(), partida.getQtdJogadoresTime(),
                partida.getDataHora(), partida.getLocal(), partida.getStatus(),
                inscritos, Math.max(0, capacidade - inscritos));
    }
}
