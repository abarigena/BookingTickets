package ru.abarigena.NauJava.Controller.HallSheduleController;

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
import ru.abarigena.NauJava.Entities.HallRow;
import ru.abarigena.NauJava.Entities.HallShedule;
import ru.abarigena.NauJava.Entities.Ticket.Ticket;
import ru.abarigena.NauJava.Repository.HallRowRepository;
import ru.abarigena.NauJava.Repository.HallSheduleRepository;
import ru.abarigena.NauJava.Repository.TicketRepository;
import ru.abarigena.NauJava.Service.HallSheduleService.HallSheduleService;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/bookTicket")
public class HallScheduleControllerUser {
    private final HallSheduleService hallSheduleService;
    private final HallRowRepository hallRowRepository;
    private final TicketRepository ticketRepository;

    @Autowired
    public HallScheduleControllerUser(HallSheduleService hallSheduleService, HallRowRepository hallRowRepository,
                                      TicketRepository ticketRepository) {
        this.hallSheduleService = hallSheduleService;
        this.hallRowRepository = hallRowRepository;
        this.ticketRepository = ticketRepository;
    }

    @GetMapping
    public String getSchedules(@RequestParam(value = "day", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate day,
                               Model model) {
        // Текущая дата и диапазон до 7 дней вперёд
        LocalDate today = LocalDate.now();
        LocalDate weekAhead = today.plusDays(7);

        // Создаём список дат в пределах 7 дней
        List<LocalDate> validDays = today.datesUntil(weekAhead.plusDays(1)).collect(Collectors.toList());

        // Получение актуальных расписаний
        Map<LocalDate, Map<Film, Map<Hall, List<HallShedule>>>> groupedSchedules =
                hallSheduleService.getUpcomingSchedules();

        // Фильтрация по выбранному дню
        Map<LocalDate, Map<Film, Map<Hall, List<HallShedule>>>> filteredSchedules =
                day != null ? Map.of(day, groupedSchedules.getOrDefault(day, Map.of())) : groupedSchedules;

        model.addAttribute("uniqueDays", validDays);
        model.addAttribute("groupedSchedules", filteredSchedules);

        return "upcomingSchedules";
    }

    @GetMapping("/pickPlace/{scheduleId}")
    public String getHallSchema(@PathVariable Long scheduleId, Model model) {
        // Получаем расписание
        HallShedule schedule = hallSheduleService.findHallSheduleById(scheduleId);

        // Получаем все ряды в зале
        List<HallRow> hallRows = hallRowRepository.findByHallOrderByRow(schedule.getHall());

        // Получаем занятые места по расписанию
        List<Ticket> bookedTickets = ticketRepository.findByHallShedule(schedule);

        // Формируем карту занятых мест (ключ: ряд, значение: список занятых мест)
        Map<Integer, List<Integer>> bookedSeats = bookedTickets.stream()
                .collect(Collectors.groupingBy(
                        Ticket::getRow,
                        Collectors.mapping(Ticket::getSeat, Collectors.toList())
                ));

        // Передаем данные в шаблон
        model.addAttribute("schedule", schedule);
        model.addAttribute("hallRows", hallRows);
        model.addAttribute("bookedSeats", bookedSeats);

        return "hallSchema";
    }
}
