package br.com.indiegamesapi.exception;

// =====================================================================================================
// UnauthorizedException = "não sei quem você é" -> HTTP 401 (Unauthorized).
// =====================================================================================================
//
// Lançada pelo AuthService em dois casos:
//   1. Login com email ou senha errados (POST /api/auth/login).
//   2. PUT ou DELETE sem o header X-API-Key, ou com uma chave que não pertence a nenhum usuário
//      (inclusive uma chave antiga, depois de um novo login ou de um logout).
//
// Fluxo num DELETE, passo a passo:
//   CategoryController.delete(...)  ->  authService.validate(apiKey)
//       -> a chave é inválida -> throw new UnauthorizedException("X-API-Key inválida...")
//       -> o service.delete(id) NEM CHEGA a ser executado, porque a exceção interrompe o método
//       -> o Spring entrega a exceção ao ExceptionHandlerModel.unauthorized(...)
//       -> o cliente recebe 401 com o JSON padrão.
// =====================================================================================================
public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String message) {
        super(message);
    }
}
