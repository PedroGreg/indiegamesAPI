package br.com.indiegamesapi.exception;

public class BadRequestException extends RuntimeException {
    public BadRequestException() {
        super("É necessário enviar o corpo da requisição neste endpoint");
    }
    public BadRequestException(String message) {
        super(message);
    }
}
