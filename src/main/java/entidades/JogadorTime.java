package entidades;

import jakarta.persistence.*;

public class JogadorTime {

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
            name = "time_id",
            referencedColumnName = "id",
            nullable = false)
    private Times time;

    public JogadorTime() {
    }

    public Times getTime() {
        return time;
    }

    public void setTime(Times time) {
        this.time = time;
    }

    public Jogador getJogador() {
        return jogador;
    }

    public void setJogador(Jogador jogador) {
        this.jogador = jogador;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
