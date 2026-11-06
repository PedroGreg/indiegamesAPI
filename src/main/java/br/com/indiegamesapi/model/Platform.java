package br.com.indiegamesapi.model;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Schema(name = "Platform", example = "{\n" +
        "  \"id\": 1,\n" +
        "  \"name\": \"PC (Steam)\",\n" +
        "  \"createdAt\": \"2026-11-06T15:45:00\",\n" +
        "  \"updatedAt\": \"2026-11-07T10:30:00\"\n" +
        "}")
@Entity
// Evita erro 500 ao serializar proxies LAZY do Hibernate (ex.: game.developer / game.category)
//@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "platforms")
public class Platform {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 50)
    private String name;
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Platform() {
    }

    public Platform(String name, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.name = name;
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
