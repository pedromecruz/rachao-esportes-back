package entidades;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.util.ArrayList;
import java.util.List;

public class Times {

    @Id
    @GeneratedValue
    private long id;
    List<Jogador> jogadoresTime = new ArrayList<>();

    public long getId() {
        return id;
    }

    public List<Jogador> getJogadoresTime() {
        return jogadoresTime;
    }

    public void setJogadoresTime(List<Jogador> jogadoresTime) {
        this.jogadoresTime = jogadoresTime;
    }
}
