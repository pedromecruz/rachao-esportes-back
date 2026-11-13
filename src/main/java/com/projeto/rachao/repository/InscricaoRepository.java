package com.projeto.rachao.repository;

import com.projeto.rachao.entidades.Inscricao;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface InscricaoRepository extends JpaRepository<Inscricao, Long> {
    boolean existsByPartidaIdAndJogadorId(Long partidaId, Long jogadorId);
    boolean existsByJogadorId(Long jogadorId);
    long countByPartidaId(Long partidaId);
    List<Inscricao> findByPartidaIdOrderByIdAsc(Long partidaId);
    List<Inscricao> findByJogadorIdOrderByIdAsc(Long jogadorId);
    Optional<Inscricao> findByPartidaIdAndJogadorId(Long partidaId, Long jogadorId);
}
