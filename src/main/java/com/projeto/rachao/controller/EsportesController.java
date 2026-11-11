package com.projeto.rachao.controller;

import com.projeto.rachao.entidades.Esportes;
import com.projeto.rachao.repository.EsportesRepository;
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
@RequestMapping("/api/esportes")
@CrossOrigin(origins = "http://localhost:4200")
/** Endpoints CRUD dos esportes disponíveis. */
public class EsportesController {

    // Repository responsável pelo acesso aos esportes no banco.
    private final EsportesRepository repository;

    // O Spring injeta automaticamente o repository neste controller.
    public EsportesController(EsportesRepository repository) {
        this.repository = repository;
    }

    // Retorna todos os esportes cadastrados.
    @GetMapping
    public List<Esportes> listar() {
        return repository.findAll();
    }

    // Procura um esporte pelo seu ID.
    @GetMapping("/{id}")
    public ResponseEntity<Esportes> buscar(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Cria um novo esporte.
    @PostMapping
    public ResponseEntity<Esportes> criar(@RequestBody Esportes registro) {
        return ResponseEntity.status(201).body(repository.save(registro));
    }

    // Atualiza um esporte existente.
    @PutMapping("/{id}")
    public ResponseEntity<Esportes> atualizar(
            @PathVariable Long id,
            @RequestBody Esportes registro
    ) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(repository.save(registro));
    }

    // Exclui um esporte pelo ID.
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> apagar(@PathVariable Long id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
