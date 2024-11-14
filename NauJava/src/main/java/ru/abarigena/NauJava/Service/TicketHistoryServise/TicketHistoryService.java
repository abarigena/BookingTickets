package ru.abarigena.NauJava.Service.TicketHistoryServise;

import ru.abarigena.NauJava.Entities.Ticket.Ticket;
import ru.abarigena.NauJava.Entities.Ticket.TicketStatus;

public interface TicketHistoryService {
    void addTicketHistory(Ticket ticket, TicketStatus status);

    void addTicketHistoryStatus(Long ticketId, TicketStatus status);
}
