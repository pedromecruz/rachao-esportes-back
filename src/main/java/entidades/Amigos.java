package entidades;

import jakarta.persistence.*;

public class Amigos {

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
            name = "jogador_id",
            referencedColumnName = "id",
            nullable = false)
    private Jogador amigo;

    public Amigos() {
    }

    public Jogador getAmigo() {
        return amigo;
    }

    public void setAmigo(Jogador amigo) {
        this.amigo = amigo;
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
