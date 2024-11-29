package ru.abarigena.NauJava.Controller.TicketController;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.abarigena.NauJava.Entities.Ticket.Ticket;
import ru.abarigena.NauJava.Entities.Ticket.TicketHistory;
import ru.abarigena.NauJava.Entities.User.User;
import ru.abarigena.NauJava.Service.TicketHistoryServise.TicketHistoryService;
import ru.abarigena.NauJava.Service.TicketService.TicketService;
import ru.abarigena.NauJava.Service.UserService.UserService;

import java.security.Principal;
import java.util.List;

/**
 * Контроллер для управления билетами пользователя: активные билеты, история и отмена бронирования.
 */
@Controller
@RequestMapping("/tickets")
public class TicketControllerView {
    private final TicketService ticketService;
    private final UserService userService;
    private final TicketHistoryService ticketHistoryService;

    public TicketControllerView(TicketService ticketService, UserService userService, TicketHistoryService ticketHistoryService) {
        this.ticketService = ticketService;
        this.userService = userService;
        this.ticketHistoryService = ticketHistoryService;
    }

    /**
     * Получение списка активных билетов для текущего пользователя.
     *
     * @param model     объект модели для передачи данных в шаблон
     * @param principal объект для получения информации о текущем пользователе
     * @return путь к шаблону со списком активных билетов
     */
    @GetMapping("/active")
    public String getActiveTickets(Model model, Principal principal) {
        String username = principal.getName();
        User user = userService.findByUsername(username);
        Long userId = user.getId();

        List<Ticket> activeTickets = ticketService.getActiveTicketsByUserId(userId);

        model.addAttribute("tickets", activeTickets);
        return "tickets/active";
    }

    /**
     * Получение истории билетов пользователя, включая отмененные и просроченные билеты.
     *
     * @param model     объект модели для передачи данных в шаблон
     * @param principal объект для получения информации о текущем пользователе
     * @return путь к шаблону с историей билетов
     */
    @GetMapping("/history")
    public String getTicketHistory(Model model, Principal principal) {

        String username = principal.getName();
        User user = userService.findByUsername(username);
        Long userId = user.getId();

        List<TicketHistory> tickets = ticketHistoryService.getCanceledAndExpiredTicketsForUser(userId);
        model.addAttribute("tickets", tickets);
        return "tickets/history";
    }

    /**
     * Отмена билета по его ID.
     *
     * @param id        идентификатор билета
     * @param principal объект для получения информации о текущем пользователе
     * @return перенаправление на страницу с активными билетами
     */
    @PostMapping("/cancel/{id}")
    public String cancelTicket(@PathVariable Long id, Principal principal) {
        ticketService.cancelBookTicket(id);
        return "redirect:/tickets/active";
    }
}
