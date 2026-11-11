package com.projeto.rachao.controller;

import com.projeto.rachao.entidades.JogadorTime;
import com.projeto.rachao.repository.JogadorTimeRepository;
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
@RequestMapping("/api/jogadores-times")
@CrossOrigin(origins = "http://localhost:4200")
/** Endpoints CRUD da associação entre jogadores e times. */
public class JogadorTimeController {

    // Repository responsável pelo acesso à relação entre jogadores e times.
    private final JogadorTimeRepository repository;

    // O Spring injeta automaticamente o repository neste controller.
    public JogadorTimeController(JogadorTimeRepository repository) {
        this.repository = repository;
    }

    // Retorna todas as relações entre jogadores e times.
    @GetMapping
    public List<JogadorTime> listar() {
        return repository.findAll();
    }

    // Procura uma relação jogador-time pelo seu ID.
    @GetMapping("/{id}")
    public ResponseEntity<JogadorTime> buscar(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Cadastra um jogador em um time.
    @PostMapping
    public ResponseEntity<JogadorTime> criar(@RequestBody JogadorTime registro) {
        return ResponseEntity.status(201).body(repository.save(registro));
    }

    // Atualiza uma relação existente.
    @PutMapping("/{id}")
    public ResponseEntity<JogadorTime> atualizar(
            @PathVariable Long id,
            @RequestBody JogadorTime registro
    ) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        registro.setId(id);
        return ResponseEntity.ok(repository.save(registro));
    }

    // Remove um jogador de um time pelo ID da relação.
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> apagar(@PathVariable Long id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
