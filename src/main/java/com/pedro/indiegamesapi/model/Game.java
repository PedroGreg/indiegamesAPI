package com.pedro.indiegamesapi.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.time.LocalDate;
import java.util.Objects;

@Entity
public class Game {

    private @Id
    @GeneratedValue Long id;
    private String title;
    private String description;
    private String genre;
    private String platforms;
    private String url;
    private String developer;
    private LocalDate realeaseDate;

    public Game() {
    }

    public Game(Long id, String title, String description, String genre, String platforms, String url, String developer, LocalDate realeaseDate) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.genre = genre;
        this.platforms = platforms;
        this.url = url;
        this.developer = developer;
        this.realeaseDate = realeaseDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getPlatforms() {
        return platforms;
    }

    public void setPlatforms(String platforms) {
        this.platforms = platforms;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDeveloper() {
        return developer;
    }

    public void setDeveloper(String developer) {
        this.developer = developer;
    }

    public LocalDate getRealeaseDate() {
        return realeaseDate;
    }

    public void setRealeaseDate(LocalDate realeaseDate) {
        this.realeaseDate = realeaseDate;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Game game = (Game) o;
        return Objects.equals(id, game.id) && Objects.equals(title, game.title) && Objects.equals(description, game.description) && Objects.equals(genre, game.genre) && Objects.equals(platforms, game.platforms) && Objects.equals(url, game.url) && Objects.equals(developer, game.developer) && Objects.equals(realeaseDate, game.realeaseDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, description, genre, platforms, url, developer, realeaseDate);
    }

    @Override
    public String toString() {
        return "Game{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", genre='" + genre + '\'' +
                ", platforms='" + platforms + '\'' +
                ", url='" + url + '\'' +
                ", developer='" + developer + '\'' +
                ", realeaseDate=" + realeaseDate +
                '}';
    }
}
