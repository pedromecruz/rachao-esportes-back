package entidades;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

public class Habilidades {

    @Id
    @GeneratedValue
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





}
