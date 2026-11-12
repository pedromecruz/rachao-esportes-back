package com.projeto.rachao.entidades;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "MENSAGEM")
public class Mensagem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "JOGADOR_ID", nullable = false)
    private Jogador jogador;

    @ManyToOne(optional = false)
    @JoinColumn(name = "CHAT_ID", nullable = false)
    private Chat chat;

    @Column(name = "MENSAGEM", nullable = false)
    private String textoMensagem;

    @Column(name = "HORA")
    private LocalDateTime hora;

    public Mensagem() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Jogador getJogador() {
        return jogador;
    }

    public void setJogador(Jogador jogador) {
        this.jogador = jogador;
    }

    public Chat getChat() {
        return chat;
    }

    public void setChat(Chat chat) {
        this.chat = chat;
    }

    public String getTextoMensagem() {
        return textoMensagem;
    }

    public void setTextoMensagem(String textoMensagem) {
        this.textoMensagem = textoMensagem;
    }

    public LocalDateTime getHora() {
        return hora;
    }

    public void setHora(LocalDateTime hora) {
        this.hora = hora;
    }
}