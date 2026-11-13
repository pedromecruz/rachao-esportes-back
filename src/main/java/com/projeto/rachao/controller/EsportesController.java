package com.projeto.rachao.controller;

import com.projeto.rachao.entidades.Esportes;
import com.projeto.rachao.service.EsporteService;
import java.util.List;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/esportes")
@CrossOrigin(origins = "http://localhost:4200")
public class EsportesController {
    private final EsporteService service;

    public EsportesController(EsporteService service) {
        this.service = service;
    }

    @GetMapping
    public List<Esportes> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public Esportes buscar(@PathVariable Long id) {
        return service.buscar(id);
    }
}
