package com.projeto.rachao.controller;

import com.projeto.rachao.entidades.Desempenho;
import com.projeto.rachao.repository.DesempenhoRepository;
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
@RequestMapping("/api/desempenhos")
@CrossOrigin(origins = "http://localhost:4200")
/** Endpoints CRUD do desempenho dos jogadores. */
public class DesempenhoController {

    // Repository responsável por acessar os desempenhos no banco.
    private final DesempenhoRepository repository;

    // O Spring injeta automaticamente o repository neste controller.
    public DesempenhoController(DesempenhoRepository repository) {
        this.repository = repository;
    }

    // Retorna todos os desempenhos cadastrados.
    @GetMapping
    public List<Desempenho> listar() {
        return repository.findAll();
    }

    // Procura um desempenho pelo seu ID.
    @GetMapping("/{id}")
    public ResponseEntity<Desempenho> buscar(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Cria um novo registro de desempenho.
    @PostMapping
    public ResponseEntity<Desempenho> criar(@RequestBody Desempenho registro) {
        return ResponseEntity.status(201).body(repository.save(registro));
    }

    // Atualiza um desempenho existente.
    @PutMapping("/{id}")
    public ResponseEntity<Desempenho> atualizar(
            @PathVariable Long id,
            @RequestBody Desempenho registro
    ) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        registro.setId(id);
        return ResponseEntity.ok(repository.save(registro));
    }

    // Exclui um desempenho pelo ID.
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> apagar(@PathVariable Long id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
