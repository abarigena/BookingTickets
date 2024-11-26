package ru.abarigena.NauJava.Controller.HallSheduleController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

@Controller
@RequestMapping("/admin/hallShedules")
public class HallSheduleController {
    private final HallSheduleService hallSheduleService;
    private final FilmService filmService;
    private final HallService hallService;

    @Autowired
    public HallSheduleController(HallSheduleService hallSheduleService, FilmService filmService, HallService hallService) {
        this.hallSheduleService = hallSheduleService;
        this.filmService = filmService;
        this.hallService = hallService;
    }

    @GetMapping("/create")
    public String createHallShedule(Model model) {
        model.addAttribute("hallShedule", new HallShedule());
        model.addAttribute("films", filmService.findAllFilms());
        model.addAttribute("halls", hallService.findAllHalls());
        return "createHallShedule";
    }

    @PostMapping("/create")
    public String createHallShedule(
            @RequestParam("filmId") Long filmId,
            @RequestParam("hallId") Long hallId,
            @RequestParam("dates") List<String> dates,
            @RequestParam("times") List<String> times) {

        for (String date : dates) {
            for (String time : times) {
                LocalDateTime startTime = LocalDateTime.parse(date + "T" + time);
                hallSheduleService.createHallShedule(startTime, filmService.findFimById(filmId),
                        hallService.findHallById(hallId));
            }
        }

        return "redirect:/admin/hallShedules";
    }

    @GetMapping("/edit/{id}")
    public String editHallShedule(@PathVariable Long id, Model model) {
        HallShedule hallShedule = hallSheduleService.findHallSheduleById(id);
        model.addAttribute("hallShedule", hallShedule);
        model.addAttribute("films", filmService.findAllFilms());
        model.addAttribute("halls", hallService.findAllHalls());
        return "editHallShedule";
    }

    @PostMapping("/edit/{id}")
    public String editHallShedule(@PathVariable Long id,
                                  @RequestParam("date") String date,
                                  @RequestParam("time") String time,
                                  @RequestParam("filmId") Long filmId,
                                  @RequestParam("hallId") Long hallId) {

        // Парсинг даты и времени
        LocalDateTime newStartTime = LocalDateTime.parse(date + "T" + time);

        hallSheduleService.updateHallShedule(id, newStartTime, filmService.findFimById(filmId),
                hallService.findHallById(hallId));
        return "redirect:/admin/hallShedules";
    }

    @PostMapping("/delete/{id}")
    public String deleteHallShedule(@PathVariable Long id) {
        hallSheduleService.deleteHallShedule(id);
        return "redirect:/admin/hallShedules";
    }

    @GetMapping
    public String getHallSchedules(
            @RequestParam(value = "day", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate day,
            Model model) {
        List<LocalDate> uniqueDays = hallSheduleService.findUniqueDays();

        // Получаем все расписания, сгруппированные так, чтобы включать объекты HallShedule
        Map<LocalDate, Map<Film, Map<Hall, List<HallShedule>>>> groupedSchedules =
                hallSheduleService.getGroupedSchedules();

        // Фильтруем только по указанному дню, если он передан
        Map<LocalDate, Map<Film, Map<Hall, List<HallShedule>>>> filteredSchedules =
                day != null ? Map.of(day, groupedSchedules.get(day)) : groupedSchedules;

        model.addAttribute("uniqueDays", uniqueDays);
        model.addAttribute("groupedSchedules", filteredSchedules);

        return "hallShedules";
    }
}
