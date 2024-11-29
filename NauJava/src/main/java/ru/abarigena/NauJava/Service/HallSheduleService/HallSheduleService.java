package ru.abarigena.NauJava.Service.HallSheduleService;

import ru.abarigena.NauJava.Entities.Film;
import ru.abarigena.NauJava.Entities.Hall;
import ru.abarigena.NauJava.Entities.HallShedule;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

public interface HallSheduleService {
    HallShedule findHallSheduleById(Long id);

    List<HallShedule> findAllHallShedules();

    List<HallShedule> findAllHallShedulesByDate(LocalDateTime date);

    List<HallShedule> findAllHallShedulesByFilm(Film film);

    List<HallShedule> findAllHallShedulesByHall(Hall hall);

    HallShedule createHallShedule(LocalDateTime date, Film film, Hall hall);

    HallShedule updateHallShedule(Long id, LocalDateTime date, Film film, Hall hall);

    void deleteHallShedule(Long id);

    List<LocalDate> findUniqueDays();

    Map<LocalDate, Map<Film, Map<Hall, List<HallShedule>>>> getGroupedSchedules();

    Map<Long, List<LocalTime>> getSchedules();

    Map<LocalDate, Map<Film, Map<Hall, List<HallShedule>>>> getUpcomingSchedules();

    Map<LocalDate, Map<Hall, List<HallShedule>>> getSchedulesForFilm(Film film);
}
