package ru.abarigena.NauJava.Controller.HallSheduleController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.abarigena.NauJava.Entities.Film;
import ru.abarigena.NauJava.Entities.Hall;
import ru.abarigena.NauJava.Entities.HallShedule;
import ru.abarigena.NauJava.Service.FilmService.FilmService;
import ru.abarigena.NauJava.Service.HallService.HallService;
import ru.abarigena.NauJava.Service.HallSheduleService.HallSheduleService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * REST-контроллер для управления расписаниями залов.
 */
@RestController
@RequestMapping("/api/hallShedules")
public class HallSheduleControllerRest {
    private final HallSheduleService hallSheduleService;
    private final FilmService filmService;
    private final HallService hallService;

    @Autowired
    public HallSheduleControllerRest(HallSheduleService hallSheduleService, FilmService filmService, HallService hallService) {
        this.hallSheduleService = hallSheduleService;
        this.filmService = filmService;
        this.hallService = hallService;
    }

    /**
     * Создает расписания для указанного фильма в зале.
     *
     * @param filmId ID фильма.
     * @param hallId ID зала.
     * @param dates  Список дат.
     * @param times  Список времен.
     */
    @PostMapping("/create")
    public void createHallShedule(@RequestParam Long filmId, @RequestParam Long hallId,
                                  @RequestParam List<String> dates, @RequestParam List<String> times) {
        for (String date : dates) {
            for (String time : times) {
                LocalDateTime startTime = LocalDateTime.parse(date + "T" + time);
                hallSheduleService.createHallShedule(startTime, filmService.findFimById(filmId), hallService.findHallById(hallId));
            }
        }
    }

    /**
     * Обновляет существующее расписание.
     *
     * @param id      ID расписания.
     * @param date    Обновленная дата.
     * @param time    Обновленное время.
     * @param filmId  ID фильма.
     * @param hallId  ID зала.
     */
    @PutMapping("/update/{id}")
    public void editHallShedule(@PathVariable Long id, @RequestParam String date, @RequestParam String time,
                                @RequestParam Long filmId, @RequestParam Long hallId) {
        LocalDateTime newStartTime = LocalDateTime.parse(date + "T" + time);
        hallSheduleService.updateHallShedule(id, newStartTime, filmService.findFimById(filmId), hallService.findHallById(hallId));
    }

    /**
     * Удаляет расписание по его ID.
     *
     * @param id ID расписания.
     */
    @DeleteMapping("/delete/{id}")
    public void deleteHallShedule(@PathVariable Long id) {
        hallSheduleService.deleteHallShedule(id);
    }

    /**
     * Возвращает расписания залов, сгруппированные по дате, фильму и залу.
     *
     * @param day Опционально: конкретный день для фильтрации.
     * @return Сгруппированные расписания.
     */
    @GetMapping("/all")
    public Map<LocalDate, Map<Film, Map<Hall, List<HallShedule>>>> getHallSchedules(
            @RequestParam(value = "day", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate day) {
        Map<LocalDate, Map<Film, Map<Hall, List<HallShedule>>>> groupedSchedules = hallSheduleService.getGroupedSchedules();
        return (day != null) ? Map.of(day, groupedSchedules.get(day)) : groupedSchedules;
    }
}
