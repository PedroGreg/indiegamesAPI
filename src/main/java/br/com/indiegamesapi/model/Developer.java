package br.com.indiegamesapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
// NOVO: anotações do Bean Validation. Na entidade elas são checadas pelo Hibernate antes de
// INSERT/UPDATE (última barreira); os RequestDTOs validam antes, na entrada da requisição.
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "developers")
public class Developer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    // NOVO: validação do nome também na entidade (requisito "Bean Validation em cada entidade")
    @NotBlank(message = "O name do desenvolvedor deve ser preenchido")
    @Size(min = 2, max = 255, message = "O name do desenvolvedor deve ter entre 2 e 255 caracteres")
    private String name;
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    // NOVO: lado "um" do relacionamento One-to-Many (um Developer tem vários games).
    // mappedBy = "developer" aponta para o campo da classe Game que é dono da chave estrangeira,
    // então nenhuma coluna nova é criada: o JPA só usa a FK que já existe na tabela games.
    // @JsonIgnore evita loop infinito se alguém serializar a entidade direto (game -> developer -> games -> ...).
    @OneToMany(mappedBy = "developer")
    @JsonIgnore
    private List<Game> games = new ArrayList<>();

    public Developer() {
    }

    public Developer(String name, LocalDateTime createdAt, LocalDateTime updatedAt) {
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

    // NOVO: getter/setter da lista de games (lado One-to-Many)
    public List<Game> getGames() {
        return games;
    }

    public void setGames(List<Game> games) {
        this.games = games;
    }
}
