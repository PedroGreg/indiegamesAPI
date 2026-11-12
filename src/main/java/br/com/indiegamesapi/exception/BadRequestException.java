package br.com.indiegamesapi.exception;

// =====================================================================================================
// BadRequestException = "o cliente mandou algo errado" -> HTTP 400 (Bad Request).
// =====================================================================================================
//
// Usada para regras de negócio que as anotações dos DTOs (@NotBlank, @Size...) não conseguem checar sozinhas.
// Exemplos no UserService:
//   - cadastro sem senha:     new BadRequestException("password: A password é obrigatória no cadastro")
//   - e-mail já cadastrado:   new BadRequestException("email: Email já cadastrado: " + email)
//
// Repare no padrão "campo: mensagem" ("password: ...", "email: ..."). É o mesmo formato que o handler
// usa para os erros de validação do @Valid, assim o cliente sempre sabe QUAL campo causou o erro.
//
// Assim como a NotFoundException, estende RuntimeException e é transformada em JSON pelo ExceptionHandlerModel.
// =====================================================================================================
public class BadRequestException extends RuntimeException {

    // Mensagem padrão, para quando o corpo (body) da requisição não foi enviado.
    public BadRequestException() {
        super("É necessário enviar o corpo da requisição neste endpoint");
    }

    // Mensagem personalizada (a mais usada).
    public BadRequestException(String message) {
        super(message);
    }
}
