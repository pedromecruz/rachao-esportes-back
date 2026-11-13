package com.projeto.rachao.controller;

import com.projeto.rachao.config.AuthContext;
import com.projeto.rachao.dto.InscricaoDTO;
import com.projeto.rachao.service.InscricaoService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class InscricaoController {
    private final InscricaoService service;

    public InscricaoController(InscricaoService service) {
        this.service = service;
    }

    @PostMapping("/api/partidas/{partidaId}/inscricoes")
    public ResponseEntity<InscricaoDTO> entrar(
            @PathVariable Long partidaId,
            HttpServletRequest request) {

        Long jogadorId = AuthContext.id(request);

        InscricaoDTO inscricao = service.entrar(partidaId, jogadorId);

        return ResponseEntity.status(201).body(inscricao);
    }

    @DeleteMapping("/api/partidas/{partidaId}/inscricoes/{jogadorId}")
    public ResponseEntity<Void> sair(@PathVariable Long partidaId, @PathVariable Long jogadorId,
            HttpServletRequest request) {
        if (!AuthContext.id(request).equals(jogadorId)) {
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.FORBIDDEN, "Só é permitido cancelar a própria inscrição.");
        }
        service.sair(partidaId, jogadorId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/api/partidas/{partidaId}/inscricoes")
    public List<InscricaoDTO> listar(@PathVariable Long partidaId) {
        return service.listarDaPartida(partidaId);
    }

    @GetMapping("/api/jogadores/{jogadorId}/partidas")
    public List<InscricaoDTO> minhasPartidas(@PathVariable Long jogadorId, HttpServletRequest request) {
        if (!AuthContext.id(request).equals(jogadorId)) {
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.FORBIDDEN,
                    "Não é possível consultar inscrições de outro jogador.");
        }
        return service.listarDoJogador(jogadorId);
    }
}
