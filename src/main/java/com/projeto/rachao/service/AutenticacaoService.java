package com.projeto.rachao.service;

import com.projeto.rachao.dto.LoginRequest;
import com.projeto.rachao.dto.LoginResponse;
import com.projeto.rachao.entidades.Jogador;
import com.projeto.rachao.repository.JogadorRepository;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

/** Login de MVP: token aleatório mantido em memória, com validade de 12 horas.
 * Não é JWT. Reiniciar o servidor invalida todos os tokens.
 */
@Service
public class AutenticacaoService {
    private static final long VALIDADE_SEGUNDOS = 12 * 60 * 60;
    private final JogadorRepository jogadores;
    private final PasswordEncoder encoder;
    private final SecureRandom random = new SecureRandom();
    private final Map<String, Sessao> sessoes = new ConcurrentHashMap<>();

    private record Sessao(Long jogadorId, Instant expiraEm) {}

    public AutenticacaoService(JogadorRepository jogadores, PasswordEncoder encoder) {
        this.jogadores = jogadores;
        this.encoder = encoder;
    }

    public LoginResponse login(LoginRequest dados) {
        if (dados == null || dados.email() == null || dados.senha() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "E-mail e senha são obrigatórios.");
        }
        Jogador jogador = jogadores.findByEmail(dados.email().trim().toLowerCase())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciais inválidas."));
        if (!encoder.matches(dados.senha(), jogador.getSenha())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciais inválidas.");
        }
        byte[] bytes = new byte[32];
        random.nextBytes(bytes);
        String token = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
        sessoes.put(token, new Sessao(jogador.getId(), Instant.now().plusSeconds(VALIDADE_SEGUNDOS)));
        return new LoginResponse(token, jogador.getId(), jogador.getNome(), VALIDADE_SEGUNDOS);
    }

    public Long validarToken(String token) {
        if (token == null || token.isBlank()) return null;
        Sessao sessao = sessoes.get(token);
        if (sessao == null) return null;
        if (!sessao.expiraEm().isAfter(Instant.now())) {
            sessoes.remove(token);
            return null;
        }
        return sessao.jogadorId();
    }

    public void logout(String token) {
        sessoes.remove(token);
    }
}
