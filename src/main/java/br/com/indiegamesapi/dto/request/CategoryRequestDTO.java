package br.com.indiegamesapi.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(name = "CategoryRequestDTO", example = "{\"name\": \"RPG\"}")
public record CategoryRequestDTO (
        @NotBlank(message = "O name da categoria deve ser preenchido")
        @Size(min = 3, max = 255)
        String name
) {
}
