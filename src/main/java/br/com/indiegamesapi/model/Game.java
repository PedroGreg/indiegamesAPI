package br.com.indiegamesapi.model;

import jakarta.persistence.*;
// NOVO: anotações do Bean Validation. Na entidade elas são checadas pelo Hibernate antes de
// INSERT/UPDATE (última barreira); os RequestDTOs validam antes, na entrada da requisição.
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
    // NOVO: validação do nome também na entidade (requisito "Bean Validation em cada entidade")
    @NotBlank(message = "O name do game deve ser preenchido")
    @Size(min = 1, max = 255, message = "O name do game deve ter entre 1 e 255 caracteres")
    private String name;
    @Enumerated(EnumType.STRING)
    // NOVO: o status é obrigatório (antes um game podia ser salvo sem status)
    @NotNull(message = "O status do game deve ser informado")
    private GameStatus status;
    @ManyToOne(fetch = FetchType.LAZY)
    // NOVO: todo game precisa de um desenvolvedor (lado "muitos" do One-to-Many com Developer)
    @NotNull(message = "O developer do game deve ser informado")
    private Developer developer;
    @ManyToOne(fetch = FetchType.LAZY)
    // NOVO: todo game precisa de uma categoria (lado "muitos" do One-to-Many com Category)
    @NotNull(message = "A category do game deve ser informada")
    private Category category;
    @ManyToMany(fetch = FetchType.LAZY)
    // NOVO: a lista pode ser vazia, mas não nula
    @NotNull(message = "A lista de platforms não pode ser nula")
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
