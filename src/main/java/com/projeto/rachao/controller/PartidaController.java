package com.projeto.rachao.controller;

import com.projeto.rachao.entidades.Partida;
import com.projeto.rachao.repository.PartidaRepository;
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
@RequestMapping("/api/partidas")
@CrossOrigin(origins = "http://localhost:4200")
/** Endpoints CRUD das partidas. */
public class PartidaController {

    // Repository responsável pelo acesso às partidas no banco.
    private final PartidaRepository repository;

    // O Spring injeta automaticamente o repository neste controller.
    public PartidaController(PartidaRepository repository) {
        this.repository = repository;
    }

    // Retorna todas as partidas cadastradas.
    @GetMapping
    public List<Partida> listar() {
        return repository.findAll();
    }

    // Procura uma partida pelo seu ID.
    @GetMapping("/{id}")
    public ResponseEntity<Partida> buscar(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Cadastra uma nova partida.
    @PostMapping
    public ResponseEntity<Partida> criar(@RequestBody Partida registro) {
        return ResponseEntity.status(201).body(repository.save(registro));
    }

    // Atualiza uma partida existente.
    @PutMapping("/{id}")
    public ResponseEntity<Partida> atualizar(
            @PathVariable Long id,
            @RequestBody Partida registro
    ) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        registro.setId(id);
        return ResponseEntity.ok(repository.save(registro));
    }

    // Exclui uma partida pelo ID.
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> apagar(@PathVariable Long id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
