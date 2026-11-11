package com.projeto.rachao.entidades;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "AMIGOS", uniqueConstraints = { @UniqueConstraint(columnNames = {"ID_JOGADOR", "ID_AMIGO"})})
public class Amigos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public enum StatusAmizade {
        PENDENTE,
        ACEITA,
        RECUSADA
    }

    @ManyToOne
    @JoinColumn(name = "ID_JOGADOR", nullable = false)
    private Jogador jogador;

    @ManyToOne
    @JoinColumn(name = "ID_AMIGO", nullable = false)
    private Jogador amigo;

    public Amigos() {
    }

    public Amigos(Jogador jogador, Jogador amigo) {
        this.jogador = jogador;
        this.amigo = amigo;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Jogador getJogador() { return jogador; }
    public void setJogador(Jogador jogador) { this.jogador = jogador; }
    public Jogador getAmigo() { return amigo; }
    public void setAmigo(Jogador amigo) { this.amigo = amigo; }
}