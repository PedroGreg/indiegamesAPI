package br.com.indiegamesapi.exception;

// NOVO: erro de autenticação -> HTTP 401 (Unauthorized).
// Lançado quando o login falha ou quando um DELETE chega sem uma X-API-Key válida.
public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String message) {
        super(message);
    }
}
