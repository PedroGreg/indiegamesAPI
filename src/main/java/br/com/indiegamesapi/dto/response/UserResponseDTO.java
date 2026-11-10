package br.com.indiegamesapi.dto.response;

import br.com.indiegamesapi.model.User;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.hateoas.server.core.Relation;

import java.time.LocalDateTime;

// DTO de resposta de User (a password NÃO é devolvida). Os _links do HATEOAS são adicionados pelo UserModelAssembler.
// @Relation: nome da lista dentro de "_embedded" nas respostas paginadas ("users").
@Relation(itemRelation = "user", collectionRelation = "users")
@Schema(name = "UserResponseDTO", example = UserResponseDTO.EXAMPLE)
public record UserResponseDTO(
        Long id,
        String name,
        String email,
        Long userProfileId,
        String bio,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    // [Swagger] exemplo de um item com os links HATEOAS
    public static final String EXAMPLE = "{\n" +
            "  \"id\": 1,\n" +
            "  \"name\": \"Pedro Gregorio\",\n" +
            "  \"email\": \"pedro@email.com\",\n" +
            "  \"userProfileId\": 1,\n" +
            "  \"bio\": \"Desenvolvedor Backend e Entusiasta de Jogos\",\n" +
            "  \"createdAt\": \"2026-11-06T15:45:00\",\n" +
            "  \"updatedAt\": \"2026-11-07T10:30:00\",\n" +
            "  \"_links\": {\n" +
            "    \"users\": {\n" +
            "      \"href\": \"http://localhost:8080/api/user\",\n" +
            "      \"type\": \"GET\"\n" +
            "    },\n" +
            "    \"self\": {\n" +
            "      \"href\": \"http://localhost:8080/api/user/1\",\n" +
            "      \"type\": \"GET\"\n" +
            "    },\n" +
            "    \"find-by-name\": {\n" +
            "      \"href\": \"http://localhost:8080/api/user/search?name=Pedro\",\n" +
            "      \"type\": \"GET\"\n" +
            "    },\n" +
            "    \"create\": {\n" +
            "      \"href\": \"http://localhost:8080/api/user\",\n" +
            "      \"type\": \"POST\"\n" +
            "    },\n" +
            "    \"update\": {\n" +
            "      \"href\": \"http://localhost:8080/api/user/1\",\n" +
            "      \"type\": \"PUT\"\n" +
            "    },\n" +
            "    \"delete\": {\n" +
            "      \"href\": \"http://localhost:8080/api/user/1\",\n" +
            "      \"type\": \"DELETE\"\n" +
            "    },\n" +
            "    \"user-profile\": {\n" +
            "      \"href\": \"http://localhost:8080/api/userprofile/1\",\n" +
            "      \"type\": \"GET\"\n" +
            "    }\n" +
            "  }\n" +
            "}";

    // [Swagger] exemplo da resposta paginada, com os links de paginação (first/self/next/last)
    public static final String PAGE_EXAMPLE = "{\n" +
            "  \"_embedded\": {\n" +
            "    \"users\": [\n" +
            "      {\n" +
            "        \"id\": 1,\n" +
            "        \"name\": \"Pedro Gregorio\",\n" +
            "        \"email\": \"pedro@email.com\",\n" +
            "        \"userProfileId\": 1,\n" +
            "        \"bio\": \"Desenvolvedor Backend e Entusiasta de Jogos\",\n" +
            "        \"createdAt\": \"2026-11-06T15:45:00\",\n" +
            "        \"updatedAt\": \"2026-11-07T10:30:00\",\n" +
            "        \"_links\": {\n" +
            "          \"users\": {\n" +
            "            \"href\": \"http://localhost:8080/api/user\",\n" +
            "            \"type\": \"GET\"\n" +
            "          },\n" +
            "          \"self\": {\n" +
            "            \"href\": \"http://localhost:8080/api/user/1\",\n" +
            "            \"type\": \"GET\"\n" +
            "          },\n" +
            "          \"find-by-name\": {\n" +
            "            \"href\": \"http://localhost:8080/api/user/search?name=Pedro\",\n" +
            "            \"type\": \"GET\"\n" +
            "          },\n" +
            "          \"create\": {\n" +
            "            \"href\": \"http://localhost:8080/api/user\",\n" +
            "            \"type\": \"POST\"\n" +
            "          },\n" +
            "          \"update\": {\n" +
            "            \"href\": \"http://localhost:8080/api/user/1\",\n" +
            "            \"type\": \"PUT\"\n" +
            "          },\n" +
            "          \"delete\": {\n" +
            "            \"href\": \"http://localhost:8080/api/user/1\",\n" +
            "            \"type\": \"DELETE\"\n" +
            "          },\n" +
            "          \"user-profile\": {\n" +
            "            \"href\": \"http://localhost:8080/api/userprofile/1\",\n" +
            "            \"type\": \"GET\"\n" +
            "          }\n" +
            "        }\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  \"_links\": {\n" +
            "    \"first\": {\n" +
            "      \"href\": \"http://localhost:8080/api/user?page=0&size=10\"\n" +
            "    },\n" +
            "    \"self\": {\n" +
            "      \"href\": \"http://localhost:8080/api/user?page=0&size=10\"\n" +
            "    },\n" +
            "    \"last\": {\n" +
            "      \"href\": \"http://localhost:8080/api/user?page=0&size=10\"\n" +
            "    }\n" +
            "  },\n" +
            "  \"page\": {\n" +
            "    \"size\": 10,\n" +
            "    \"totalElements\": 1,\n" +
            "    \"totalPages\": 1,\n" +
            "    \"number\": 0\n" +
            "  }\n" +
            "}";

    // Converte a entidade no DTO
    public UserResponseDTO(User user) {
        this(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getUserProfile() != null ? user.getUserProfile().getId() : null,
                user.getUserProfile() != null ? user.getUserProfile().getBio() : null,
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}
