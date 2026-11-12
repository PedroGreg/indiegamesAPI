package br.com.indiegamesapi.exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.Date;
import java.util.stream.Collectors;

// =====================================================================================================
// ExceptionHandlerModel = o "tradutor" de exceções em respostas HTTP.
// =====================================================================================================
//
// O CAMINHO DE UMA EXCEÇÃO, DO SERVICE ATÉ O JSON
// ------------------------------------------------
// 1. Um service lança uma exceção. Ex.: CategoryService.findById(99) -> throw new NotFoundException("Categoria não encontrada, ID: 99").
// 2. Ninguém faz try/catch, então a exceção "sobe": sai do service, sai do controller e chega ao Spring MVC
//    (mais exatamente ao DispatcherServlet, a classe que recebe todas as requisições).
// 3. O Spring procura quem sabe tratar aquele TIPO de exceção. Ele olha as classes marcadas com
//    @RestControllerAdvice (esta aqui) e, dentro delas, os métodos com @ExceptionHandler(Tipo.class).
// 4. Ele escolhe o método MAIS ESPECÍFICO. Uma NotFoundException cai em notFound(...), e não em generic(...),
//    mesmo que NotFoundException também seja uma Exception, porque o Spring prefere o tipo mais próximo.
// 5. O método monta um ResponseEntity<ExceptionResponseModel>:
//       - o ResponseEntity carrega o STATUS HTTP (404, 400...) e o CORPO da resposta;
//       - o corpo é um ExceptionResponseModel(timestamp, message, details).
// 6. O Jackson transforma o ExceptionResponseModel em JSON e o Spring envia para o cliente.
//
// @RestControllerAdvice = @ControllerAdvice + @ResponseBody:
//   - @ControllerAdvice: "estes tratamentos valem para TODOS os controllers da aplicação" (tratamento global);
//   - @ResponseBody: "o que os métodos devolverem vira o corpo da resposta (JSON)".
//
// DE ONDE VEM CADA CAMPO DO JSON
// ------------------------------
//   - timestamp -> new Date(): data e hora do momento em que o erro foi tratado;
//   - message   -> e.getMessage() nas nossas exceções (o texto passado no "new XxxException(...)"),
//                  ou um texto montado aqui nos erros padrão do Spring (ver buildMessage);
//   - details   -> request.getDescription(false): o Spring devolve "uri=/api/category/v1/99".
//                  O "false" significa "não inclua dados do cliente", como IP e id da sessão.
//
// AS DUAS FAMÍLIAS DE ERRO
// ------------------------
//   A) Erros NOSSOS (NotFoundException, BadRequestException, UnauthorizedException) e alguns de bibliotecas
//      (validação da entidade, violação do banco): cada um tem o seu método @ExceptionHandler logo abaixo.
//   B) Erros PADRÃO do Spring MVC (JSON inválido, @Valid falhou, header ou parâmetro faltando, ID não numérico,
//      método não suportado, rota inexistente...). Esses o Spring já sabe tratar, através da classe
//      ResponseEntityExceptionHandler, que esta classe ESTENDE (ver handleExceptionInternal, mais abaixo).
//
// IMPORTANTE: erros que acontecem nos FILTROS (o RateLimitFilter, que devolve 429) NÃO passam por aqui,
// porque os filtros rodam antes do Spring MVC. Por isso o 429 escreve o próprio JSON, dentro do filtro.
// =====================================================================================================
@RestControllerAdvice
public class ExceptionHandlerModel extends ResponseEntityExceptionHandler {

    // -------------------------------------------------------------------------------------------------
    // FAMÍLIA A: um método para cada tipo de exceção
    // -------------------------------------------------------------------------------------------------
    // Como ler a assinatura:
    //   @ExceptionHandler(NotFoundException.class) -> "este método trata NotFoundException";
    //   WebRequest request -> o Spring injeta os dados da requisição que falhou (usamos para pegar a URI);
    //   Exception e        -> a exceção que foi lançada (usamos e.getMessage()).

    // 404: registro não encontrado. A mensagem é a que o service escreveu no "new NotFoundException(...)".
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ExceptionResponseModel> notFound(WebRequest request, Exception e){
        return build(HttpStatus.NOT_FOUND, e.getMessage(), request);
    }

    // 400: regra de negócio violada (senha ausente no cadastro, e-mail repetido...).
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ExceptionResponseModel> badRequest(WebRequest request, Exception e){
        return build(HttpStatus.BAD_REQUEST, e.getMessage(), request);
    }

