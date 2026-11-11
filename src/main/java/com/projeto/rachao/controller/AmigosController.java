package com.projeto.rachao.controller;

import com.projeto.rachao.entidades.Amigos;
import com.projeto.rachao.repository.AmigosRepository;
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
@RequestMapping("/api/amigos")
@CrossOrigin(origins = "http://localhost:4200")
/** Endpoints CRUD de amizades entre jogadores. */
public class AmigosController {

    // Repository responsável por acessar os registros de amizades no banco.
    private final AmigosRepository repository;

    // O Spring injeta automaticamente o repository neste controller.
    public AmigosController(AmigosRepository repository) {
        this.repository = repository;
    }

    // Retorna todas as amizades cadastradas.
    @GetMapping
    public List<Amigos> listar() {
        return repository.findAll();
    }

    // Procura uma amizade pelo seu ID.
    @GetMapping("/{id}")
    public ResponseEntity<Amigos> buscar(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Recebe os dados da amizade e cria um novo registro.
    @PostMapping
    public ResponseEntity<Amigos> criar(@RequestBody Amigos registro) {
        return ResponseEntity.status(201).body(repository.save(registro));
    }

    // Atualiza uma amizade que já existe.
    @PutMapping("/{id}")
    public ResponseEntity<Amigos> atualizar(
            @PathVariable Long id,
            @RequestBody Amigos registro
    ) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        registro.setId(id);
        return ResponseEntity.ok(repository.save(registro));
    }

    // Exclui uma amizade pelo ID.
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> apagar(@PathVariable Long id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
