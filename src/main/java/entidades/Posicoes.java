package entidades;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

public class Posicoes {

    @Id
    @GeneratedValue
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
