package com.projeto.rachao.repository;

import com.projeto.rachao.entidades.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRepository extends JpaRepository<Chat, Long> {
}
