package br.com.indiegamesapi.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

// NOVO: DTO de entrada (POST/PUT); as validações ficam aqui e não na entidade
@Schema(name = "PlatformRequestDTO", example = "{\"name\": \"PC (Steam)\"}")
public record PlatformRequestDTO(
        @NotBlank(message = "O name da plataforma deve ser preenchido")
        @Size(min = 2, max = 50, message = "O name da plataforma deve ter entre 2 e 50 caracteres")
        String name
) {
}
