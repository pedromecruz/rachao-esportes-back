package com.projeto.rachao.repository;

import com.projeto.rachao.entidades.Partida;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import jakarta.persistence.LockModeType;
import java.util.Optional;

public interface PartidaRepository extends JpaRepository<Partida, Long> {
    boolean existsByJogadorHostId(Long jogadorHostId);

    // Bloqueia a partida durante a inscrição para evitar overbooking concorrente.
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select p from Partida p where p.id = :id")
    Optional<Partida> bloquearPorId(@Param("id") Long id);
}
