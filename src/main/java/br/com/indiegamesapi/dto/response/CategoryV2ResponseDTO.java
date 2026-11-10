package br.com.indiegamesapi.dto.response;

import br.com.indiegamesapi.model.Category;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.hateoas.server.core.Relation;

import java.time.LocalDateTime;

// NOVO (versionamento): categoria na v2. A diferença para a v1 é o campo gamesCount (quantos games ela tem).
// A v1 continua igual, então quem já usa a API não é afetado: esse é o objetivo de versionar.
@Relation(itemRelation = "category", collectionRelation = "categories")
@Schema(name = "CategoryV2ResponseDTO", example = CategoryV2ResponseDTO.EXAMPLE)
public record CategoryV2ResponseDTO(
        Long id,
        String name,
        long gamesCount,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    // [Swagger] exemplo de um item
    public static final String EXAMPLE = "{\n" +
            "  \"id\": 1,\n" +
            "  \"name\": \"RPG\",\n" +
            "  \"gamesCount\": 2,\n" +
            "  \"createdAt\": \"2026-11-06T15:45:00\",\n" +
            "  \"updatedAt\": \"2026-11-07T10:30:00\",\n" +
            "  \"_links\": {\n" +
            "    \"self\": {\n" +
            "      \"href\": \"http://localhost:8080/api/category/v2/1\",\n" +
            "      \"type\": \"GET\"\n" +
            "    },\n" +
            "    \"categories\": {\n" +
            "      \"href\": \"http://localhost:8080/api/category/v2\",\n" +
            "      \"type\": \"GET\"\n" +
            "    },\n" +
            "    \"v1\": {\n" +
            "      \"href\": \"http://localhost:8080/api/category/1\",\n" +
            "      \"type\": \"GET\"\n" +
            "    }\n" +
            "  }\n" +
            "}";

    // [Swagger] exemplo da resposta paginada
    public static final String PAGE_EXAMPLE = "{\n" +
            "  \"_embedded\": {\n" +
            "    \"categories\": [\n" +
            "      {\n" +
            "        \"id\": 1,\n" +
            "        \"name\": \"RPG\",\n" +
            "        \"gamesCount\": 2,\n" +
            "        \"createdAt\": \"2026-11-06T15:45:00\",\n" +
            "        \"updatedAt\": \"2026-11-07T10:30:00\",\n" +
            "        \"_links\": {\n" +
            "          \"self\": {\n" +
            "            \"href\": \"http://localhost:8080/api/category/v2/1\",\n" +
            "            \"type\": \"GET\"\n" +
            "          },\n" +
            "          \"categories\": {\n" +
            "            \"href\": \"http://localhost:8080/api/category/v2\",\n" +
            "            \"type\": \"GET\"\n" +
            "          },\n" +
            "          \"v1\": {\n" +
            "            \"href\": \"http://localhost:8080/api/category/1\",\n" +
            "            \"type\": \"GET\"\n" +
            "          }\n" +
            "        }\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  \"_links\": {\n" +
            "    \"first\": {\n" +
            "      \"href\": \"http://localhost:8080/api/category/v2?page=0&size=10\"\n" +
            "    },\n" +
            "    \"self\": {\n" +
            "      \"href\": \"http://localhost:8080/api/category/v2?page=0&size=10\"\n" +
            "    },\n" +
            "    \"last\": {\n" +
            "      \"href\": \"http://localhost:8080/api/category/v2?page=0&size=10\"\n" +
            "    }\n" +
            "  },\n" +
            "  \"page\": {\n" +
            "    \"size\": 10,\n" +
            "    \"totalElements\": 1,\n" +
            "    \"totalPages\": 1,\n" +
            "    \"number\": 0\n" +
            "  }\n" +
            "}";

    public CategoryV2ResponseDTO(Category category, long gamesCount) {
        this(category.getId(), category.getName(), gamesCount, category.getCreatedAt(), category.getUpdatedAt());
    }
}
