package com.projeto.rachao.service;

import com.projeto.rachao.entidades.Amigos;
import com.projeto.rachao.entidades.Jogador;
import com.projeto.rachao.entidades.enums.StatusAmizade;
import com.projeto.rachao.repository.AmigosRepository;
import com.projeto.rachao.repository.JogadorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AmigosService {

    private final AmigosRepository amigosRepository;
    private final JogadorRepository jogadorRepository;

    public AmigosService(
            AmigosRepository amigosRepository, 
            JogadorRepository jogadorRepository) {
        
        this.amigosRepository = amigosRepository;
        this.jogadorRepository = jogadorRepository;
    }

    @Transactional
    public void enviarSolicitacao(Long idRemetente, Long idDestinatario) {

        if (idRemetente.equals(idDestinatario)) 
            throw new IllegalArgumentException("Você não pode enviar uma solicitação para si mesmo.");

        Jogador remetente = jogadorRepository.findById(idRemetente).orElseThrow(() -> new RuntimeException("Remetente não encontrado."));
        
        Jogador destinatario = jogadorRepository.findById(idDestinatario).orElseThrow(() -> new RuntimeException("Destinatário não encontrado."));

        // Verifica se já existe uma relação em qualquer um dos sentidos (Ida ou Volta)
        // Isso impede que A convide B, e B convide A ao mesmo tempo
        if (amigosRepository.existsByJogadorAndAmigo(remetente, destinatario) || amigosRepository.existsByJogadorAndAmigo(destinatario, remetente))
            throw new IllegalArgumentException("Já existe uma amizade ou solicitação pendente entre estes jogadores.");

        // Salva a solicitação com status PENDENTE
        Amigos solicitacao = new Amigos(remetente, destinatario, StatusAmizade.PENDENTE);
        amigosRepository.save(solicitacao);
    }

    @Transactional
    public void aceitarSolicitacao(Long idRemetente, Long idDestinatario) {

        Jogador remetente = jogadorRepository.findById(idRemetente).orElseThrow(() -> new RuntimeException("Remetente não encontrado."));
        
        Jogador destinatario = jogadorRepository.findById(idDestinatario).orElseThrow(() -> new RuntimeException("Destinatário não encontrado."));

        // Busca o convite original (onde o remetente é o Jogador e o destinatário é o Amigo)
        Amigos solicitacao = amigosRepository.findByJogadorAndAmigo(remetente, destinatario).orElseThrow(() -> new IllegalArgumentException("Solicitação não encontrada."));

        if (solicitacao.getStatus() == StatusAmizade.ACEITA)throw new IllegalArgumentException("Esta solicitação já foi aceita.");

        // 1. Atualiza o status do convite original para ACEITA
        solicitacao.setStatus(StatusAmizade.ACEITA);
        amigosRepository.save(solicitacao);

        // 2. Cria o espelhamento automático para facilitar as buscas
        // Agora o Destinatário também tem o Remetente na lista dele
        Amigos espelho = new Amigos(destinatario, remetente, StatusAmizade.ACEITA);
        amigosRepository.save(espelho);
    }
    
    @Transactional
    public void recusarSolicitacao(Long idRemetente, Long idDestinatario) {
        
        Jogador remetente = jogadorRepository.findById(idRemetente)
                .orElseThrow(() -> new RuntimeException("Remetente não encontrado."));
        
        Jogador destinatario = jogadorRepository.findById(idDestinatario)
                .orElseThrow(() -> new RuntimeException("Destinatário não encontrado."));

        Amigos solicitacao = amigosRepository.findByJogadorAndAmigo(remetente, destinatario)
                .orElseThrow(() -> new IllegalArgumentException("Solicitação não encontrada."));
                
        amigosRepository.delete(solicitacao);
    }
}