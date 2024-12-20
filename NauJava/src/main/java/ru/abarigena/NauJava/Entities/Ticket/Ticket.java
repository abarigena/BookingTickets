package ru.abarigena.NauJava.Entities.Ticket;

import jakarta.persistence.*;
import ru.abarigena.NauJava.Entities.HallShedule.HallShedule;
import ru.abarigena.NauJava.Entities.User.User;

/**
 * Представляет билет, приобретенный пользователем на определенное место в расписании зала.
 */
@Entity
@Table(name = "tickets")
public class Ticket {

    @Id
    @GeneratedValue
    private Long id;

    @Column
    private int row;

    @Column
    private int seat;

    @ManyToOne
    private User user;

    @ManyToOne
    private HallShedule hallShedule;

    public Ticket(){

    }

    public Ticket(int row, int seat){
        this.row = row;
        this.seat = seat;
    }

    /**
    *Геттеры и сеттеры для каждого поля
     **/

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getSeat() {
        return seat;
    }

    public void setSeat(int seat) {
        this.seat = seat;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public HallShedule getHallShedule() {
        return hallShedule;
    }

    public void setHallShedule(HallShedule hallShedule) {
        this.hallShedule = hallShedule;
    }
}
