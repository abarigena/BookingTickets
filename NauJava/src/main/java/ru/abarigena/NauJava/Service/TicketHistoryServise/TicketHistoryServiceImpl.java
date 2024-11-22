package ru.abarigena.NauJava.Service.TicketHistoryServise;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.abarigena.NauJava.Entities.Ticket.Ticket;
import ru.abarigena.NauJava.Entities.Ticket.TicketHistory;
import ru.abarigena.NauJava.Entities.Ticket.TicketStatus;
import ru.abarigena.NauJava.Repository.TicketHistoryRepository;

import java.time.LocalDateTime;

@Service
public class TicketHistoryServiceImpl implements TicketHistoryService {
    private final TicketHistoryRepository ticketHistoryRepository;

    @Autowired
    public TicketHistoryServiceImpl(TicketHistoryRepository ticketHistoryRepository) {
        this.ticketHistoryRepository = ticketHistoryRepository;
    }

    /**
     * @param ticket
     * @param status
     */
    @Override
    public void addTicketHistory(Ticket ticket, TicketStatus status) {
        TicketHistory ticketHistory = new TicketHistory();
        ticketHistory.setId(ticket.getId());
        ticketHistory.setStatus(status);
        ticketHistory.setDate(LocalDateTime.now());
        ticketHistory.setRow(ticket.getRow());
        ticketHistory.setSeat(ticket.getSeat());
        ticketHistory.setHallShedule(ticket.getHallShedule());
        ticketHistory.setUser(ticket.getUser());

        ticketHistoryRepository.save(ticketHistory);
    }

    /**
     * Обновляет статус билета в истории.
     *
     * @param ticketId ID билета
     * @param status   новый статус
     */
    @Override
    public void addTicketHistoryStatus(Long ticketId, TicketStatus status) {
        TicketHistory ticketHistory = ticketHistoryRepository.findById(ticketId).orElse(null);
        assert ticketHistory != null;
        ticketHistory.setStatus(status);

        ticketHistoryRepository.save(ticketHistory);
    }
}
