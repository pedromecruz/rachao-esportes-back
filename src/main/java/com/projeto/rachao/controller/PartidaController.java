package com.projeto.rachao.controller;

import com.projeto.rachao.dto.PartidaDTO;
import com.projeto.rachao.dto.PartidaRequest;
import com.projeto.rachao.service.PartidaService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.projeto.rachao.config.AuthContext;

@RestController
@RequestMapping("/api/partidas")
@CrossOrigin(origins = "http://localhost:4200")
public class PartidaController {
    private final PartidaService service;

    public PartidaController(PartidaService service) {
        this.service = service;
    }

    @GetMapping
    public List<PartidaDTO> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public PartidaDTO buscar(@PathVariable Long id) {
        return service.buscar(id);
    }

    @PostMapping
    public ResponseEntity<PartidaDTO> criar(@RequestBody PartidaRequest dados, HttpServletRequest request) {
        return ResponseEntity.status(201).body(service.criarPartida(dados, AuthContext.id(request)));
    }

    @PutMapping("/{id}")
    public PartidaDTO atualizar(@PathVariable Long id, @RequestBody PartidaRequest dados,
            HttpServletRequest request) {
        return service.atualizar(id, dados, AuthContext.id(request));
    }

    @PostMapping("/{id}/cancelar")
    public PartidaDTO cancelar(@PathVariable Long id, HttpServletRequest request) {
        return service.cancelar(id, AuthContext.id(request));
    }

    /** DELETE mantém o registro e marca a partida como CANCELADA. */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> apagar(@PathVariable Long id, HttpServletRequest request) {
        service.cancelar(id, AuthContext.id(request));
        return ResponseEntity.noContent().build();
    }
}
