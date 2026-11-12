package br.com.indiegamesapi.dto.response;

import br.com.indiegamesapi.model.Category;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.hateoas.server.core.Relation;

import java.time.LocalDateTime;

@Relation(itemRelation = "category", collectionRelation = "categories")
@Schema(name = "CategoryResponseDTO", example = CategoryResponseDTO.EXAMPLE)
public record CategoryResponseDTO(
        Long id,
        String name,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static final String EXAMPLE = "{\n" +
            "  \"id\": 1,\n" +
            "  \"name\": \"RPG\",\n" +
            "  \"createdAt\": \"2026-11-06T15:45:00\",\n" +
            "  \"updatedAt\": \"2026-11-07T10:30:00\",\n" +
            "  \"_links\": {\n" +
            "    \"categories\": {\n" +
            "      \"href\": \"http://localhost:8080/api/category/v1\",\n" +
            "      \"type\": \"GET\"\n" +
            "    },\n" +
            "    \"self\": {\n" +
            "      \"href\": \"http://localhost:8080/api/category/v1/1\",\n" +
            "      \"type\": \"GET\"\n" +
            "    },\n" +
            "    \"find-by-name\": {\n" +
            "      \"href\": \"http://localhost:8080/api/category/v1/search?name=RPG\",\n" +
            "      \"type\": \"GET\"\n" +
            "    },\n" +
            "    \"create\": {\n" +
            "      \"href\": \"http://localhost:8080/api/category/v1\",\n" +
            "      \"type\": \"POST\"\n" +
            "    },\n" +
            "    \"update\": {\n" +
            "      \"href\": \"http://localhost:8080/api/category/v1/1\",\n" +
            "      \"type\": \"PUT\"\n" +
            "    },\n" +
            "    \"delete\": {\n" +
            "      \"href\": \"http://localhost:8080/api/category/v1/1\",\n" +
            "      \"type\": \"DELETE\"\n" +
            "    },\n" +
            "    \"games\": {\n" +
            "      \"href\": \"http://localhost:8080/api/game/v1/category/1\",\n" +
            "      \"type\": \"GET\"\n" +
            "    },\n" +
            "    \"v2\": {\n" +
            "      \"href\": \"http://localhost:8080/api/category/v2/1\",\n" +
            "      \"type\": \"GET\"\n" +
            "    }\n" +
            "  }\n" +
            "}";

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
            "            \"href\": \"http://localhost:8080/api/category/v1\",\n" +
            "            \"type\": \"GET\"\n" +
            "          },\n" +
            "          \"self\": {\n" +
            "            \"href\": \"http://localhost:8080/api/category/v1/1\",\n" +
            "            \"type\": \"GET\"\n" +
            "          },\n" +
            "          \"find-by-name\": {\n" +
            "            \"href\": \"http://localhost:8080/api/category/v1/search?name=RPG\",\n" +
            "            \"type\": \"GET\"\n" +
            "          },\n" +
            "          \"create\": {\n" +
            "            \"href\": \"http://localhost:8080/api/category/v1\",\n" +
            "            \"type\": \"POST\"\n" +
            "          },\n" +
            "          \"update\": {\n" +
            "            \"href\": \"http://localhost:8080/api/category/v1/1\",\n" +
            "            \"type\": \"PUT\"\n" +
            "          },\n" +
            "          \"delete\": {\n" +
            "            \"href\": \"http://localhost:8080/api/category/v1/1\",\n" +
            "            \"type\": \"DELETE\"\n" +
            "          },\n" +
            "          \"games\": {\n" +
            "            \"href\": \"http://localhost:8080/api/game/v1/category/1\",\n" +
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
            "      \"href\": \"http://localhost:8080/api/category/v1?page=0&size=10\"\n" +
            "    },\n" +
            "    \"self\": {\n" +
            "      \"href\": \"http://localhost:8080/api/category/v1?page=0&size=10\"\n" +
            "    },\n" +
            "    \"last\": {\n" +
            "      \"href\": \"http://localhost:8080/api/category/v1?page=0&size=10\"\n" +
            "    }\n" +
            "  },\n" +
            "  \"page\": {\n" +
            "    \"size\": 10,\n" +
            "    \"totalElements\": 1,\n" +
            "    \"totalPages\": 1,\n" +
            "    \"number\": 0\n" +
            "  }\n" +
            "}";

    public CategoryResponseDTO(Category category) {
        this(category.getId(), category.getName(), category.getCreatedAt(), category.getUpdatedAt());
    }
}
