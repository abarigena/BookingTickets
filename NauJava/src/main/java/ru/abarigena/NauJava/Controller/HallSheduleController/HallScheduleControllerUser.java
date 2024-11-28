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
import ru.abarigena.NauJava.Service.HallRowService.HallRowService;
import ru.abarigena.NauJava.Service.HallSheduleService.HallSheduleService;
import ru.abarigena.NauJava.Service.TicketService.TicketService;
import ru.abarigena.NauJava.Service.UserService.UserService;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Контроллер для работы с расписанием залов и бронированием билетов для пользователей.
 */
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

    /**
     * Получение списка ближайших расписаний (7 дней).
     *
     * @param day   выбранный день (необязательный параметр)
     * @param model объект модели
     * @return путь к шаблону с расписаниями
     */
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

        return "userView/upcomingSchedules";
    }

    /**
     * Получение схемы зала с информацией о занятых местах.
     *
     * @param scheduleId идентификатор расписания
     * @param model      объект модели
     * @return путь к шаблону со схемой зала
     */
    @GetMapping("/pickPlace/{scheduleId}")
    public String getHallSchema(@PathVariable Long scheduleId, Model model) {
        // Получаем расписание
        HallShedule schedule = hallSheduleService.findHallSheduleById(scheduleId);

        // Получаем все ряды в зале
        List<HallRow> hallRows = hallRowService.findRowByHall(schedule.getHall());

        // Формируем карту занятых мест (ключ: ряд, значение: список занятых мест)
        Map<Integer, List<Integer>> bookedSeats = ticketService.getBookedSeats(schedule);

        model.addAttribute("schedule", schedule);
        model.addAttribute("hallRows", hallRows);
        model.addAttribute("bookedSeats", bookedSeats);

        return "userView/hallSchema";
    }

    /**
     * Подтверждение выбранных мест.
     *
     * @param scheduleId       идентификатор расписания
     * @param selectedSeats    список выбранных мест (формат "ряд-место")
     * @param principal        объект с данными текущего пользователя
     * @param redirectAttributes объект для передачи данных при перенаправлении
     * @return перенаправление на страницу подтверждения или выбора мест при ошибке
     */
    @PostMapping("/pickPlace/{scheduleId}")
    public String confirmSeats(@PathVariable Long scheduleId,
                               @RequestParam List<String> selectedSeats, // получаем строки с выбранными местами
                               Principal principal,
                               RedirectAttributes redirectAttributes) {

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
            return "redirect:/bookTicket/pickPlace/" + scheduleId ;
        }

        return "redirect:/bookTicket/confirmation";
    }

    /**
     * Отображение страницы подтверждения.
     *
     * @param model объект модели
     * @return путь к шаблону подтверждения
     */
    @GetMapping("/confirmation")
    public String showConfirmationPage(Model model) {
        return "userView/confirmation";
    }

}
