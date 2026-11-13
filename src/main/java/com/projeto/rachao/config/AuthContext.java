package com.projeto.rachao.config;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/** O identificador é definido exclusivamente pelo filtro de autenticação. */
public final class AuthContext {
    private static final String CHAVE = AuthContext.class.getName() + ".jogadorId";
    private AuthContext() {}

    public static void definir(HttpServletRequest request, Long jogadorId) {
        request.setAttribute(CHAVE, jogadorId);
    }

    public static Long id(HttpServletRequest request) {
        Object jogadorId = request.getAttribute(CHAVE);
        if (!(jogadorId instanceof Long)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Faça login para continuar.");
        }
        return (Long) jogadorId;
    }
}
