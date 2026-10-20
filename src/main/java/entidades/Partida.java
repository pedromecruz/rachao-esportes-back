package entidades;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.util.Date;

public class Partida {

    @Id
    @GeneratedValue
    private long id;
    private String host;
    private String local;
    private int quantidadeTimes;
    private Date DataJogo;
    private String esportePartida;
    private String statusPartida;
    private int duracaoPartida;
    private int duracaoReserva;
    private int quantidadeJogos;

    public Partida(){}

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public int getQuantidadeTimes() {
        return quantidadeTimes;
    }

    public void setQuantidadeTimes(int quantidadeTimes) {
        this.quantidadeTimes = quantidadeTimes;
    }

    public Date getDataJogo() {
        return DataJogo;
    }

    public void setDataJogo(Date dataJogo) {
        DataJogo = dataJogo;
    }

    public String getEsportePartida() {
        return esportePartida;
    }

    public void setEsportePartida(String esportePartida) {
        this.esportePartida = esportePartida;
    }

    public String getStatusPartida() {
        return statusPartida;
    }

    public void setStatusPartida(String statusPartida) {
        this.statusPartida = statusPartida;
    }

    public int getDuracaoPartida() {
        return duracaoPartida;
    }

    public void setDuracaoPartida(int duracaoPartida) {
        this.duracaoPartida = duracaoPartida;
    }

    public int getDuracaoReserva() {
        return duracaoReserva;
    }

    public void setDuracaoReserva(int duracaoReserva) {
        this.duracaoReserva = duracaoReserva;
    }

    public int getQuantidadeJogos() {
        return quantidadeJogos;
    }

    public void setQuantidadeJogos(int quantidadeJogos) {
        this.quantidadeJogos = quantidadeJogos;
    }

    public long getId() {
        return id;
    }


}
