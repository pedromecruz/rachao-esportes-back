package entidades;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

public class Esportes {

    @Id
    @GeneratedValue
    private long id;

    public static enum ListaEsportes{
        Futebol,
        Volei,
        Basquete
    }
}
