package ru.abarigena.NauJava.Entities.HallShedule;

import ru.abarigena.NauJava.Entities.Film;
import ru.abarigena.NauJava.Entities.Hall;

import java.time.LocalDate;
import java.util.List;

public class GroupedSchedule {
    private LocalDate date;
    private Film film;
    private Hall hall;
    private List<HallShedule> schedules;

    public GroupedSchedule(LocalDate date, Film film, Hall hall, List<HallShedule> schedules) {
        this.date = date;
        this.film = film;
        this.hall = hall;
        this.schedules = schedules;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
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

    public List<HallShedule> getSchedules() {
        return schedules;
    }

    public void setSchedules(List<HallShedule> schedules) {
        this.schedules = schedules;
    }
}
