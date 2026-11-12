package br.com.indiegamesapi.exception;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;

// =====================================================================================================
// ExceptionResponseModel = o "molde" (formato) de TODA resposta de erro da API.
// =====================================================================================================
//
// Não importa qual erro aconteceu (404, 400, 401, 409, 500...): o cliente sempre recebe um JSON assim:
//
//     {
//       "timestamp": "2026-11-12T17:45:00.000+00:00",
//       "message":   "Categoria não encontrada, ID: 99",
//       "details":   "uri=/api/category/v1/99"
//     }
//
// Por que um formato único? Quem consome a API (um front-end, o Postman, o professor no Swagger) só precisa
// aprender a ler um formato. Basta pegar o campo "message" para mostrar o erro ao usuário.
//
// Por que um "record"? Um record é uma classe imutável e enxuta: o Java gera sozinho o construtor,
// os "getters" (timestamp(), message(), details()), equals, hashCode e toString.
// O Jackson (biblioteca que transforma objeto Java <-> JSON) entende records e usa os nomes dos
// componentes (timestamp, message, details) como nomes dos campos do JSON.
//
// Os três campos:
//   - timestamp: quando o erro aconteceu. É um java.util.Date; o Jackson converte para texto no padrão ISO-8601.
//   - message:   a explicação do erro, em português. É ela que o ExceptionHandlerModel monta para cada caso.
//   - details:   qual endereço foi chamado, no formato "uri=/api/...". Vem do request.getDescription(false).
//
// O @Schema abaixo é só documentação: faz o Swagger mostrar um exemplo desse JSON nas respostas de erro.
// =====================================================================================================
@Schema(name = "ExceptionResponseModel", example = "{\n" +
        "  \"timestamp\": \"2026-11-12T17:45:00.000+00:00\",\n" +
        "  \"message\": \"name: O name da categoria deve ser preenchido\",\n" +
        "  \"details\": \"uri=/api/category/v1\"\n" +
        "}")
public record ExceptionResponseModel(Date timestamp, String message, String details) {
}
