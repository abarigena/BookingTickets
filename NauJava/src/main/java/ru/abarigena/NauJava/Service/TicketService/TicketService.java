package ru.abarigena.NauJava.Service.TicketService;

import ru.abarigena.NauJava.Entities.HallShedule;
import ru.abarigena.NauJava.Entities.Ticket.Ticket;

import java.util.List;
import java.util.Map;

public interface TicketService {
    Ticket addTicket(Ticket ticket);

    Ticket cancelBookTicket(Long ticketId);

    List<Ticket> findByHallShedule(HallShedule hallShedule);

    void confirmSeats(HallShedule shedule, List<Ticket> tickets, String email);

    Map<Integer, List<Integer>> getBookedSeats(HallShedule schedule);

    List<Ticket> getActiveTicketsByUserId(Long userId);

    List<Ticket> getActiveTicketsByIdentifier(String identifier);
}
