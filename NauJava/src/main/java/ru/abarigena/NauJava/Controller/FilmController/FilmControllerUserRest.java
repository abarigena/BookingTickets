package ru.abarigena.NauJava.Controller.FilmController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.abarigena.NauJava.Entities.Film;
import ru.abarigena.NauJava.Entities.Hall;
import ru.abarigena.NauJava.Entities.HallShedule;
import ru.abarigena.NauJava.Service.FilmService.FilmService;
import ru.abarigena.NauJava.Service.HallSheduleService.HallSheduleService;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/filmsUser")
public class FilmControllerUserRest {
    private final FilmService filmService;
    private final HallSheduleService hallSheduleService;

    @Autowired
    public FilmControllerUserRest(FilmService filmService, HallSheduleService hallSheduleService) {
        this.filmService = filmService;
        this.hallSheduleService = hallSheduleService;
    }

    @GetMapping("/{id}")
    public Map<String, Object> viewFilm(@PathVariable("id") Long id,
                                        @RequestParam(value = "day", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate day) {
        Film film = filmService.findFimById(id);

        LocalDate today = LocalDate.now();
        Map<LocalDate, Map<Hall, List<HallShedule>>> schedules = hallSheduleService.getSchedulesForFilm(film);

        Map<LocalDate, Map<Hall, List<HallShedule>>> filteredSchedules = day != null
                ? Map.of(day, schedules.getOrDefault(day, Map.of()))
                : schedules;

        return Map.of(
                "film", film,
                "uniqueDays", today.datesUntil(today.plusDays(8)).collect(Collectors.toList()),
                "filteredSchedules", filteredSchedules
        );
    }
}
