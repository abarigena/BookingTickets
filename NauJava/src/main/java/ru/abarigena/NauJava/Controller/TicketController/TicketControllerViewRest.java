package ru.abarigena.NauJava.Controller.TicketController;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.abarigena.NauJava.Entities.Ticket.Ticket;
import ru.abarigena.NauJava.Entities.Ticket.TicketHistory;
import ru.abarigena.NauJava.Service.TicketHistoryServise.TicketHistoryService;
import ru.abarigena.NauJava.Service.TicketService.TicketService;

import java.util.List;

@RestController
@RequestMapping("/api/ticketsView")
public class TicketControllerViewRest {
    private final TicketService ticketService;
    private final TicketHistoryService ticketHistoryService;

    public TicketControllerViewRest(TicketService ticketService, TicketHistoryService ticketHistoryService) {
        this.ticketService = ticketService;
        this.ticketHistoryService = ticketHistoryService;
    }

    @GetMapping("/active")
    public ResponseEntity<List<Ticket>> getActiveTickets(@RequestParam Long userId) {
        List<Ticket> activeTickets = ticketService.getActiveTicketsByUserId(userId);
        return ResponseEntity.ok(activeTickets);
    }

    @GetMapping("/history")
    public ResponseEntity<List<TicketHistory>> getTicketHistory(@RequestParam Long userId) {
        List<TicketHistory> tickets = ticketHistoryService.getCanceledAndExpiredTicketsForUser(userId);
        return ResponseEntity.ok(tickets);
    }

    @PostMapping("/cancel/{ticketId}")
    public ResponseEntity<String> cancelTicket(@PathVariable Long ticketId) {
        ticketService.cancelBookTicket(ticketId);
        return ResponseEntity.ok("Билет успешно отменен");
    }
}
