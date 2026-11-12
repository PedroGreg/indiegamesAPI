package br.com.indiegamesapi.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "games")
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    @NotBlank(message = "O name do game deve ser preenchido")
    @Size(min = 1, max = 255, message = "O name do game deve ter entre 1 e 255 caracteres")
    private String name;
    @Enumerated(EnumType.STRING)
    @NotNull(message = "O status do game deve ser informado")
    private GameStatus status;
    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull(message = "O developer do game deve ser informado")
    private Developer developer;
    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull(message = "A category do game deve ser informada")
    private Category category;
    @ManyToMany(fetch = FetchType.LAZY)
    @NotNull(message = "A lista de platforms não pode ser nula")
    private List<Platform> platforms;
    @NotNull(message = "O totalAchievements deve ser informado")
    @PositiveOrZero(message = "O totalAchievements não pode ser negativo")
    @Column(name = "total_achievements", nullable = false)
    private Integer totalAchievements = 0;
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

    public Integer getTotalAchievements() {
        return totalAchievements;
    }

    public void setTotalAchievements(Integer totalAchievements) {
        this.totalAchievements = totalAchievements;
    }
}
