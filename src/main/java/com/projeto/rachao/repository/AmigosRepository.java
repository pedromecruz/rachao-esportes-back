package com.projeto.rachao.repository;

import com.projeto.rachao.entidades.Amigos;
import com.projeto.rachao.entidades.Jogador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AmigosRepository extends JpaRepository<Amigos, Long> {

    // Método para verificar se já existe uma amizade (ou solicitação) entre os dois
    boolean existsByJogadorAndAmigo(Jogador jogador, Jogador amigo);

    // Método para buscar a solicitação específica na hora de aceitar ou recusar
    Optional<Amigos> findByJogadorAndAmigo(Jogador jogador, Jogador amigo);

}