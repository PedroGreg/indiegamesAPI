package br.com.indiegamesapi.dto.response;

import br.com.indiegamesapi.model.Category;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(name = "CategoryResponseDTO", example = "{\"id\" : \" 1 \", \n" +
        "\"name\": \"RPG\", \n" +
        "\"createdAt\": \"localdatetime\", \n" +
        "\"updatedAt\": \"localdatetime\"}")
public record CategoryResponseDTO(
        Long id,
        String name,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public CategoryResponseDTO(Category category) {
        this(
                category.getId(),
                category.getName(),
                category.getCreatedAt(),
                category.getUpdatedAt()
        );
    }
}