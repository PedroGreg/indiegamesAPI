package br.com.indiegamesapi.exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;
import java.util.stream.Collectors;

// Tratamento GLOBAL de exceções: qualquer exceção lançada nos controllers/services
// vira uma resposta JSON no formato ExceptionResponseModel (timestamp, message, details).
@RestControllerAdvice
public class ExceptionHandlerModel extends ResponseEntityExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ExceptionResponseModel> notFound(WebRequest request, Exception e){
        return build(HttpStatus.NOT_FOUND, e.getMessage(), request);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ExceptionResponseModel> badRequest(WebRequest request, Exception e){
        return build(HttpStatus.BAD_REQUEST, e.getMessage(), request);
    }

    // NOVO: login inválido ou DELETE sem X-API-Key válida -> 401
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ExceptionResponseModel> unauthorized(WebRequest request, Exception e){
        return build(HttpStatus.UNAUTHORIZED, e.getMessage(), request);
    }

    // NOVO: a validação da ENTIDADE falhou (o Hibernate checa o Bean Validation antes do INSERT) -> 400.
    // Normalmente o RequestDTO barra antes; aqui é a "rede de segurança".
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ExceptionResponseModel> constraintViolation(WebRequest request, ConstraintViolationException e){
        String message = e.getConstraintViolations().stream()
                .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                .collect(Collectors.joining("; "));
        return build(HttpStatus.BAD_REQUEST, message, request);
    }

    // Registro em uso (ex.: deletar categoria usada por um game) -> 409
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ExceptionResponseModel> conflict(WebRequest request, Exception e){
        return build(HttpStatus.CONFLICT, "O registro está em uso por outro recurso (ou já existe) e a operação não pode ser feita", request);
    }

    // Qualquer erro não previsto -> 500 com mensagem limpa (o detalhe técnico vai só para o log)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponseModel> generic(WebRequest request, Exception e){
        logger.error("Erro inesperado", e);
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno no servidor, tente novamente mais tarde", request);
    }

    // Erros padrão do Spring (validação do @Valid, JSON inválido, header obrigatório faltando, ID não numérico...)
    // passam por aqui e também viram ExceptionResponseModel
    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatusCode statusCode, WebRequest request) {
        ResponseEntity<Object> defaultResponse = super.handleExceptionInternal(ex, body, headers, statusCode, request);
        if (defaultResponse == null) {
            return null;
        }
        ExceptionResponseModel response = new ExceptionResponseModel(new Date(), buildMessage(ex, defaultResponse.getBody()), request.getDescription(false));
        return new ResponseEntity<>(response, defaultResponse.getHeaders(), defaultResponse.getStatusCode());
    }

    // Monta a resposta padrão (evita repetir o mesmo código em cada handler)
    private ResponseEntity<ExceptionResponseModel> build(HttpStatus status, String message, WebRequest request){
        return new ResponseEntity<>(new ExceptionResponseModel(new Date(), message, request.getDescription(false)), status);
    }

    // Mensagem legível para cada tipo de erro padrão do Spring
    private String buildMessage(Exception ex, Object body) {
        if (ex instanceof MethodArgumentNotValidException validationEx) {
            return validationEx.getBindingResult().getFieldErrors().stream()
                    .map(error -> error.getField() + ": " + error.getDefaultMessage())
                    .collect(Collectors.joining("; "));
        }
        if (ex instanceof HttpMessageNotReadableException) {
            return "JSON inválido: verifique a sintaxe, os nomes dos campos e os tipos dos valores (ex.: status deve ser DEVELOPMENT, EARLY_ACCESS ou RELEASED)";
        }
        // NOVO: POST sem o header X-Idempotency-Key cai aqui
        if (ex instanceof MissingRequestHeaderException headerEx) {
            return "O header " + headerEx.getHeaderName() + " é obrigatório nesta requisição";
        }
        if (body instanceof ProblemDetail problemDetail && problemDetail.getDetail() != null) {
            return problemDetail.getDetail();
        }
        return ex.getMessage();
    }
}
