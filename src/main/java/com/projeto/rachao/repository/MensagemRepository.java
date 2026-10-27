package com.projeto.rachao.repository;

import com.projeto.rachao.entidades.Mensagem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MensagemRepository extends JpaRepository<Mensagem, Long> {
}
