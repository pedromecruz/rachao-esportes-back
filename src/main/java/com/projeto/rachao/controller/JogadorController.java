package com.projeto.rachao.controller;

import com.projeto.rachao.entidades.Jogador;
import com.projeto.rachao.dto.JogadorDTO;
import com.projeto.rachao.repository.JogadorRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jogadores")
@CrossOrigin(origins = "http://localhost:4200")
public class JogadorController {

    // Repository usado para consultar e salvar jogadores no banco.
    private final JogadorRepository repository;

    // Componente usado para criptografar as senhas antes do cadastro.
    private final PasswordEncoder passwordEncoder;

    // O Spring injeta o repository e o codificador de senha no controller.
    public JogadorController(JogadorRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    // Lista os jogadores sem devolver a senha.
    @GetMapping
    public List<JogadorDTO> listar() {
        return repository.findAll().stream().map(this::converter).toList();
    }

    // Busca um jogador pelo identificador.
    @GetMapping("/{id}")
    public ResponseEntity<JogadorDTO> buscar(@PathVariable Long id) {
        return repository.findById(id)
                .map(jogador -> ResponseEntity.ok(converter(jogador)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Cria um jogador e armazena a senha criptografada.
    @PostMapping
    public ResponseEntity<JogadorDTO> criar(@RequestBody Jogador registro) {
        registro.setSenha(passwordEncoder.encode(registro.getSenha()));
        return ResponseEntity.status(201).body(converter(repository.save(registro)));
    }

    // Atualiza os dados de um jogador existente.
    @PutMapping("/{id}")
    public ResponseEntity<JogadorDTO> atualizar(
            @PathVariable Long id,
            @RequestBody Jogador registro
    ) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        registro.setId(id);
        registro.setSenha(passwordEncoder.encode(registro.getSenha()));
        return ResponseEntity.ok(converter(repository.save(registro)));
    }

    // Exclui um jogador pelo identificador.
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> apagar(@PathVariable Long id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private JogadorDTO converter(Jogador jogador) {
        JogadorDTO dto = new JogadorDTO();

        // Copia apenas os dados públicos para evitar devolver a senha na API.
        dto.setId(jogador.getId());
        dto.setNome(jogador.getNome());
        dto.setEmail(jogador.getEmail());
        dto.setCep(jogador.getCep());
        dto.setDataNascimento(jogador.getDataNasc());
        dto.setDescricao(jogador.getDescricao());
        return dto;
    }
}
