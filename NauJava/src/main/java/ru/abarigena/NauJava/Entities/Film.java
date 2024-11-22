package ru.abarigena.NauJava.Entities;

import jakarta.persistence.*;

/**
 * Представляет фильм с информацией о названии, возрастном ограничении, длительности, описании и ссылке на изображение.
 */
@Entity
@Table(name = "films")
public class Film {
    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true)
    private String title;

    @Column
    private int minAge;

    @Column
    private int duration;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column
    private String imageUrl;

    /**
     *Геттеры и сеттеры для каждого поля
     **/

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getMinAge() {
        return minAge;
    }

    public void setMinAge(int minAge) {
        this.minAge = minAge;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
