package br.com.indiegamesapi.dto.response;

import br.com.indiegamesapi.model.Platform;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.hateoas.server.core.Relation;

import java.time.LocalDateTime;

// DTO de resposta de Platform. Os _links do HATEOAS são adicionados pelo PlatformModelAssembler.
// @Relation: nome da lista dentro de "_embedded" nas respostas paginadas ("platforms").
@Relation(itemRelation = "platform", collectionRelation = "platforms")
@Schema(name = "PlatformResponseDTO", example = PlatformResponseDTO.EXAMPLE)
public record PlatformResponseDTO(
        Long id,
        String name,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    // [Swagger] exemplo de um item com os links HATEOAS
    public static final String EXAMPLE = "{\n" +
            "  \"id\": 1,\n" +
            "  \"name\": \"PC (Steam)\",\n" +
            "  \"createdAt\": \"2026-11-06T15:45:00\",\n" +
            "  \"updatedAt\": \"2026-11-07T10:30:00\",\n" +
            "  \"_links\": {\n" +
            "    \"platforms\": {\n" +
            "      \"href\": \"http://localhost:8080/api/platform\",\n" +
            "      \"type\": \"GET\"\n" +
            "    },\n" +
            "    \"self\": {\n" +
            "      \"href\": \"http://localhost:8080/api/platform/1\",\n" +
            "      \"type\": \"GET\"\n" +
            "    },\n" +
            "    \"find-by-name\": {\n" +
            "      \"href\": \"http://localhost:8080/api/platform/search?name=PC\",\n" +
            "      \"type\": \"GET\"\n" +
            "    },\n" +
            "    \"create\": {\n" +
            "      \"href\": \"http://localhost:8080/api/platform\",\n" +
            "      \"type\": \"POST\"\n" +
            "    },\n" +
            "    \"update\": {\n" +
            "      \"href\": \"http://localhost:8080/api/platform/1\",\n" +
            "      \"type\": \"PUT\"\n" +
            "    },\n" +
            "    \"delete\": {\n" +
            "      \"href\": \"http://localhost:8080/api/platform/1\",\n" +
            "      \"type\": \"DELETE\"\n" +
            "    },\n" +
            "    \"games\": {\n" +
            "      \"href\": \"http://localhost:8080/api/game/platform/1\",\n" +
            "      \"type\": \"GET\"\n" +
            "    }\n" +
            "  }\n" +
            "}";

    // [Swagger] exemplo da resposta paginada, com os links de paginação (first/self/next/last)
    public static final String PAGE_EXAMPLE = "{\n" +
            "  \"_embedded\": {\n" +
            "    \"platforms\": [\n" +
            "      {\n" +
            "        \"id\": 1,\n" +
            "        \"name\": \"PC (Steam)\",\n" +
            "        \"createdAt\": \"2026-11-06T15:45:00\",\n" +
            "        \"updatedAt\": \"2026-11-07T10:30:00\",\n" +
            "        \"_links\": {\n" +
            "          \"platforms\": {\n" +
            "            \"href\": \"http://localhost:8080/api/platform\",\n" +
            "            \"type\": \"GET\"\n" +
            "          },\n" +
            "          \"self\": {\n" +
            "            \"href\": \"http://localhost:8080/api/platform/1\",\n" +
            "            \"type\": \"GET\"\n" +
            "          },\n" +
            "          \"find-by-name\": {\n" +
            "            \"href\": \"http://localhost:8080/api/platform/search?name=PC\",\n" +
            "            \"type\": \"GET\"\n" +
            "          },\n" +
            "          \"create\": {\n" +
            "            \"href\": \"http://localhost:8080/api/platform\",\n" +
            "            \"type\": \"POST\"\n" +
            "          },\n" +
            "          \"update\": {\n" +
            "            \"href\": \"http://localhost:8080/api/platform/1\",\n" +
            "            \"type\": \"PUT\"\n" +
            "          },\n" +
            "          \"delete\": {\n" +
            "            \"href\": \"http://localhost:8080/api/platform/1\",\n" +
            "            \"type\": \"DELETE\"\n" +
            "          },\n" +
            "          \"games\": {\n" +
            "            \"href\": \"http://localhost:8080/api/game/platform/1\",\n" +
            "            \"type\": \"GET\"\n" +
            "          }\n" +
            "        }\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  \"_links\": {\n" +
            "    \"first\": {\n" +
            "      \"href\": \"http://localhost:8080/api/platform?page=0&size=10\"\n" +
            "    },\n" +
            "    \"self\": {\n" +
            "      \"href\": \"http://localhost:8080/api/platform?page=0&size=10\"\n" +
            "    },\n" +
            "    \"last\": {\n" +
            "      \"href\": \"http://localhost:8080/api/platform?page=0&size=10\"\n" +
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
    public PlatformResponseDTO(Platform platform) {
        this(platform.getId(), platform.getName(), platform.getCreatedAt(), platform.getUpdatedAt());
    }
}
