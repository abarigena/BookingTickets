package ru.abarigena.NauJava.Entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * Представляет расписание зала, включая время начала, фильм и зал.
 */
@Entity
@Table(name = "hallShedules")
public class HallShedule {
    @Id
    @GeneratedValue
    private Long id;

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime startTime;

    @ManyToOne
    private Film film;

    @ManyToOne
    private Hall hall;

    /**
     *Геттеры и сеттеры для каждого поля
     **/

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public Film getFilm() {
        return film;
    }

    public void setFilm(Film film) {
        this.film = film;
    }

    public Hall getHall() {
        return hall;
    }

    public void setHall(Hall hall) {
        this.hall = hall;
    }
}
