package br.com.indiegamesapi.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(name = "LoginRequestDTO", example = "{\"email\": \"admin@indiegames.com\", \"password\": \"admin123\"}")
public record LoginRequestDTO(
        @NotBlank(message = "O email deve ser preenchido")
        @Email(message = "O email informado não é válido")
        String email,

        @NotBlank(message = "A password deve ser preenchida")
        String password
) {
}
