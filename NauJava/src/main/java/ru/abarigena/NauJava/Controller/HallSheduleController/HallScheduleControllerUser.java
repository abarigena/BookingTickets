package ru.abarigena.NauJava.Controller.HallSheduleController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.abarigena.NauJava.Entities.Film;
import ru.abarigena.NauJava.Entities.Hall;
import ru.abarigena.NauJava.Entities.HallRow;
import ru.abarigena.NauJava.Entities.HallShedule;
import ru.abarigena.NauJava.Entities.Ticket.Ticket;
import ru.abarigena.NauJava.Entities.User.User;
import ru.abarigena.NauJava.Repository.HallRowRepository;
import ru.abarigena.NauJava.Repository.HallSheduleRepository;
import ru.abarigena.NauJava.Repository.TicketRepository;
import ru.abarigena.NauJava.Service.HallRowService.HallRowService;
import ru.abarigena.NauJava.Service.HallSheduleService.HallSheduleService;
import ru.abarigena.NauJava.Service.TicketService.TicketService;
import ru.abarigena.NauJava.Service.UserService.UserService;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/bookTicket")
public class HallScheduleControllerUser {
    private final HallSheduleService hallSheduleService;
    private final HallRowService hallRowService;
    private final TicketService ticketService;
    private final UserService userService;

    @Autowired
    public HallScheduleControllerUser(HallSheduleService hallSheduleService, HallRowService hallRowService,
                                      TicketService ticketService, UserService userService) {
        this.hallSheduleService = hallSheduleService;
        this.hallRowService = hallRowService;
        this.ticketService = ticketService;
        this.userService = userService;
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
        List<HallRow> hallRows = hallRowService.findRowByHall(schedule.getHall());

        // Получаем занятые места по расписанию
        List<Ticket> bookedTickets = ticketService.findByHallShedule(schedule);

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

    @PostMapping("/pickPlace/{scheduleId}")
    public String confirmSeats(@PathVariable Long scheduleId,
                               @RequestParam List<String> selectedSeats, // получаем строки с выбранными местами
                               Principal principal,
                               RedirectAttributes redirectAttributes) {
        // Выводим полученные данные
        System.out.println("Selected seats: " + selectedSeats);

        HallShedule schedule = hallSheduleService.findHallSheduleById(scheduleId);
        String username = principal.getName();
        User user = userService.findByUsername(username);

        // Преобразуем строки в объекты Ticket
        List<Ticket> tickets = selectedSeats.stream()
                .map(seat -> {
                    String[] parts = seat.split("-"); // разделяем ряд и место
                    int row = Integer.parseInt(parts[0]);
                    int seatNumber = Integer.parseInt(parts[1]);

                    Ticket ticket = new Ticket();
                    ticket.setRow(row);
                    ticket.setSeat(seatNumber);
                    ticket.setUser(user);
                    ticket.setHallShedule(schedule);
                    return ticket;
                })
                .collect(Collectors.toList());

        try {
            ticketService.confirmSeats(schedule, tickets, user.getEmail()); // Подтверждаем билеты

            redirectAttributes.addFlashAttribute("tickets", tickets);
        } catch (IllegalStateException e) {
            return "redirect:/bookTicket/pickPlace/" + scheduleId ; // Ошибка при подтверждении
        }

        return "redirect:/bookTicket/confirmation"; // Перенаправление после успешного подтверждения
    }

    @GetMapping("/confirmation")
    public String showConfirmationPage(Model model) {
        return "confirmation";
    }

}
