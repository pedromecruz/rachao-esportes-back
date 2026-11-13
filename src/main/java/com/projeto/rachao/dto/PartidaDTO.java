package com.projeto.rachao.dto;

import java.time.LocalDateTime;
import com.projeto.rachao.entidades.Esportes.EsportesLista;
import com.projeto.rachao.entidades.StatusPartida.StatusAtual;

/** Resposta sem expor a senha ou o CPF do organizador. */
public record PartidaDTO(
        Long id,
        Long jogadorHostId,
        String jogadorHostNome,
        Long esporteId,
        EsportesLista esporte,
        Integer qtdTimes,
        Integer qtdJogadoresTime,
        LocalDateTime dataHora,
        String local,
        StatusAtual status,
        long inscritos,
        long vagasRestantes) {
}
