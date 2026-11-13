package com.projeto.rachao.entidades;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "PARTIDA")
public class Partida {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "HOST_ID", nullable = false)
    private Jogador jogadorHost;

    @ManyToOne(optional = false)
    @JoinColumn(name = "ESPORTE", nullable = false)
    private Esportes esporte;

    @Column(name = "QTD_TIMES")
    private Integer qtdTimes;

    @Column(name = "QTD_JOGADORES_TIME")
    private Integer qtdJogadoresTime;

    @Column(name = "DATA_HORA")
    private LocalDateTime dataHora;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusPartida.StatusAtual status = StatusPartida.StatusAtual.PENDENTE;

    private String local;

    private LocalTime duracao;

    @Column(name = "QTD_JOGOS")
    private Integer qtdJogos;

    public Partida() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Jogador getJogadorHost() {
        return jogadorHost;
    }

    public void setJogadorHost(Jogador jogadorHost) {
        this.jogadorHost = jogadorHost;
    }

    public Esportes getEsporte() {
        return esporte;
    }

    public void setEsporte(Esportes esporte) {
        this.esporte = esporte;
    }

    public Integer getQtdTimes() {
        return qtdTimes;
    }

    public void setQtdTimes(Integer qtdTimes) {
        this.qtdTimes = qtdTimes;
    }

    public Integer getQtdJogadoresTime() {
        return qtdJogadoresTime;
    }

    public void setQtdJogadoresTime(Integer qtdJogadoresTime) {
        this.qtdJogadoresTime = qtdJogadoresTime;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public StatusPartida.StatusAtual getStatus() {
        return status;
    }

    public void setStatus(StatusPartida.StatusAtual status) {
        this.status = status;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public LocalTime getDuracao() {
        return duracao;
    }

    public void setDuracao(LocalTime duracao) {
        this.duracao = duracao;
    }

    public Integer getQtdJogos() {
        return qtdJogos;
    }

    public void setQtdJogos(Integer qtdJogos) {
        this.qtdJogos = qtdJogos;
    }
}