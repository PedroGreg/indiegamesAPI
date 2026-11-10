package br.com.indiegamesapi.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
// NOVO: anotações do Bean Validation. Na entidade elas são checadas pelo Hibernate antes de
// INSERT/UPDATE (última barreira); os RequestDTOs validam antes, na entrada da requisição.
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_profile")
public class UserProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = true)
    // NOVO: a bio é opcional, mas limitada ao tamanho da coluna (VARCHAR 255)
    @Size(max = 255, message = "A bio deve ter no máximo 255 caracteres")
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
