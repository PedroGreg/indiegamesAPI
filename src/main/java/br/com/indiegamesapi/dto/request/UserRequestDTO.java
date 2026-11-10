package br.com.indiegamesapi.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

// NOVO: DTO de entrada do User; a bio do perfil vem direto aqui (antes era "userProfile": { "bio": ... })
// password é opcional: no PUT, se não for enviada, a senha atual é mantida
@Schema(name = "UserRequestDTO", example = "{\"name\": \"Pedro Gregorio\", \"email\": \"pedro@email.com\", \"password\": \"senha123\", \"bio\": \"Desenvolvedor Backend e Entusiasta de Jogos\"}")
public record UserRequestDTO(
        @NotBlank(message = "O name do usuário deve ser preenchido")
        @Size(min = 3, max = 255, message = "O name do usuário deve ter entre 3 e 255 caracteres")
        String name,

        @NotBlank(message = "O email do usuário deve ser preenchido")
        @Email(message = "O email informado não é válido")
        @Size(max = 255, message = "O email deve ter no máximo 255 caracteres")
        String email,

        @Size(min = 6, max = 255, message = "A password deve ter entre 6 e 255 caracteres")
        String password,

        @Size(max = 255, message = "A bio deve ter no máximo 255 caracteres")
        String bio
) {
}
