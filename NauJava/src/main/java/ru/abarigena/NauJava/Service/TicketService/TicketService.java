package ru.abarigena.NauJava.Service.TicketService;

import ru.abarigena.NauJava.Entities.Ticket.Ticket;

public interface TicketService {
    Ticket addTicket(Ticket ticket);

    Ticket deleteTicket(Long ticketId);
}
