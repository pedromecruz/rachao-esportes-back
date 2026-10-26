package com.projeto.rachao.entidades;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Posicoes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    public static enum PosicaoFutebol {
        Goleiro,
        Zagueiro,
        LateralDireito,
        LateralEsquerdo,
        Volante,
        SegundoVolante,
        MeiaArmador,
        CentroAvante,
        PontaDireita,
        PontaEsquerda
    }

    public static enum PosicaoVolei {
        Libero,
        Ponteiro,
        Levantador,
        Central,
        Oposto
    }

    public static enum PosicaoBasquete {
        Pivo,
        Ala,
        AlaPivo,
        Armador,
        AlaArmador
    }
}
