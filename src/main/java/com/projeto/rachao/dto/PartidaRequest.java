package com.projeto.rachao.dto;

import java.time.LocalDateTime;

/** qtdJogadoresTime é opcional (padrão: 5 para o MVP). */
public record PartidaRequest(
                
                Long esporteId,
                Integer qtdTimes,
                Integer qtdJogadoresTime,
                LocalDateTime dataHora,
                String local) {
}
