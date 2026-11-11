package com.projeto.rachao.controller;

import com.projeto.rachao.entidades.Chat;
import com.projeto.rachao.repository.ChatRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/chats")
@CrossOrigin(origins = "http://localhost:4200")
/** Endpoints CRUD dos chats associados às partidas. */
public class ChatController {

    // Repository responsável pelo acesso aos chats no banco.
    private final ChatRepository repository;

    // O Spring injeta automaticamente o repository neste controller.
    public ChatController(ChatRepository repository) {
        this.repository = repository;
    }

    // Retorna todos os chats cadastrados.
    @GetMapping
    public List<Chat> listar() {
        return repository.findAll();
    }

    // Procura um chat pelo seu ID.
    @GetMapping("/{id}")
    public ResponseEntity<Chat> buscar(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Cria um novo chat a partir dos dados recebidos no JSON.
    @PostMapping
    public ResponseEntity<Chat> criar(@RequestBody Chat registro) {
        return ResponseEntity.status(201).body(repository.save(registro));
    }

    // Atualiza os dados de um chat existente.
    @PutMapping("/{id}")
    public ResponseEntity<Chat> atualizar(
            @PathVariable Long id,
            @RequestBody Chat registro
    ) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        registro.setId(id);
        return ResponseEntity.ok(repository.save(registro));
    }

    // Exclui um chat pelo ID.
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> apagar(@PathVariable Long id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
