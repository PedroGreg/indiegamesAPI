package br.com.indiegamesapi.dto.response;

import br.com.indiegamesapi.model.Game;
import br.com.indiegamesapi.model.GameStatus;
import br.com.indiegamesapi.model.Platform;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.hateoas.server.core.Relation;

import java.time.LocalDateTime;
import java.util.List;

// NOVO (versionamento): game na v2, resumido. Na v1 developer, category e platforms vêm como objetos completos;
// aqui vem só o NOME de cada um (resposta menor, boa para listagens).
@Relation(itemRelation = "game", collectionRelation = "games")
@Schema(name = "GameV2ResponseDTO", example = GameV2ResponseDTO.EXAMPLE)
public record GameV2ResponseDTO(
        Long id,
        String name,
        GameStatus status,
        String developerName,
        String categoryName,
        List<String> platformNames,
        LocalDateTime createdAt
) {
    // [Swagger] exemplo de um item
    public static final String EXAMPLE = "{\n" +
            "  \"id\": 1,\n" +
            "  \"name\": \"Undertale\",\n" +
            "  \"status\": \"RELEASED\",\n" +
            "  \"developerName\": \"Toby Fox\",\n" +
            "  \"categoryName\": \"RPG\",\n" +
            "  \"platformNames\": [\n" +
            "    \"PC (Steam)\",\n" +
            "    \"Nintendo Switch\"\n" +
            "  ],\n" +
            "  \"createdAt\": \"2026-11-06T15:45:00\",\n" +
            "  \"_links\": {\n" +
            "    \"self\": {\n" +
            "      \"href\": \"http://localhost:8080/api/game/v2/1\",\n" +
            "      \"type\": \"GET\"\n" +
            "    },\n" +
            "    \"games\": {\n" +
            "      \"href\": \"http://localhost:8080/api/game/v2\",\n" +
            "      \"type\": \"GET\"\n" +
            "    },\n" +
            "    \"v1\": {\n" +
            "      \"href\": \"http://localhost:8080/api/game/1\",\n" +
            "      \"type\": \"GET\"\n" +
            "    }\n" +
            "  }\n" +
            "}";

    // [Swagger] exemplo da resposta paginada
    public static final String PAGE_EXAMPLE = "{\n" +
            "  \"_embedded\": {\n" +
            "    \"games\": [\n" +
            "      {\n" +
            "        \"id\": 1,\n" +
            "        \"name\": \"Undertale\",\n" +
            "        \"status\": \"RELEASED\",\n" +
            "        \"developerName\": \"Toby Fox\",\n" +
            "        \"categoryName\": \"RPG\",\n" +
            "        \"platformNames\": [\n" +
            "          \"PC (Steam)\",\n" +
            "          \"Nintendo Switch\"\n" +
            "        ],\n" +
            "        \"createdAt\": \"2026-11-06T15:45:00\",\n" +
            "        \"_links\": {\n" +
            "          \"self\": {\n" +
            "            \"href\": \"http://localhost:8080/api/game/v2/1\",\n" +
            "            \"type\": \"GET\"\n" +
            "          },\n" +
            "          \"games\": {\n" +
            "            \"href\": \"http://localhost:8080/api/game/v2\",\n" +
            "            \"type\": \"GET\"\n" +
            "          },\n" +
            "          \"v1\": {\n" +
            "            \"href\": \"http://localhost:8080/api/game/1\",\n" +
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
                game.getCreatedAt()
        );
    }
}
