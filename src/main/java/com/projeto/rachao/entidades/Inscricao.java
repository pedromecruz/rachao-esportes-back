package com.projeto.rachao.entidades;

import jakarta.persistence.*;

@Entity
@Table(name = "INSCRICAO", uniqueConstraints = {
        @UniqueConstraint(name = "UK_INSCRICAO_PARTIDA_JOGADOR", columnNames = {"PARTIDA_ID", "JOGADOR_ID"})
})
public class Inscricao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "PARTIDA_ID", nullable = false)
    private Partida partida;

    @ManyToOne(optional = false)
    @JoinColumn(name = "JOGADOR_ID", nullable = false)
    private Jogador jogador;

    public Inscricao() {}

    public Inscricao(Partida partida, Jogador jogador) {
        this.partida = partida;
        this.jogador = jogador;
    }

    public Long getId() { return id; }
    public Partida getPartida() { return partida; }
    public Jogador getJogador() { return jogador; }
}
