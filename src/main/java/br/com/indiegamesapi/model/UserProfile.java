package br.com.indiegamesapi.model;

import io.swagger.v3.oas.annotations.media.Schema;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Schema(name = "UserProfile", example = "{\n" +
        "  \"id\": 1,\n" +
        "  \"bio\": \"Desenvolvedor Backend e Entusiasta de Jogos\",\n" +
        "  \"user\": {\n" +
        "    \"id\": 1,\n" +
        "    \"name\": \"Pedro Gregorio\",\n" +
        "    \"email\": \"pedro@email.com\",\n" +
        "    \"password\": \"senha123\",\n" +
        "    \"createdAt\": \"2026-11-06T15:45:00\",\n" +
        "    \"updatedAt\": \"2026-11-07T10:30:00\"\n" +
        "  },\n" +
        "  \"createdAt\": \"2026-11-06T15:45:00\",\n" +
        "  \"updatedAt\": \"2026-11-07T10:30:00\"\n" +
        "}")
@Entity
@Table(name = "user_profile")
public class UserProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = true)
    private String bio;
    @OneToOne(mappedBy = "userProfile")
    @JsonIgnoreProperties("userProfile")
    private User user;
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public UserProfile() {
    }

    public UserProfile(String bio, User user, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.bio = bio;
        this.user = user;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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
