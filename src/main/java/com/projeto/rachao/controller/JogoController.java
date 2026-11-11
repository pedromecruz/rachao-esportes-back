package com.projeto.rachao.controller;

import com.projeto.rachao.entidades.Jogo;
import com.projeto.rachao.repository.JogoRepository;
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
@RequestMapping("/api/jogos")
@CrossOrigin(origins = "http://localhost:4200")
public class JogoController {

    // Repository responsável pelo acesso aos jogos no banco.
    private final JogoRepository repository;

    // O Spring injeta automaticamente o repository neste controller.
    public JogoController(JogoRepository repository) {
        this.repository = repository;
    }

    // Lista todos os jogos cadastrados.
    @GetMapping
    public List<Jogo> listar() {
        return repository.findAll();
    }

    // Busca um jogo pelo identificador.
    @GetMapping("/{id}")
    public ResponseEntity<Jogo> buscar(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Cria um novo jogo a partir do JSON enviado pelo cliente.
    @PostMapping
    public ResponseEntity<Jogo> criar(@RequestBody Jogo jogo) {
        return ResponseEntity.status(201).body(repository.save(jogo));
    }

    // Atualiza um jogo existente.
    @PutMapping("/{id}")
    public ResponseEntity<Jogo> atualizar(
            @PathVariable Long id,
            @RequestBody Jogo jogo
    ) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        jogo.setId(id);
        return ResponseEntity.ok(repository.save(jogo));
    }

    // Remove um jogo pelo identificador.
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> apagar(@PathVariable Long id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
