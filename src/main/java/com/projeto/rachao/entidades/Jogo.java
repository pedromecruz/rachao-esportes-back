package com.projeto.rachao.entidades;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "JOGO")
public class Jogo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String placar;

    @ManyToOne(optional = false)
    @JoinColumn(name = "ID_PARTIDA", nullable = false)
    private Partida partida;

    private Integer tempos;

    public Jogo() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPlacar() {
        return placar;
    }

    public void setPlacar(String placar) {
        this.placar = placar;
    }

    public Partida getPartida() {
        return partida;
    }

    public void setPartida(Partida partida) {
        this.partida = partida;
    }

    public Integer getTempos() {
        return tempos;
    }

    public void setTempos(Integer tempos) {
        this.tempos = tempos;
    }
}