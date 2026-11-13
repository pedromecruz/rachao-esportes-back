package com.projeto.rachao.config;

import com.projeto.rachao.service.AutenticacaoService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.stereotype.Component;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.OncePerRequestFilter;

/** Protege as rotas /api, permitindo apenas cadastro, login e catálogo público. */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 10)
public class AuthFilter extends OncePerRequestFilter {
    private final AutenticacaoService auth;

    public AuthFilter(AutenticacaoService auth) { this.auth = auth; }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {
        String path = request.getRequestURI();
        String method = request.getMethod();
        if (!path.startsWith("/api/") || "OPTIONS".equalsIgnoreCase(method)
                || ("POST".equalsIgnoreCase(method) && ("/api/auth/login".equals(path)
                   || "/api/jogadores".equals(path)))
                || ("GET".equalsIgnoreCase(method)
                    && (path.equals("/api/esportes") || path.matches("/api/esportes/\\d+")
                       || path.equals("/api/partidas") || path.matches("/api/partidas/\\d+")))) {
            chain.doFilter(request, response);
            return;
        }
        String authorization = request.getHeader("Authorization");
        String token = authorization != null && authorization.startsWith("Bearer ")
                ? authorization.substring(7).trim() : null;
        Long id = auth.validarToken(token);
        if (id == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token ausente, inválido ou expirado.");
            return;
        }
        AuthContext.definir(request, id);
        chain.doFilter(request, response);
    }
}
