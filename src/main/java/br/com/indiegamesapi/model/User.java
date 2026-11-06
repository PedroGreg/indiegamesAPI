package br.com.indiegamesapi.model;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Schema(name = "User", example = "{\n" +
        "  \"id\": 1,\n" +
        "  \"name\": \"Pedro Gregorio\",\n" +
        "  \"email\": \"pedro@email.com\",\n" +
        "  \"password\": \"senha123\",\n" +
        "  \"userProfile\": {\n" +
        "    \"id\": 1,\n" +
        "    \"bio\": \"Desenvolvedor Backend e Entusiasta de Jogos\",\n" +
        "    \"user\": {\n" +
        "      \"id\": 1,\n" +
        "      \"name\": \"Pedro Gregorio\",\n" +
        "      \"email\": \"pedro@email.com\",\n" +
        "      \"password\": \"senha123\",\n" +
        "      \"createdAt\": \"2026-11-06T15:45:00\",\n" +
        "      \"updatedAt\": \"2026-11-07T10:30:00\"\n" +
        "    },\n" +
        "    \"createdAt\": \"2026-11-06T15:45:00\",\n" +
        "    \"updatedAt\": \"2026-11-07T10:30:00\"\n" +
        "  },\n" +
        "  \"createdAt\": \"2026-11-06T15:45:00\",\n" +
        "  \"updatedAt\": \"2026-11-07T10:30:00\"\n" +
        "}")
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    private String email;
    private String password = null;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private UserProfile userProfile;
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public User() {
    }

    public User(LocalDateTime updatedAt, LocalDateTime createdAt, UserProfile userProfile, String password, String email, String name) {
        this.updatedAt = updatedAt;
        this.createdAt = createdAt;
        this.userProfile = userProfile;
        this.password = password;
        this.email = email;
        this.name = name;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
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
