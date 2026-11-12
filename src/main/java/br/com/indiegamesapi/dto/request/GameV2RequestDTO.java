package br.com.indiegamesapi.dto.request;

import br.com.indiegamesapi.model.GameStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.util.List;

@Schema(name = "GameV2RequestDTO", example = "{\"name\": \"Hollow Knight\", \"status\": \"RELEASED\", \"developerId\": 2, \"categoryId\": 2, \"platformIds\": [1, 2], \"totalAchievements\": 63}")
public record GameV2RequestDTO(
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
        List<@NotNull(message = "A lista platformIds não pode ter valores nulos") Long> platformIds,

        @NotNull(message = "O totalAchievements (quantidade total de conquistas) deve ser informado")
        @PositiveOrZero(message = "O totalAchievements não pode ser negativo")
        @Max(value = 10000, message = "O totalAchievements deve ser no máximo 10000")
        Integer totalAchievements
) {
}
