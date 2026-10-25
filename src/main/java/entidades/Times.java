package entidades;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

public class Times {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToMany
    @JoinColumn(
            name = "jogador_id",
            referencedColumnName = "id",
            nullable = false)
    private Jogador jogador;

    @ManyToMany
    @JoinColumn(
            name = "partida_id",
            referencedColumnName = "id",
            nullable = false)
    private Partida partida;

    public long getId() {
        return id;
    }

    public void setId(long id) {
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


}

