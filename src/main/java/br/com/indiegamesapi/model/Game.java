package br.com.indiegamesapi.model;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Schema(name = "Game", example = "{\n" +
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
        "  \"updatedAt\": \"2026-11-07T10:30:00\"\n" +
        "}")
@Entity
@Table(name = "games")
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Enumerated(EnumType.STRING)
    private GameStatus status;
    @ManyToOne(fetch = FetchType.LAZY)
    private Developer developer;
    @ManyToOne(fetch = FetchType.LAZY)
    private Category category;
    @ManyToMany(fetch = FetchType.LAZY)
    private List<Platform> platforms;
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Game() {
    }

    public Game(String name, GameStatus status, Developer developer, Category category, List<Platform> platforms, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.name = name;
        this.status = status;
        this.developer = developer;
        this.category = category;
        this.platforms = platforms;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public GameStatus getStatus() {
        return status;
    }

    public void setStatus(GameStatus status) {
        this.status = status;
    }

    public Developer getDeveloper() {
        return developer;
    }

    public void setDeveloper(Developer developer) {
        this.developer = developer;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public List<Platform> getPlatforms() {
        return platforms;
    }

    public void setPlatforms(List<Platform> platforms) {
        this.platforms = platforms;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
