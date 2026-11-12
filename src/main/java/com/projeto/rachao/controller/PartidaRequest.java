
package com.projeto.rachao.controller;

import java.time.LocalDateTime;

public record PartidaRequest(
                Long jogadorHostId,
                Long esporteId,
                Integer qtdTimes,
                LocalDateTime dataHora,
                String local) {
}
