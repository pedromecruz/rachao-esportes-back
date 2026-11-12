package com.projeto.rachao.entidades;

import jakarta.persistence.*;

@Entity
@Table(name = "TIMES")
public class Time {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "ID_JOGO", nullable = false)
    private Jogo jogo;

    @Column(name = "NOME")
    private String nome;

    public Time() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Jogo getJogo() {
        return jogo;
    }

    public void setJogo(Jogo jogo) {
        this.jogo = jogo;
    }
}
