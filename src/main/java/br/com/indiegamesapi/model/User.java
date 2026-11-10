package br.com.indiegamesapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
// NOVO: anotações do Bean Validation. Na entidade elas são checadas pelo Hibernate antes de
// INSERT/UPDATE (última barreira); os RequestDTOs validam antes, na entrada da requisição.
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    // NOVO: validação do nome também na entidade (requisito "Bean Validation em cada entidade")
    @NotBlank(message = "O name do usuário deve ser preenchido")
    @Size(min = 3, max = 255, message = "O name do usuário deve ter entre 3 e 255 caracteres")
    private String name;
    // NOVO: e-mail obrigatório, com formato válido e único no banco (ele é o "login" do usuário)
    @NotBlank(message = "O email do usuário deve ser preenchido")
    @Email(message = "O email informado não é válido")
    @Column(nullable = false, unique = true)
    private String email;
    // ALTERADO (comportamento): agora guarda o HASH BCrypt da senha (ex.: "$2a$10$..."), nunca a senha pura.
    // Sem @Size aqui porque o hash sempre tem 60 caracteres, independente do tamanho da senha digitada.
    private String password = null;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private UserProfile userProfile;
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    // NOVO (login): chave de API gerada no POST /api/auth/login. Fica null até o usuário logar
    // e volta a null no logout. unique = true: dois usuários nunca têm a mesma chave.
    // @JsonIgnore: a chave nunca aparece se alguém serializar a entidade direto.
    @JsonIgnore
    @Column(name = "api_key", unique = true)
    private String apiKey;

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

    // NOVO: getter/setter da chave de API
    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }
}
