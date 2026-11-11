package com.projeto.rachao.controller;

import com.projeto.rachao.entidades.Posicoes;
import com.projeto.rachao.repository.PosicoesRepository;
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
@RequestMapping("/api/posicoes")
@CrossOrigin(origins = "http://localhost:4200")
/** Endpoints CRUD das posições esportivas. */
public class PosicoesController {

    // Repository responsável pelo acesso às posições no banco.
    private final PosicoesRepository repository;

    // O Spring injeta automaticamente o repository neste controller.
    public PosicoesController(PosicoesRepository repository) {
        this.repository = repository;
    }

    // Retorna todas as posições cadastradas.
    @GetMapping
    public List<Posicoes> listar() {
        return repository.findAll();
    }

    // expressao lambda pra quando nao achar o registro
    // Procura uma posição pelo seu ID.
    @GetMapping("/{id}")
    public ResponseEntity<Posicoes> buscar(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Cadastra uma nova posição.
    @PostMapping
    public ResponseEntity<Posicoes> criar(@RequestBody Posicoes registro) {
        return ResponseEntity.status(201).body(repository.save(registro));
    }

    // Atualiza uma posição existente.
    @PutMapping("/{id}")
    public ResponseEntity<Posicoes> atualizar(
            @PathVariable Long id,
            @RequestBody Posicoes registro
    ) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(repository.save(registro));
    }

    // Exclui uma posição pelo ID.
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> apagar(@PathVariable Long id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
