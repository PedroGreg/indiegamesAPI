package br.com.indiegamesapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
    // ALTERADO: a mensagem era vazia; agora informa o ID não encontrado
    public NotFoundException(Long id){
        super("Registro não encontrado, ID: " + id);
    }
}
