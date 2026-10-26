package com.projeto.rachao.entidades;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Esportes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    public static enum ListaEsportes{
        Futebol,
        Volei,
        Basquete
    }

    @Enumerated(EnumType.STRING)
    private ListaEsportes nome;

    public Esportes() {
    }

    public Esportes(ListaEsportes nome) {
        this.nome = nome;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public ListaEsportes getNome() {
        return nome;
    }

    public void setNome(ListaEsportes nome) {
        this.nome = nome;
    }


}
