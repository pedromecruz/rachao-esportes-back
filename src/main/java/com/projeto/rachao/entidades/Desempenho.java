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
@Table(name = "DESEMPENHO", uniqueConstraints = { @UniqueConstraint(columnNames = { "ID_JOGADOR", "ID_JOGO" }) })
public class Desempenho {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "ID_JOGADOR", nullable = false)
    private Jogador jogador;

    @ManyToOne(optional = false)
    @JoinColumn(name = "ID_JOGO", nullable = false)
    private Jogo jogo;

    @Column(name = "NOTA")
    private Double nota;

    @Column(name = "PGC")
    private Integer Pgc; // Quantidade de Pontos, Gols ou Cestas

    public Desempenho() {
    }

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

    public Jogo getJogo() {
        return jogo;
    }

    public void setJogo(Jogo jogo) {
        this.jogo = jogo;
    }

    public Double getNota() {
        return nota;
    }

    public void setNota(Double nota) {
        this.nota = nota;
    }

    public Integer getPgc() {
        return Pgc;
    }

    public void setPgc(Integer Pgc) {
        this.Pgc = Pgc;
    }
}