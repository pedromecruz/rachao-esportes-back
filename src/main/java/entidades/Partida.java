package entidades;

import jakarta.persistence.*;

import java.util.Date;

public class Partida {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToMany
    @JoinColumn(
            name = "jogador_id",
            referencedColumnName = "id",
            nullable = false)
    private Jogador jogadorHost;

    @OneToOne
    @JoinColumn(
            name = "esporte_id",
            referencedColumnName = "id",
            nullable = false)
    private Esportes esporte;

    private String local;
    private int quantidadeTimes;
    private Date DataJogo;
    private String statusPartida;
    private int duracaoPartida;
    private int duracaoReserva;
    private int quantidadeJogos;

    public Partida(){}

    public int getQuantidadeJogos() {
        return quantidadeJogos;
    }

    public void setQuantidadeJogos(int quantidadeJogos) {
        this.quantidadeJogos = quantidadeJogos;
    }

    public int getDuracaoReserva() {
        return duracaoReserva;
    }

    public void setDuracaoReserva(int duracaoReserva) {
        this.duracaoReserva = duracaoReserva;
    }

    public int getDuracaoPartida() {
        return duracaoPartida;
    }

    public void setDuracaoPartida(int duracaoPartida) {
        this.duracaoPartida = duracaoPartida;
    }

    public String getStatusPartida() {
        return statusPartida;
    }

    public void setStatusPartida(String statusPartida) {
        this.statusPartida = statusPartida;
    }

    public Date getDataJogo() {
        return DataJogo;
    }

    public void setDataJogo(Date dataJogo) {
        DataJogo = dataJogo;
    }

    public int getQuantidadeTimes() {
        return quantidadeTimes;
    }

    public void setQuantidadeTimes(int quantidadeTimes) {
        this.quantidadeTimes = quantidadeTimes;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public Esportes getEsporte() {
        return esporte;
    }

    public void setEsporte(Esportes esporte) {
        this.esporte = esporte;
    }

    public Jogador getJogadorHost() {
        return jogadorHost;
    }

    public void setJogadorHost(Jogador jogadorHost) {
        this.jogadorHost = jogadorHost;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
