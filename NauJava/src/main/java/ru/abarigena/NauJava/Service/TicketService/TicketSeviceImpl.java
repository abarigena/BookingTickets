package ru.abarigena.NauJava.Service.TicketService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.abarigena.NauJava.Entities.Ticket.Ticket;
import ru.abarigena.NauJava.Entities.Ticket.TicketStatus;
import ru.abarigena.NauJava.Repository.TicketRepository;
import ru.abarigena.NauJava.Service.TicketHistoryServise.TicketHistoryService;

@Service
public class TicketSeviceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    private final TicketHistoryService ticketHistoryService;

    @Autowired
    public TicketSeviceImpl(TicketRepository ticketRepository, TicketHistoryService ticketHistoryService) {
        this.ticketRepository = ticketRepository;
        this.ticketHistoryService = ticketHistoryService;
    }

    /**
     * Добавляет новый билет.
     *
     * @param ticket объект {@link Ticket}
     * @return сохраненный объект {@link Ticket}
     */
    @Override
    public Ticket addTicket(Ticket ticket) {
        Ticket saveTicket = ticketRepository.save(ticket);

        ticketHistoryService.addTicketHistory(saveTicket, TicketStatus.BOOKED);
        return saveTicket;
    }

    /**
     * Удаляет билет и обновляет его статус в истории.
     *
     * @param ticketId ID билета
     * @return удаленный объект {@link Ticket}
     */
    @Override
    public Ticket deleteTicket(Long ticketId) {
        ticketHistoryService.addTicketHistoryStatus(ticketId, TicketStatus.CANCELED);
        Ticket ticket = ticketRepository.findById(ticketId).get();
        ticketRepository.delete(ticket);
        return ticket;
    }
}
