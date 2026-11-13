
package com.projeto.rachao.controller;

import java.util.List;
import com.projeto.rachao.config.AuthContext;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.projeto.rachao.dto.JogadorCadastroDTO;
import com.projeto.rachao.dto.JogadorDTO;
import com.projeto.rachao.dto.JogadorPerfilDTO;
import com.projeto.rachao.service.JogadorService;

@RestController
@RequestMapping("/api/jogadores")
@CrossOrigin(origins = "http://localhost:4200")
public class JogadorController {

    private final JogadorService jogadorService;

    // Injecao de dependencia pelo construtor
    public JogadorController(JogadorService jogadorService) {
        this.jogadorService = jogadorService;
    }

    // LISTAR JOGADORES
    @GetMapping
    public ResponseEntity<List<JogadorDTO>> listar() {

        return ResponseEntity.ok(jogadorService.listar());
    }

    // BUSCAR JOGADOR POR ID
    @GetMapping("/{id}")
    public ResponseEntity<JogadorDTO> buscar(@PathVariable Long id) {

        return ResponseEntity.ok(jogadorService.buscarPorId(id));
    }

    // CADASTRAR JOGADOR
    @PostMapping
    public ResponseEntity<JogadorDTO> cadastrar(
            @RequestBody JogadorCadastroDTO dto) {

        JogadorDTO jogador = jogadorService.cadastrar(dto);

        return ResponseEntity.status(201).body(jogador);
    }

    // ATUALIZAR PERFIL
    @PutMapping("/{id}/perfil")
    public ResponseEntity<JogadorDTO> atualizarPerfil(
            @PathVariable Long id,
            @RequestBody JogadorPerfilDTO dto, HttpServletRequest request) {
        if (!AuthContext.id(request).equals(id)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Só é possível alterar o próprio perfil.");
        }

        JogadorDTO jogador = jogadorService.atualizarPerfil(id, dto);

        return ResponseEntity.ok(jogador);
    }

    // EXCLUIR JOGADOR
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id, HttpServletRequest request) {
        if (!AuthContext.id(request).equals(id)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Só é possível excluir a própria conta.");
        }

        jogadorService.excluir(id);

        return ResponseEntity.noContent().build();
    }
}
