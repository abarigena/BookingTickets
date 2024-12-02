package ru.abarigena.NauJava.Controller.HallSheduleController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.abarigena.NauJava.Entities.HallRow;
import ru.abarigena.NauJava.Entities.HallShedule;
import ru.abarigena.NauJava.Entities.Ticket.Ticket;
import ru.abarigena.NauJava.Entities.User.User;
import ru.abarigena.NauJava.Service.HallRowService.HallRowService;
import ru.abarigena.NauJava.Service.HallSheduleService.HallSheduleService;
import ru.abarigena.NauJava.Service.TicketService.TicketService;
import ru.abarigena.NauJava.Service.UserService.UserService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * REST-контроллер для работы с бронированиями мест и расписаниями зала.
 */
@RestController
@RequestMapping("/api/bookings")
public class HallScheduleControllerUserRest {
    private final HallSheduleService hallSheduleService;
    private final HallRowService hallRowService;
    private final TicketService ticketService;
    private final UserService userService;

    @Autowired
    public HallScheduleControllerUserRest(HallSheduleService hallSheduleService, HallRowService hallRowService,
                                          TicketService ticketService, UserService userService) {
        this.hallSheduleService = hallSheduleService;
        this.hallRowService = hallRowService;
        this.ticketService = ticketService;
        this.userService = userService;
    }

    /**
     * Возвращает данные о расписании зала и занятых местах.
     *
     * @param scheduleId ID расписания.
     * @return Карта с информацией о расписании, рядах и занятых местах.
     */
    @GetMapping("/pickPlace/{scheduleId}")
    public Map<String, Object> getHallSchema(@PathVariable Long scheduleId) {
        HallShedule schedule = hallSheduleService.findHallSheduleById(scheduleId);
        List<HallRow> hallRows = hallRowService.findRowByHall(schedule.getHall());
        Map<Integer, List<Integer>> bookedSeats = ticketService.getBookedSeats(schedule);

        return Map.of(
                "schedule", schedule,
                "hallRows", hallRows,
                "bookedSeats", bookedSeats
        );
    }

    /**
     * Подтверждает выбор мест и создает билеты.
     *
     * @param scheduleId    ID расписания.
     * @param userId        ID пользователя.
     * @param selectedSeats Список выбранных мест в формате "ряд-место".
     * @return Ответ с подтверждением или ошибкой.
     */
    @PostMapping("/pickPlace/{scheduleId}")
    public ResponseEntity<String> confirmSeats(@PathVariable Long scheduleId,
                                               @RequestParam Long userId,
                                               @RequestBody List<String> selectedSeats) {
        HallShedule schedule = hallSheduleService.findHallSheduleById(scheduleId);
        User user = userService.findById(userId);

        List<Ticket> tickets = selectedSeats.stream()
                .map(seat -> {
                    String[] parts = seat.split("-");
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
            ticketService.confirmSeats(schedule, tickets, user.getEmail());
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body("Место уже занято");
        }

        return ResponseEntity.ok("Билеты подтверждены");
    }
}
