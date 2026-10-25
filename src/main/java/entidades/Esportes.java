package entidades;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

public class Esportes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    public static enum ListaEsportes{
        Futebol,
        Volei,
        Basquete
    }

    Esportes(){}


}
