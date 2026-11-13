
package com.projeto.rachao.entidades;

import jakarta.persistence.*;

@Entity
@Table(name = "ESPORTES")
public class Esportes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "ESPORTE", nullable = false, unique = true)
    private EsportesLista esporte;

    public enum EsportesLista {
        BASQUETE,
        VOLEI,
        FUTEBOL
    }

    public Esportes() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EsportesLista getEsporte() {
        return esporte;
    }

    public void setEsporte(EsportesLista esporte) {
        this.esporte = esporte;
    }
}
