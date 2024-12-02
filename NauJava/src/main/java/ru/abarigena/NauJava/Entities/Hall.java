package ru.abarigena.NauJava.Entities;

import jakarta.persistence.*;

/**
 * Представляет зал кинотеатра с уникальным названием и статусом активности.
 */
@Entity
@Table(name = "halls")
public class Hall {
    @Id
    @GeneratedValue
    private Long id;

    /**
     * Название зала. Должно быть уникальным, чтобы избежать дублирования залов в базе данных.
     */
    @Column(unique = true)
    private String name;

    /**
     * Активен зал или нет.
     */
    @Column
    private boolean active;

    /**
     *Геттеры и сеттеры для каждого поля
     **/

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
