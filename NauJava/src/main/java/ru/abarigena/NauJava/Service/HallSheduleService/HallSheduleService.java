package ru.abarigena.NauJava.Service.HallSheduleService;

import ru.abarigena.NauJava.Entities.Film;
import ru.abarigena.NauJava.Entities.Hall;
import ru.abarigena.NauJava.Entities.HallShedule;

import java.time.LocalDateTime;
import java.util.List;

public interface HallSheduleService {
    HallShedule findHallSheduleById(Long id);

    List<HallShedule> findAllHallShedules();

    List<HallShedule> findAllHallShedulesByDate(LocalDateTime date);

    List<HallShedule> findAllHallShedulesByFilm(Film film);

    List<HallShedule> findAllHallShedulesByHall(Hall hall);

    HallShedule createHallShedule(LocalDateTime date, Film film, Hall hall);

    HallShedule updateHallShedule(Long id, LocalDateTime date, Film film, Hall hall);

    void deleteHallShedule(Long id);


}
