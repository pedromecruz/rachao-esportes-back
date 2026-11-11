package com.projeto.rachao.controller;

import com.projeto.rachao.entidades.Mensagem;
import com.projeto.rachao.repository.MensagemRepository;
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
@RequestMapping("/api/mensagens")
@CrossOrigin(origins = "http://localhost:4200")
/** Endpoints CRUD das mensagens dos chats. */
public class MensagemController {

    // Repository responsável pelo acesso às mensagens no banco.
    private final MensagemRepository repository;

    // O Spring injeta automaticamente o repository neste controller.
    public MensagemController(MensagemRepository repository) {
        this.repository = repository;
    }

    // Retorna todas as mensagens cadastradas.
    @GetMapping
    public List<Mensagem> listar() {
        return repository.findAll();
    }

    // Procura uma mensagem pelo seu ID.
    @GetMapping("/{id}")
    public ResponseEntity<Mensagem> buscar(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Cadastra uma nova mensagem.
    @PostMapping
    public ResponseEntity<Mensagem> criar(@RequestBody Mensagem registro) {
        return ResponseEntity.status(201).body(repository.save(registro));
    }

    // Atualiza uma mensagem existente.
    @PutMapping("/{id}")
    public ResponseEntity<Mensagem> atualizar(
            @PathVariable Long id,
            @RequestBody Mensagem registro
    ) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        registro.setId(id);
        return ResponseEntity.ok(repository.save(registro));
    }

    // Exclui uma mensagem pelo ID.
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> apagar(@PathVariable Long id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
