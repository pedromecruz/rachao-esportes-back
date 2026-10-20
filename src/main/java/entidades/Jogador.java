package entidades;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.util.Date;

public class Jogador {

    @Id
    @GeneratedValue
    private long id;
    private String nome;
    private String cep;
    private Date dataNascimento;
    private String descricao;

    public Jogador() {
    }

    public Jogador(String nome) {
        this.nome = nome;
    }

    public long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getCep() {
        return cep;
    }

    public Date getDataNascimento() {
        return dataNascimento;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public void setDataNascimento(Date dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
