package com.projeto.rachao.controller;

import com.projeto.rachao.entidades.Habilidades;
import com.projeto.rachao.repository.HabilidadesRepository;
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
@RequestMapping("/api/habilidades")
@CrossOrigin(origins = "http://localhost:4200")
/** Endpoints CRUD das habilidades. */
public class HabilidadesController {

    // Repository responsável pelo acesso às habilidades no banco.
    private final HabilidadesRepository repository;

    // O Spring injeta automaticamente o repository neste controller.
    public HabilidadesController(HabilidadesRepository repository) {
        this.repository = repository;
    }

    // Retorna todas as habilidades cadastradas.
    @GetMapping
    public List<Habilidades> listar() {
        return repository.findAll();
    }

    // Procura uma habilidade pelo seu ID.
    @GetMapping("/{id}")
    public ResponseEntity<Habilidades> buscar(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Cadastra uma nova habilidade.
    @PostMapping
    public ResponseEntity<Habilidades> criar(@RequestBody Habilidades registro) {
        return ResponseEntity.status(201).body(repository.save(registro));
    }

    // Atualiza uma habilidade existente.
    @PutMapping("/{id}")
    public ResponseEntity<Habilidades> atualizar(
            @PathVariable Long id,
            @RequestBody Habilidades registro
    ) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(repository.save(registro));
    }

    // Exclui uma habilidade pelo ID.
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> apagar(@PathVariable Long id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
