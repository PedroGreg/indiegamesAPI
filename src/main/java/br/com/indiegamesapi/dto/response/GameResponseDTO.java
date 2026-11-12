package br.com.indiegamesapi.dto.response;

import br.com.indiegamesapi.model.Game;
import br.com.indiegamesapi.model.GameStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.hateoas.server.core.Relation;

import java.time.LocalDateTime;
import java.util.List;

@Relation(itemRelation = "game", collectionRelation = "games")
@Schema(name = "GameResponseDTO", example = GameResponseDTO.EXAMPLE)
public record GameResponseDTO(
        Long id,
        String name,
        GameStatus status,
        DeveloperResponseDTO developer,
        CategoryResponseDTO category,
        List<PlatformResponseDTO> platforms,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static final String EXAMPLE = "{\n" +
            "  \"id\": 1,\n" +
            "  \"name\": \"Undertale\",\n" +
            "  \"status\": \"RELEASED\",\n" +
            "  \"developer\": {\n" +
            "    \"id\": 1,\n" +
            "    \"name\": \"Toby Fox\",\n" +
            "    \"createdAt\": \"2026-11-06T15:45:00\",\n" +
            "    \"updatedAt\": \"2026-11-07T10:30:00\"\n" +
            "  },\n" +
            "  \"category\": {\n" +
            "    \"id\": 1,\n" +
            "    \"name\": \"RPG\",\n" +
            "    \"createdAt\": \"2026-11-06T15:45:00\",\n" +
            "    \"updatedAt\": \"2026-11-07T10:30:00\"\n" +
            "  },\n" +
            "  \"platforms\": [\n" +
            "    {\n" +
            "      \"id\": 1,\n" +
            "      \"name\": \"PC (Steam)\",\n" +
            "      \"createdAt\": \"2026-11-06T15:45:00\",\n" +
            "      \"updatedAt\": \"2026-11-07T10:30:00\"\n" +
            "    }\n" +
            "  ],\n" +
            "  \"createdAt\": \"2026-11-06T15:45:00\",\n" +
            "  \"updatedAt\": \"2026-11-07T10:30:00\",\n" +
            "  \"_links\": {\n" +
            "    \"games\": {\n" +
            "      \"href\": \"http://localhost:8080/api/game/v1\",\n" +
            "      \"type\": \"GET\"\n" +
            "    },\n" +
            "    \"self\": {\n" +
            "      \"href\": \"http://localhost:8080/api/game/v1/1\",\n" +
            "      \"type\": \"GET\"\n" +
            "    },\n" +
            "    \"find-by-name\": {\n" +
            "      \"href\": \"http://localhost:8080/api/game/v1/search?name=Undertale\",\n" +
            "      \"type\": \"GET\"\n" +
            "    },\n" +
            "    \"create\": {\n" +
            "      \"href\": \"http://localhost:8080/api/game/v1\",\n" +
            "      \"type\": \"POST\"\n" +
            "    },\n" +
            "    \"update\": {\n" +
            "      \"href\": \"http://localhost:8080/api/game/v1/1\",\n" +
            "      \"type\": \"PUT\"\n" +
            "    },\n" +
            "    \"delete\": {\n" +
            "      \"href\": \"http://localhost:8080/api/game/v1/1\",\n" +
            "      \"type\": \"DELETE\"\n" +
            "    },\n" +
            "    \"developer\": {\n" +
            "      \"href\": \"http://localhost:8080/api/developer/v1/1\",\n" +
            "      \"type\": \"GET\"\n" +
            "    },\n" +
            "    \"category\": {\n" +
            "      \"href\": \"http://localhost:8080/api/category/v1/1\",\n" +
            "      \"type\": \"GET\"\n" +
            "    },\n" +
            "    \"v2\": {\n" +
            "      \"href\": \"http://localhost:8080/api/game/v2/1\",\n" +
            "      \"type\": \"GET\"\n" +
            "    }\n" +
            "  }\n" +
            "}";

    public static final String PAGE_EXAMPLE = "{\n" +
            "  \"_embedded\": {\n" +
            "    \"games\": [\n" +
            "      {\n" +
            "        \"id\": 1,\n" +
            "        \"name\": \"Undertale\",\n" +
            "        \"status\": \"RELEASED\",\n" +
            "        \"developer\": {\n" +
            "          \"id\": 1,\n" +
            "          \"name\": \"Toby Fox\",\n" +
            "          \"createdAt\": \"2026-11-06T15:45:00\",\n" +
            "          \"updatedAt\": \"2026-11-07T10:30:00\"\n" +
            "        },\n" +
            "        \"category\": {\n" +
            "          \"id\": 1,\n" +
            "          \"name\": \"RPG\",\n" +
            "          \"createdAt\": \"2026-11-06T15:45:00\",\n" +
            "          \"updatedAt\": \"2026-11-07T10:30:00\"\n" +
            "        },\n" +
            "        \"platforms\": [\n" +
            "          {\n" +
            "            \"id\": 1,\n" +
            "            \"name\": \"PC (Steam)\",\n" +
            "            \"createdAt\": \"2026-11-06T15:45:00\",\n" +
            "            \"updatedAt\": \"2026-11-07T10:30:00\"\n" +
            "          }\n" +
            "        ],\n" +
            "        \"createdAt\": \"2026-11-06T15:45:00\",\n" +
            "        \"updatedAt\": \"2026-11-07T10:30:00\",\n" +
            "        \"_links\": {\n" +
            "          \"games\": {\n" +
            "            \"href\": \"http://localhost:8080/api/game/v1\",\n" +
            "            \"type\": \"GET\"\n" +
            "          },\n" +
            "          \"self\": {\n" +
            "            \"href\": \"http://localhost:8080/api/game/v1/1\",\n" +
            "            \"type\": \"GET\"\n" +
            "          },\n" +
            "          \"find-by-name\": {\n" +
            "            \"href\": \"http://localhost:8080/api/game/v1/search?name=Undertale\",\n" +
            "            \"type\": \"GET\"\n" +
            "          },\n" +
            "          \"create\": {\n" +
            "            \"href\": \"http://localhost:8080/api/game/v1\",\n" +
            "            \"type\": \"POST\"\n" +
            "          },\n" +
            "          \"update\": {\n" +
            "            \"href\": \"http://localhost:8080/api/game/v1/1\",\n" +
            "            \"type\": \"PUT\"\n" +
            "          },\n" +
            "          \"delete\": {\n" +
            "            \"href\": \"http://localhost:8080/api/game/v1/1\",\n" +
            "            \"type\": \"DELETE\"\n" +
            "          },\n" +
            "          \"developer\": {\n" +
            "            \"href\": \"http://localhost:8080/api/developer/v1/1\",\n" +
            "            \"type\": \"GET\"\n" +
            "          },\n" +
            "          \"category\": {\n" +
            "            \"href\": \"http://localhost:8080/api/category/v1/1\",\n" +
            "            \"type\": \"GET\"\n" +
            "          },\n" +
            "          \"v2\": {\n" +
            "            \"href\": \"http://localhost:8080/api/game/v2/1\",\n" +
            "            \"type\": \"GET\"\n" +
            "          }\n" +
            "        }\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  \"_links\": {\n" +
            "    \"first\": {\n" +
            "      \"href\": \"http://localhost:8080/api/game/v1?page=0&size=10\"\n" +
            "    },\n" +
            "    \"self\": {\n" +
            "      \"href\": \"http://localhost:8080/api/game/v1?page=0&size=10\"\n" +
            "    },\n" +
            "    \"last\": {\n" +
            "      \"href\": \"http://localhost:8080/api/game/v1?page=0&size=10\"\n" +
            "    }\n" +
            "  },\n" +
            "  \"page\": {\n" +
            "    \"size\": 10,\n" +
            "    \"totalElements\": 1,\n" +
            "    \"totalPages\": 1,\n" +
            "    \"number\": 0\n" +
            "  }\n" +
            "}";

    public GameResponseDTO(Game game) {
        this(
                game.getId(),
                game.getName(),
                game.getStatus(),
                game.getDeveloper() != null ? new DeveloperResponseDTO(game.getDeveloper()) : null,
                game.getCategory() != null ? new CategoryResponseDTO(game.getCategory()) : null,
                game.getPlatforms() != null ? game.getPlatforms().stream().map(PlatformResponseDTO::new).toList() : List.of(),
                game.getCreatedAt(),
                game.getUpdatedAt()
        );
    }
}
