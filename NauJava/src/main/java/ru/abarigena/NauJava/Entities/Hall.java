package ru.abarigena.NauJava.Entities;

import jakarta.persistence.*;

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

    @Column
    private boolean active;

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
