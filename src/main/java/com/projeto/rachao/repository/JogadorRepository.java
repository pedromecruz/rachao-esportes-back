package com.projeto.rachao.repository;

import com.projeto.rachao.entidades.Jogador;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface JogadorRepository extends JpaRepository<Jogador, Long> {
    boolean existsByEmail(String email);
    boolean existsByCpf(String cpf);
    Optional<Jogador> findByEmail(String email);
}