    // 401: login inválido, ou PUT/DELETE sem uma X-API-Key válida.
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ExceptionResponseModel> unauthorized(WebRequest request, Exception e){
        return build(HttpStatus.UNAUTHORIZED, e.getMessage(), request);
    }

    // 400: a validação da ENTIDADE falhou.
    // As entidades (model/) têm @NotBlank, @Size etc. O Hibernate confere essas regras logo antes do INSERT/UPDATE
    // e, se alguma falhar, lança ConstraintViolationException. Normalmente o RequestDTO já barrou antes, no @Valid;
    // isto é a "rede de segurança".
    // getConstraintViolations() traz TODAS as regras que falharam. Para cada uma:
    //   - getPropertyPath() -> o campo (ex.: "name");
    //   - getMessage()      -> o texto do "message" da anotação (ex.: "O name da categoria deve ser preenchido").
    // O stream junta tudo numa linha: "name: O name da categoria deve ser preenchido; email: O email informado não é válido".
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ExceptionResponseModel> constraintViolation(WebRequest request, ConstraintViolationException e){
        String message = e.getConstraintViolations().stream()
                .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                .collect(Collectors.joining("; "));
        return build(HttpStatus.BAD_REQUEST, message, request);
    }

    // 409: o banco recusou a operação por causa de uma regra dele (chave estrangeira ou campo unique).
    // Caso mais comum: deletar uma categoria que ainda tem games. A tabela games aponta para ela (category_id),
    // então o banco não deixa apagar, e o Spring transforma o erro do banco em DataIntegrityViolationException.
    // A mensagem original do banco é técnica (SQL, nomes de tabelas), por isso escrevemos uma em português.
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ExceptionResponseModel> conflict(WebRequest request, Exception e){
        return build(HttpStatus.CONFLICT, "O registro está em uso por outro recurso (ou já existe) e a operação não pode ser feita", request);
    }

    // 500: qualquer exceção que nenhum outro método tratou (é o "pega-tudo").
    // Não devolvemos e.getMessage() ao cliente: pode conter detalhes internos (SQL, nomes de classes).
    // O detalhe técnico vai para o log do servidor (logger.error, campo herdado de ResponseEntityExceptionHandler),
    // e o cliente recebe só uma mensagem genérica.
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponseModel> generic(WebRequest request, Exception e){
        logger.error("Erro inesperado", e);
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno no servidor, tente novamente mais tarde", request);
    }

