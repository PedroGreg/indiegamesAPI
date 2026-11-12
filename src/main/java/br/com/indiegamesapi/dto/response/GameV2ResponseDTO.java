package br.com.indiegamesapi.dto.response;

import br.com.indiegamesapi.model.Game;
import br.com.indiegamesapi.model.GameStatus;
import br.com.indiegamesapi.model.Platform;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.hateoas.server.core.Relation;

import java.time.LocalDateTime;
import java.util.List;

@Relation(itemRelation = "game", collectionRelation = "games")
@Schema(name = "GameV2ResponseDTO", example = GameV2ResponseDTO.EXAMPLE)
public record GameV2ResponseDTO(
        Long id,
        String name,
        GameStatus status,
        String developerName,
        String categoryName,
        List<String> platformNames,
        Integer totalAchievements,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static final String EXAMPLE = "{\n" +
            "  \"id\": 3,\n" +
            "  \"name\": \"Hollow Knight\",\n" +
            "  \"status\": \"RELEASED\",\n" +
            "  \"developerName\": \"Team Cherry\",\n" +
            "  \"categoryName\": \"Metroidvania\",\n" +
            "  \"platformNames\": [\n" +
            "    \"PC (Steam)\",\n" +
            "    \"Nintendo Switch\",\n" +
            "    \"PlayStation 5\"\n" +
            "  ],\n" +
            "  \"totalAchievements\": 63,\n" +
            "  \"createdAt\": \"2026-11-06T15:45:00\",\n" +
            "  \"updatedAt\": \"2026-11-07T10:30:00\",\n" +
            "  \"_links\": {\n" +
            "    \"games\": {\n" +
            "      \"href\": \"http://localhost:8080/api/game/v2\",\n" +
            "      \"type\": \"GET\"\n" +
            "    },\n" +
            "    \"self\": {\n" +
            "      \"href\": \"http://localhost:8080/api/game/v2/3\",\n" +
            "      \"type\": \"GET\"\n" +
            "    },\n" +
            "    \"find-by-name\": {\n" +
            "      \"href\": \"http://localhost:8080/api/game/v1/search?name=Hollow%20Knight\",\n" +
            "      \"type\": \"GET\"\n" +
            "    },\n" +
            "    \"create\": {\n" +
            "      \"href\": \"http://localhost:8080/api/game/v2\",\n" +
            "      \"type\": \"POST\"\n" +
            "    },\n" +
            "    \"update\": {\n" +
            "      \"href\": \"http://localhost:8080/api/game/v2/3\",\n" +
            "      \"type\": \"PUT\"\n" +
            "    },\n" +
            "    \"delete\": {\n" +
            "      \"href\": \"http://localhost:8080/api/game/v1/3\",\n" +
            "      \"type\": \"DELETE\"\n" +
            "    },\n" +
            "    \"developer\": {\n" +
            "      \"href\": \"http://localhost:8080/api/developer/v1/2\",\n" +
            "      \"type\": \"GET\"\n" +
            "    },\n" +
            "    \"category\": {\n" +
            "      \"href\": \"http://localhost:8080/api/category/v2/2\",\n" +
            "      \"type\": \"GET\"\n" +
            "    },\n" +
            "    \"same-status\": {\n" +
            "      \"href\": \"http://localhost:8080/api/game/v1/status/RELEASED\",\n" +
            "      \"type\": \"GET\"\n" +
            "    },\n" +
            "    \"v1\": {\n" +
            "      \"href\": \"http://localhost:8080/api/game/v1/3\",\n" +
            "      \"type\": \"GET\"\n" +
            "    }\n" +
            "  }\n" +
            "}";

    public static final String PAGE_EXAMPLE = "{\n" +
            "  \"_embedded\": {\n" +
            "    \"games\": [\n" +
            "      {\n" +
            "        \"id\": 3,\n" +
            "        \"name\": \"Hollow Knight\",\n" +
            "        \"status\": \"RELEASED\",\n" +
            "        \"developerName\": \"Team Cherry\",\n" +
            "        \"categoryName\": \"Metroidvania\",\n" +
            "        \"platformNames\": [\n" +
            "          \"PC (Steam)\",\n" +
            "          \"Nintendo Switch\",\n" +
            "          \"PlayStation 5\"\n" +
            "        ],\n" +
            "        \"totalAchievements\": 63,\n" +
            "        \"createdAt\": \"2026-11-06T15:45:00\",\n" +
            "        \"updatedAt\": \"2026-11-07T10:30:00\",\n" +
            "        \"_links\": {\n" +
            "          \"games\": {\n" +
            "            \"href\": \"http://localhost:8080/api/game/v2\",\n" +
            "            \"type\": \"GET\"\n" +
            "          },\n" +
            "          \"self\": {\n" +
            "            \"href\": \"http://localhost:8080/api/game/v2/3\",\n" +
            "            \"type\": \"GET\"\n" +
            "          },\n" +
            "          \"find-by-name\": {\n" +
            "            \"href\": \"http://localhost:8080/api/game/v1/search?name=Hollow%20Knight\",\n" +
            "            \"type\": \"GET\"\n" +
            "          },\n" +
            "          \"create\": {\n" +
            "            \"href\": \"http://localhost:8080/api/game/v2\",\n" +
            "            \"type\": \"POST\"\n" +
            "          },\n" +
            "          \"update\": {\n" +
            "            \"href\": \"http://localhost:8080/api/game/v2/3\",\n" +
            "            \"type\": \"PUT\"\n" +
            "          },\n" +
            "          \"delete\": {\n" +
            "            \"href\": \"http://localhost:8080/api/game/v1/3\",\n" +
            "            \"type\": \"DELETE\"\n" +
            "          },\n" +
            "          \"developer\": {\n" +
            "            \"href\": \"http://localhost:8080/api/developer/v1/2\",\n" +
            "            \"type\": \"GET\"\n" +
            "          },\n" +
            "          \"category\": {\n" +
            "            \"href\": \"http://localhost:8080/api/category/v2/2\",\n" +
            "            \"type\": \"GET\"\n" +
            "          },\n" +
            "          \"same-status\": {\n" +
            "            \"href\": \"http://localhost:8080/api/game/v1/status/RELEASED\",\n" +
            "            \"type\": \"GET\"\n" +
            "          },\n" +
            "          \"v1\": {\n" +
            "            \"href\": \"http://localhost:8080/api/game/v1/3\",\n" +
            "            \"type\": \"GET\"\n" +
            "          }\n" +
            "        }\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  \"_links\": {\n" +
            "    \"first\": {\n" +
            "      \"href\": \"http://localhost:8080/api/game/v2?page=0&size=10\"\n" +
            "    },\n" +
            "    \"self\": {\n" +
            "      \"href\": \"http://localhost:8080/api/game/v2?page=0&size=10\"\n" +
            "    },\n" +
            "    \"last\": {\n" +
            "      \"href\": \"http://localhost:8080/api/game/v2?page=0&size=10\"\n" +
            "    }\n" +
            "  },\n" +
            "  \"page\": {\n" +
            "    \"size\": 10,\n" +
            "    \"totalElements\": 1,\n" +
            "    \"totalPages\": 1,\n" +
            "    \"number\": 0\n" +
            "  }\n" +
            "}";

    public GameV2ResponseDTO(Game game) {
        this(
                game.getId(),
                game.getName(),
                game.getStatus(),
                game.getDeveloper().getName(),
                game.getCategory().getName(),
                game.getPlatforms().stream().map(Platform::getName).toList(),
                game.getTotalAchievements(),
                game.getCreatedAt(),
                game.getUpdatedAt()
        );
    }
}
