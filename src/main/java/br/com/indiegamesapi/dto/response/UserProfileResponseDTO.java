package br.com.indiegamesapi.dto.response;

import br.com.indiegamesapi.model.UserProfile;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.hateoas.server.core.Relation;

import java.time.LocalDateTime;

// DTO de resposta de UserProfile. Os _links do HATEOAS são adicionados pelo UserProfileModelAssembler.
// @Relation: nome da lista dentro de "_embedded" nas respostas paginadas ("user-profiles").
@Relation(itemRelation = "user-profile", collectionRelation = "user-profiles")
@Schema(name = "UserProfileResponseDTO", example = UserProfileResponseDTO.EXAMPLE)
public record UserProfileResponseDTO(
        Long id,
        String bio,
        Long userId,
        String userName,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    // [Swagger] exemplo de um item com os links HATEOAS
    public static final String EXAMPLE = "{\n" +
            "  \"id\": 1,\n" +
            "  \"bio\": \"Desenvolvedor Backend e Entusiasta de Jogos\",\n" +
            "  \"userId\": 1,\n" +
            "  \"userName\": \"Pedro Gregorio\",\n" +
            "  \"createdAt\": \"2026-11-06T15:45:00\",\n" +
            "  \"updatedAt\": \"2026-11-07T10:30:00\",\n" +
            "  \"_links\": {\n" +
            "    \"user-profiles\": {\n" +
            "      \"href\": \"http://localhost:8080/api/userprofile\",\n" +
            "      \"type\": \"GET\"\n" +
            "    },\n" +
            "    \"self\": {\n" +
            "      \"href\": \"http://localhost:8080/api/userprofile/1\",\n" +
            "      \"type\": \"GET\"\n" +
            "    },\n" +
            "    \"create\": {\n" +
            "      \"href\": \"http://localhost:8080/api/userprofile\",\n" +
            "      \"type\": \"POST\"\n" +
            "    },\n" +
            "    \"update\": {\n" +
            "      \"href\": \"http://localhost:8080/api/userprofile/1\",\n" +
            "      \"type\": \"PUT\"\n" +
            "    },\n" +
            "    \"delete\": {\n" +
            "      \"href\": \"http://localhost:8080/api/userprofile/1\",\n" +
            "      \"type\": \"DELETE\"\n" +
            "    },\n" +
            "    \"user\": {\n" +
            "      \"href\": \"http://localhost:8080/api/user/1\",\n" +
            "      \"type\": \"GET\"\n" +
            "    }\n" +
            "  }\n" +
            "}";

    // [Swagger] exemplo da resposta paginada, com os links de paginação (first/self/next/last)
    public static final String PAGE_EXAMPLE = "{\n" +
            "  \"_embedded\": {\n" +
            "    \"user-profiles\": [\n" +
            "      {\n" +
            "        \"id\": 1,\n" +
            "        \"bio\": \"Desenvolvedor Backend e Entusiasta de Jogos\",\n" +
            "        \"userId\": 1,\n" +
            "        \"userName\": \"Pedro Gregorio\",\n" +
            "        \"createdAt\": \"2026-11-06T15:45:00\",\n" +
            "        \"updatedAt\": \"2026-11-07T10:30:00\",\n" +
            "        \"_links\": {\n" +
            "          \"user-profiles\": {\n" +
            "            \"href\": \"http://localhost:8080/api/userprofile\",\n" +
            "            \"type\": \"GET\"\n" +
            "          },\n" +
            "          \"self\": {\n" +
            "            \"href\": \"http://localhost:8080/api/userprofile/1\",\n" +
            "            \"type\": \"GET\"\n" +
            "          },\n" +
            "          \"create\": {\n" +
            "            \"href\": \"http://localhost:8080/api/userprofile\",\n" +
            "            \"type\": \"POST\"\n" +
            "          },\n" +
            "          \"update\": {\n" +
            "            \"href\": \"http://localhost:8080/api/userprofile/1\",\n" +
            "            \"type\": \"PUT\"\n" +
            "          },\n" +
            "          \"delete\": {\n" +
            "            \"href\": \"http://localhost:8080/api/userprofile/1\",\n" +
            "            \"type\": \"DELETE\"\n" +
            "          },\n" +
            "          \"user\": {\n" +
            "            \"href\": \"http://localhost:8080/api/user/1\",\n" +
            "            \"type\": \"GET\"\n" +
            "          }\n" +
            "        }\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  \"_links\": {\n" +
            "    \"first\": {\n" +
            "      \"href\": \"http://localhost:8080/api/userprofile?page=0&size=10\"\n" +
            "    },\n" +
            "    \"self\": {\n" +
            "      \"href\": \"http://localhost:8080/api/userprofile?page=0&size=10\"\n" +
            "    },\n" +
            "    \"last\": {\n" +
            "      \"href\": \"http://localhost:8080/api/userprofile?page=0&size=10\"\n" +
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
    public UserProfileResponseDTO(UserProfile userProfile) {
        this(
                userProfile.getId(),
                userProfile.getBio(),
                userProfile.getUser() != null ? userProfile.getUser().getId() : null,
                userProfile.getUser() != null ? userProfile.getUser().getName() : null,
                userProfile.getCreatedAt(),
                userProfile.getUpdatedAt()
        );
    }
}
