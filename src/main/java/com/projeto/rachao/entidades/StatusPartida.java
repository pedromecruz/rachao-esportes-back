package com.projeto.rachao.entidades;

/** Status do ciclo de vida de uma partida. */
public final class StatusPartida {
    private StatusPartida() {}

    public enum StatusAtual {
        PENDENTE,
        EM_ANDAMENTO,
        CONCLUIDA,
        CANCELADA
    }
}
