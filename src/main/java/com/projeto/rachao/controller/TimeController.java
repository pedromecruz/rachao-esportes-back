package com.projeto.rachao.controller;

import com.projeto.rachao.entidades.Time;
import com.projeto.rachao.repository.TimesRepository;
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
@RequestMapping("/api/times")
@CrossOrigin(origins = "http://localhost:4200")
/** Endpoints CRUD dos times. */
public class TimeController {

    // Repository responsável pelo acesso aos times no banco.
    private final TimesRepository repository;

    // O Spring injeta automaticamente o repository neste controller.
    public TimeController(TimesRepository repository) {
        this.repository = repository;
    }

    // Retorna todos os times cadastrados.
    @GetMapping
    public List<Time> listar() {
        return repository.findAll();
    }

    // Procura um time pelo seu ID.
    @GetMapping("/{id}")
    public ResponseEntity<Time> buscar(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Cadastra um novo time.
    @PostMapping
    public ResponseEntity<Time> criar(@RequestBody Time registro) {
        return ResponseEntity.status(201).body(repository.save(registro));
    }

    // Atualiza um time existente.
    @PutMapping("/{id}")
    public ResponseEntity<Time> atualizar(
            @PathVariable Long id,
            @RequestBody Time registro
    ) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        registro.setId(id);
        return ResponseEntity.ok(repository.save(registro));
    }

    // Exclui um time pelo ID.
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> apagar(@PathVariable Long id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
