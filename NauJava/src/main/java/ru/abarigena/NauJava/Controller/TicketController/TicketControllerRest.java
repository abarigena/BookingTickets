package ru.abarigena.NauJava.Controller.TicketController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.abarigena.NauJava.Entities.Ticket.Ticket;
import ru.abarigena.NauJava.Service.TicketService.TicketService;

import java.util.List;

/**
 * REST-контроллер для управления билетами.
 */
@RestController
@RequestMapping("/api/tickets")
public class TicketControllerRest {
    private final TicketService ticketService;

    @Autowired
    public TicketControllerRest(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    /**
     * Ищет активные билеты по идентификатору.
     *
     * @param identifier Идентификатор пользователя или билета.
     * @return Список активных билетов.
     */
    @PostMapping("/search")
    public List<Ticket> searchTickets(@RequestParam String identifier) {
        return ticketService.getActiveTicketsByIdentifier(identifier);
    }

    /**
     * Отменяет бронирование билета.
     *
     * @param ticketId ID билета.
     */
    @PostMapping("/cancel/{ticketId}")
    public void cancelTicket(@PathVariable Long ticketId) {
        ticketService.cancelBookTicket(ticketId);
    }
}