    // -------------------------------------------------------------------------------------------------
    // FAMÍLIA B: erros padrão do Spring MVC
    // -------------------------------------------------------------------------------------------------
    // Como o Spring monta esses erros "por baixo dos panos":
    //   1. A classe ResponseEntityExceptionHandler já tem um @ExceptionHandler para cada exceção padrão
    //      (MethodArgumentNotValidException, HttpMessageNotReadableException, MissingRequestHeaderException...).
    //   2. Cada um chama um método "handleXxx" que decide o STATUS (quase sempre 400; 405 para método errado;
    //      404 para rota inexistente...) e cria um corpo do tipo ProblemDetail, um JSON padrão (RFC 9457)
    //      com os campos type, title, status, detail e instance. O "detail" vem em INGLÊS.
    //   3. TODOS esses métodos terminam chamando handleExceptionInternal(ex, body, headers, status, request).
    //
    // Por isso basta sobrescrever (@Override) só o handleExceptionInternal: é o "funil" por onde todos passam.
    // Nele, deixamos o Spring decidir o status e os headers, mas trocamos o corpo ProblemDetail (em inglês)
    // pelo nosso ExceptionResponseModel (em português). Assim TODOS os erros da API ficam no mesmo formato.
    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatusCode statusCode, WebRequest request) {
        // super.handleExceptionInternal(...) = deixa a classe pai fazer o trabalho normal dela: montar o
        // ResponseEntity com o status certo e o ProblemDetail. Ele só devolve null num caso raro: quando a
        // resposta já começou a ser enviada ao cliente e não dá mais para mudar nada.
        ResponseEntity<Object> defaultResponse = super.handleExceptionInternal(ex, body, headers, statusCode, request);
        if (defaultResponse == null) {
            return null;
        }
        // Montamos o NOSSO corpo e reaproveitamos os headers e o status que o Spring escolheu.
        ExceptionResponseModel response = new ExceptionResponseModel(
                new Date(),
                buildMessage(ex, defaultResponse.getBody()),
                request.getDescription(false));
        return new ResponseEntity<>(response, defaultResponse.getHeaders(), defaultResponse.getStatusCode());
    }

    // -------------------------------------------------------------------------------------------------
    // Métodos auxiliares
    // -------------------------------------------------------------------------------------------------

    // Monta a resposta padrão. Existe para não repetir as mesmas linhas em todos os métodos da família A.
    // new ResponseEntity<>(corpo, status): o "<>" (diamond) deixa o Java descobrir o tipo sozinho.
    private ResponseEntity<ExceptionResponseModel> build(HttpStatus status, String message, WebRequest request){
        return new ResponseEntity<>(new ExceptionResponseModel(new Date(), message, request.getDescription(false)), status);
    }

    // Escreve a mensagem em português de cada erro padrão do Spring.
    // "instanceof Tipo variavel" (pattern matching, Java 16+) faz duas coisas de uma vez: testa o tipo e,
    // se for desse tipo, já cria a variável convertida. Assim podemos chamar métodos específicos de cada exceção.
    private String buildMessage(Exception ex, Object body) {

        // @Valid falhou num RequestDTO (ex.: POST de categoria com "naem" no lugar de "name": o name fica null
        // e o @NotBlank reclama). getBindingResult() guarda o resultado da validação; getFieldErrors() lista
        // cada campo inválido com a mensagem que escrevemos na anotação do DTO.
        if (ex instanceof MethodArgumentNotValidException validationEx) {
            return validationEx.getBindingResult().getFieldErrors().stream()
                    .map(error -> error.getField() + ": " + error.getDefaultMessage())
                    .collect(Collectors.joining("; "));
        }

        // O Jackson não conseguiu ler o corpo: JSON com erro de sintaxe (vírgula sobrando, aspas faltando),
        // tipo errado (texto onde deveria ser número) ou valor de enum inexistente (status "LANCADO").
        if (ex instanceof HttpMessageNotReadableException) {
            return "JSON inválido: verifique a sintaxe, os nomes dos campos e os tipos dos valores (ex.: status deve ser DEVELOPMENT, EARLY_ACCESS ou RELEASED)";
        }

        // Faltou um header obrigatório (@RequestHeader). Ex.: POST sem o X-Idempotency-Key.
        if (ex instanceof MissingRequestHeaderException headerEx) {
            return "O header " + headerEx.getHeaderName() + " é obrigatório nesta requisição";
        }

        // Faltou um parâmetro obrigatório da URL (@RequestParam). Ex.: GET /api/category/v1/search sem ?name=
        if (ex instanceof MissingServletRequestParameterException paramEx) {
            return "O parâmetro " + paramEx.getParameterName() + " é obrigatório nesta requisição";
        }

        // Um valor da URL não pôde ser convertido para o tipo esperado.
        // Ex.: GET /api/category/v1/abc ("abc" não é um Long) ou /api/game/v1/status/LANCADO (não existe no enum).
        if (ex instanceof MethodArgumentTypeMismatchException typeEx) {
            return "O valor '" + typeEx.getValue() + "' não é válido para o parâmetro " + typeEx.getName();
        }

        // Método HTTP não existe para aquela rota (status 405). Ex.: DELETE /api/category/v2/1 (a v2 de categoria só tem GET).
        if (ex instanceof HttpRequestMethodNotSupportedException methodEx) {
            return "O método " + methodEx.getMethod() + " não é suportado nesta rota";
        }

        // Content-Type não suportado (status 415). Ex.: enviar o body como texto puro em vez de application/json.
        if (ex instanceof HttpMediaTypeNotSupportedException) {
            return "Content-Type não suportado: envie o corpo como application/json";
        }

        // Rota que não existe (status 404). Ex.: GET /api/categoria (o certo é /api/category/v1).
        if (ex instanceof NoResourceFoundException) {
            return "Rota não encontrada: confira o endereço e a versão (/v1 ou /v2)";
        }

        // Qualquer outro erro padrão: usamos o "detail" do ProblemDetail que o Spring montou (pode vir em inglês).
        if (body instanceof ProblemDetail problemDetail && problemDetail.getDetail() != null) {
            return problemDetail.getDetail();
        }
        return ex.getMessage();
    }
}
