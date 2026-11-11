package com.projeto.rachao.controller;

import com.projeto.rachao.entidades.FormaPagamento;
import com.projeto.rachao.repository.FormaPagamentoRepository;
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
@RequestMapping("/api/formas-pagamento")
@CrossOrigin(origins = "http://localhost:4200")
/** Endpoints CRUD das formas de pagamento. */
public class FormaPagamentoController {

    // Repository responsável pelo acesso às formas de pagamento no banco.
    private final FormaPagamentoRepository repository;

    // O Spring injeta automaticamente o repository neste controller.
    public FormaPagamentoController(FormaPagamentoRepository repository) {
        this.repository = repository;
    }

    // Retorna todas as formas de pagamento cadastradas.
    @GetMapping
    public List<FormaPagamento> listar() {
        return repository.findAll();
    }

    // Procura uma forma de pagamento pelo seu ID.
    @GetMapping("/{id}")
    public ResponseEntity<FormaPagamento> buscar(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Cadastra uma nova forma de pagamento.
    @PostMapping
    public ResponseEntity<FormaPagamento> criar(@RequestBody FormaPagamento registro) {
        return ResponseEntity.status(201).body(repository.save(registro));
    }

    // Atualiza uma forma de pagamento existente.
    @PutMapping("/{id}")
    public ResponseEntity<FormaPagamento> atualizar(
            @PathVariable Long id,
            @RequestBody FormaPagamento registro
    ) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        registro.setId(id);
        return ResponseEntity.ok(repository.save(registro));
    }

    // Exclui uma forma de pagamento pelo ID.
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> apagar(@PathVariable Long id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
