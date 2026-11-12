package br.com.indiegamesapi.dto.request;

import br.com.indiegamesapi.model.GameStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

@Schema(name = "GameRequestDTO", example = "{\"name\": \"Undertale\", \"status\": \"RELEASED\", \"developerId\": 1, \"categoryId\": 1, \"platformIds\": [1, 2]}")
public record GameRequestDTO(
        @NotBlank(message = "O name do game deve ser preenchido")
        @Size(max = 255, message = "O name do game deve ter no máximo 255 caracteres")
        String name,

        @NotNull(message = "O status deve ser informado: DEVELOPMENT, EARLY_ACCESS ou RELEASED")
        GameStatus status,

        @NotNull(message = "O developerId deve ser informado")
        Long developerId,

        @NotNull(message = "O categoryId deve ser informado")
        Long categoryId,

        @NotNull(message = "A lista platformIds deve ser informada (pode ser vazia)")
        List<@NotNull(message = "A lista platformIds não pode ter valores nulos") Long> platformIds
) {
}
