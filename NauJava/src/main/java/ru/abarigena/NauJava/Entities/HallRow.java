package ru.abarigena.NauJava.Entities;

import jakarta.persistence.*;

/**
 * Представляет ряд в зале с номером ряда и количеством мест.
 * Связан с конкретным залом.
 */
@Entity
@Table(name = "hallRows", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"row", "hall_id"})
})
public class HallRow {
    @Id
    @GeneratedValue
    private Long id;

    @Column
    private int row;

    @Column
    private int seatCount;

    @ManyToOne
    @JoinColumn(name = "hall_id")
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

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getSeatCount() {
        return seatCount;
    }

    public void setSeatCount(int seatCount) {
        this.seatCount = seatCount;
    }

    public Hall getHall() {
        return hall;
    }

    public void setHall(Hall hall) {
        this.hall = hall;
    }
}
