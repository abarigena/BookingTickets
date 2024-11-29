package ru.abarigena.NauJava.Controller.TicketController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.abarigena.NauJava.Entities.Ticket.Ticket;
import ru.abarigena.NauJava.Service.TicketService.TicketService;

import java.util.List;

@Controller
@RequestMapping("admin/tickets")
public class TicketController {
    private final TicketService ticketService;

    @Autowired
    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    /**
     * Страница для поиска пользователя и отображения его активных билетов
     */
    @GetMapping("/search")
    public String showSearchForm(Model model) {
        model.addAttribute("identifier", "");
        return "admin/tickets/search";
    }

    @PostMapping("/search")
    public String searchTickets(@RequestParam("identifier") String identifier, Model model) {
        List<Ticket> activeTickets = ticketService.getActiveTicketsByIdentifier(identifier);
        model.addAttribute("tickets", activeTickets);
        return "admin/tickets/active";
    }

    /**
     * Отмена бронирования билета
     */
    @PostMapping("/cancel/{ticketId}")
    public String cancelTicket(@PathVariable Long ticketId) {
        ticketService.cancelBookTicket(ticketId);
        return "redirect:/admin/tickets/search";
    }
}
