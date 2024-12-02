package ru.abarigena.NauJava.Controller.FilmController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.abarigena.NauJava.Entities.Film;
import ru.abarigena.NauJava.Entities.Hall;
import ru.abarigena.NauJava.Entities.HallShedule;
import ru.abarigena.NauJava.Service.FilmService.FilmService;
import ru.abarigena.NauJava.Service.HallSheduleService.HallSheduleService;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Контроллер для отображения информации о фильмах для пользователей.
 */
@Controller
@RequestMapping("/filmsView")
public class FilmControllerUser {
    private final FilmService filmService;
    private final HallSheduleService hallSheduleService;

    @Autowired
    public FilmControllerUser(FilmService filmService, HallSheduleService hallSheduleService) {
        this.filmService = filmService;
        this.hallSheduleService = hallSheduleService;
    }

    /**
     * Отображает информацию о фильме и его расписание.
     *
     * @param id   ID фильма
     * @param day  выбранный день для фильтрации расписания (необязательно)
     * @param model модель для передачи данных в представление
     * @return имя представления
     */
    @GetMapping("/{id}")
    public String viewFilm(@PathVariable("id") Long id,
                           @RequestParam(value = "day", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate day,
                           Model model) {
        Film film = filmService.findFimById(id);

        LocalDate today = LocalDate.now();
        LocalDate weekAhead = today.plusDays(7);

        Map<LocalDate, Map<Hall, List<HallShedule>>> schedules = hallSheduleService.getSchedulesForFilm(film);

        // Фильтрация расписания по выбранному дню
        Map<LocalDate, Map<Hall, List<HallShedule>>> filteredSchedules = day != null
                ? Map.of(day, schedules.getOrDefault(day, Map.of()))
                : schedules;

        // Добавляем данные в модель
        model.addAttribute("film", film);
        model.addAttribute("uniqueDays", today.datesUntil(weekAhead.plusDays(1)).collect(Collectors.toList()));
        model.addAttribute("filteredSchedules", filteredSchedules);

        return "userView/filmView";
    }


}
