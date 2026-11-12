package br.com.indiegamesapi.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "LoginResponseDTO", example = "{\n" +
        "  \"apiKey\": \"9b1c2f4e-6a7d-4c3b-8e2f-0a1b2c3d4e5f\",\n" +
        "  \"userId\": 1,\n" +
        "  \"name\": \"Administrador\",\n" +
        "  \"email\": \"admin@indiegames.com\",\n" +
        "  \"message\": \"Logado. Envie a apiKey no header X-API-Key para usar os PUT e DELETE.\",\n" +
        "  \"_links\": {\n" +
        "    \"user\": { \"href\": \"http://localhost:8080/api/user/1\", \"type\": \"GET\" },\n" +
        "    \"logout\": { \"href\": \"http://localhost:8080/api/auth/logout\", \"type\": \"POST\" }\n" +
        "  }\n" +
        "}")
public record LoginResponseDTO(
        String apiKey,
        Long userId,
        String name,
        String email,
        String message
) {
}
