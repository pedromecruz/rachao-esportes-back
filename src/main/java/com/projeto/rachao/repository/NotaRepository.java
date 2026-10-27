package com.projeto.rachao.repository;

import com.projeto.rachao.entidades.Nota;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotaRepository extends JpaRepository<Nota, Long> {
}
