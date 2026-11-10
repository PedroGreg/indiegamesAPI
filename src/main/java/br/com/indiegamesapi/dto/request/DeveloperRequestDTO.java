package br.com.indiegamesapi.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

// NOVO: DTO de entrada (POST/PUT); as validações ficam aqui e não na entidade
@Schema(name = "DeveloperRequestDTO", example = "{\"name\": \"Toby Fox\"}")
public record DeveloperRequestDTO(
        @NotBlank(message = "O name do desenvolvedor deve ser preenchido")
        @Size(min = 2, max = 255, message = "O name do desenvolvedor deve ter entre 2 e 255 caracteres")
        String name
) {
}
