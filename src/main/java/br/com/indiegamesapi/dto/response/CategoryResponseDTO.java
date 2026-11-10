package br.com.indiegamesapi.dto.response;

import br.com.indiegamesapi.model.Category;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.hateoas.server.core.Relation;

import java.time.LocalDateTime;

// DTO de resposta de Category. Os _links do HATEOAS são adicionados pelo CategoryModelAssembler.
// @Relation: nome da lista dentro de "_embedded" nas respostas paginadas ("categories").
@Relation(itemRelation = "category", collectionRelation = "categories")
@Schema(name = "CategoryResponseDTO", example = CategoryResponseDTO.EXAMPLE)
public record CategoryResponseDTO(
        Long id,
        String name,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    // [Swagger] exemplo de um item com os links HATEOAS
    public static final String EXAMPLE = "{\n" +
            "  \"id\": 1,\n" +
            "  \"name\": \"RPG\",\n" +
            "  \"createdAt\": \"2026-11-06T15:45:00\",\n" +
            "  \"updatedAt\": \"2026-11-07T10:30:00\",\n" +
            "  \"_links\": {\n" +
            "    \"categories\": {\n" +
            "      \"href\": \"http://localhost:8080/api/category\",\n" +
            "      \"type\": \"GET\"\n" +
            "    },\n" +
            "    \"self\": {\n" +
            "      \"href\": \"http://localhost:8080/api/category/1\",\n" +
            "      \"type\": \"GET\"\n" +
            "    },\n" +
            "    \"find-by-name\": {\n" +
            "      \"href\": \"http://localhost:8080/api/category/search?name=RPG\",\n" +
            "      \"type\": \"GET\"\n" +
            "    },\n" +
            "    \"create\": {\n" +
            "      \"href\": \"http://localhost:8080/api/category\",\n" +
            "      \"type\": \"POST\"\n" +
            "    },\n" +
            "    \"update\": {\n" +
            "      \"href\": \"http://localhost:8080/api/category/1\",\n" +
            "      \"type\": \"PUT\"\n" +
            "    },\n" +
            "    \"delete\": {\n" +
            "      \"href\": \"http://localhost:8080/api/category/1\",\n" +
            "      \"type\": \"DELETE\"\n" +
            "    },\n" +
            "    \"games\": {\n" +
            "      \"href\": \"http://localhost:8080/api/game/category/1\",\n" +
            "      \"type\": \"GET\"\n" +
            "    },\n" +
            "    \"v2\": {\n" +
            "      \"href\": \"http://localhost:8080/api/category/v2/1\",\n" +
            "      \"type\": \"GET\"\n" +
            "    }\n" +
            "  }\n" +
            "}";

    // [Swagger] exemplo da resposta paginada, com os links de paginação (first/self/next/last)
    public static final String PAGE_EXAMPLE = "{\n" +
            "  \"_embedded\": {\n" +
            "    \"categories\": [\n" +
            "      {\n" +
            "        \"id\": 1,\n" +
            "        \"name\": \"RPG\",\n" +
            "        \"createdAt\": \"2026-11-06T15:45:00\",\n" +
            "        \"updatedAt\": \"2026-11-07T10:30:00\",\n" +
            "        \"_links\": {\n" +
            "          \"categories\": {\n" +
            "            \"href\": \"http://localhost:8080/api/category\",\n" +
            "            \"type\": \"GET\"\n" +
            "          },\n" +
            "          \"self\": {\n" +
            "            \"href\": \"http://localhost:8080/api/category/1\",\n" +
            "            \"type\": \"GET\"\n" +
            "          },\n" +
            "          \"find-by-name\": {\n" +
            "            \"href\": \"http://localhost:8080/api/category/search?name=RPG\",\n" +
            "            \"type\": \"GET\"\n" +
            "          },\n" +
            "          \"create\": {\n" +
            "            \"href\": \"http://localhost:8080/api/category\",\n" +
            "            \"type\": \"POST\"\n" +
            "          },\n" +
            "          \"update\": {\n" +
            "            \"href\": \"http://localhost:8080/api/category/1\",\n" +
            "            \"type\": \"PUT\"\n" +
            "          },\n" +
            "          \"delete\": {\n" +
            "            \"href\": \"http://localhost:8080/api/category/1\",\n" +
            "            \"type\": \"DELETE\"\n" +
            "          },\n" +
            "          \"games\": {\n" +
            "            \"href\": \"http://localhost:8080/api/game/category/1\",\n" +
            "            \"type\": \"GET\"\n" +
            "          },\n" +
            "          \"v2\": {\n" +
            "            \"href\": \"http://localhost:8080/api/category/v2/1\",\n" +
            "            \"type\": \"GET\"\n" +
            "          }\n" +
            "        }\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  \"_links\": {\n" +
            "    \"first\": {\n" +
            "      \"href\": \"http://localhost:8080/api/category?page=0&size=10\"\n" +
            "    },\n" +
            "    \"self\": {\n" +
            "      \"href\": \"http://localhost:8080/api/category?page=0&size=10\"\n" +
            "    },\n" +
            "    \"last\": {\n" +
            "      \"href\": \"http://localhost:8080/api/category?page=0&size=10\"\n" +
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
    public CategoryResponseDTO(Category category) {
        this(category.getId(), category.getName(), category.getCreatedAt(), category.getUpdatedAt());
    }
}
