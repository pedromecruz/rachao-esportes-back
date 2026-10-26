package com.projeto.rachao.entidades;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Habilidades {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    public static enum ListaHabilidades {
        ForçaFisica,
        Velocidade,
        PuloVertical,
        Inteligencia,
        Defesa,
        Ataque,
        Criatividade,
        Estamina,
        Finalização,
        Passe
    }

    @Enumerated(EnumType.STRING)
    private ListaHabilidades habilidade;

    public Habilidades() {
    }

    public Habilidades(ListaHabilidades habilidade) {
        this.habilidade = habilidade;
    }

    public long getId() {
        return id;
    }

    public ListaHabilidades getHabilidade() {
        return habilidade;
    }

    public void setHabilidade(ListaHabilidades habilidade) {
        this.habilidade = habilidade;
    }





}
