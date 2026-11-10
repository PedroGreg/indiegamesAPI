package br.com.indiegamesapi.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

// NOVO: DTO de entrada do perfil; o vínculo com o usuário é controlado pelo User, então aqui vai só a bio
@Schema(name = "UserProfileRequestDTO", example = "{\"bio\": \"Esse é meu perfil\"}")
public record UserProfileRequestDTO(
        @NotNull(message = "A bio deve ser enviada (pode ser vazia)")
        @Size(max = 255, message = "A bio deve ter no máximo 255 caracteres")
        String bio
) {
}
