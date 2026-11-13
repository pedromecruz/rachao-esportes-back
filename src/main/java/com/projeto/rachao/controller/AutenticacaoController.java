package com.projeto.rachao.controller;

import com.projeto.rachao.dto.LoginRequest;
import com.projeto.rachao.dto.LoginResponse;
import com.projeto.rachao.service.AutenticacaoService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:4200")
public class AutenticacaoController {
    private final AutenticacaoService auth;

    public AutenticacaoController(AutenticacaoService auth) {
        this.auth = auth;
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest dados) {
        return auth.login(dados);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        auth.logout(authorization.substring(7).trim());
        return ResponseEntity.noContent().build();
    }
}
