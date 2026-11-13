package com.projeto.rachao.entidades;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
    name = "DESEMPENHO",
    uniqueConstraints = {
        @UniqueConstraint(
            columnNames = {"ID_JOGADOR", "ID_PARTIDA"}
        )
    }
)
public class Desempenho {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // JOGADOR
    @ManyToOne(optional = false)
    @JoinColumn(name = "ID_JOGADOR", nullable = false)
    private Jogador jogador;

    // PARTIDA
    @ManyToOne(optional = false)
    @JoinColumn(name = "ID_PARTIDA", nullable = false)
    private Partida partida;

    // NOTA DO JOGADOR
    @Column(name = "NOTA")
    private Double nota;

    // PONTOS, GOLS OU CESTAS
    @Column(name = "PGC")
    private Integer pgc;

    // CONSTRUTOR VAZIO
    public Desempenho() {
    }

    // GETTERS E SETTERS

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Jogador getJogador() {
        return jogador;
    }

    public void setJogador(Jogador jogador) {
        this.jogador = jogador;
    }

    public Partida getPartida() {
        return partida;
    }

    public void setPartida(Partida partida) {
        this.partida = partida;
    }

    public Double getNota() {
        return nota;
    }

    public void setNota(Double nota) {
        this.nota = nota;
    }

    public Integer getPgc() {
        return pgc;
    }

    public void setPgc(Integer pgc) {
        this.pgc = pgc;
    }
}