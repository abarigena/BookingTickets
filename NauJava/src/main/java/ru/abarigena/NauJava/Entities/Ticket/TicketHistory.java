package ru.abarigena.NauJava.Entities.Ticket;

import jakarta.persistence.*;
import ru.abarigena.NauJava.Entities.HallShedule.HallShedule;
import ru.abarigena.NauJava.Entities.User.User;

import java.time.LocalDateTime;

/**
 * Сохраняет историю изменений билета, включая пользователя, расписание и статус.
 */
@Entity
@Table(name = "ticketHistory")
public class TicketHistory {
    @Id
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private HallShedule hallShedule;

    @Column
    private int row;

    @Column
    private int seat;

    @Enumerated(EnumType.STRING)
    @Column
    private TicketStatus status;

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime date;

    /**
     *Геттеры и сеттеры для каждого поля
     **/

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
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

    public TicketStatus getStatus() {
        return status;
    }

    public void setStatus(TicketStatus status) {
        this.status = status;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Билет -" +
                " Пользователь:" + user.getUsername() +
                ", Зал: " + hallShedule.getHall().getName() +
                ", Фильм: " + hallShedule.getFilm().getTitle() +
                ", Ряд: " + row +
                ", Место: " + seat +
                ", Статус билета: " + status ;
    }
}
