package ru.abarigena.NauJava.Service.TicketHistoryServise;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.abarigena.NauJava.Entities.Ticket.Ticket;
import ru.abarigena.NauJava.Entities.Ticket.TicketHistory;
import ru.abarigena.NauJava.Entities.Ticket.TicketStatus;
import ru.abarigena.NauJava.Repository.TicketHistoryRepository;
import ru.abarigena.NauJava.Repository.TicketRepository;
import ru.abarigena.NauJava.Service.TicketService.TicketServiceImpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class TicketHistoryServiceImpl implements TicketHistoryService {
    private static final Logger logger = LoggerFactory.getLogger(TicketServiceImpl.class);

    private final TicketHistoryRepository ticketHistoryRepository;
    private final TicketRepository ticketRepository;

    @Autowired
    public TicketHistoryServiceImpl(TicketHistoryRepository ticketHistoryRepository, TicketRepository ticketRepository) {
        this.ticketHistoryRepository = ticketHistoryRepository;
        this.ticketRepository = ticketRepository;
    }

    /**
     * Добавляет запись о билете в историю.
     *
     * @param ticket билет, который нужно добавить в историю
     * @param status статус билета
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

    /**
     * Получает список отмененных или завершенных билетов для пользователя.
     *
     * @param userId ID пользователя
     * @return список билетов из истории
     */
    @Override
    public List<TicketHistory> getCanceledAndExpiredTicketsForUser(Long userId) {
        return ticketHistoryRepository.findCanceledAndExpiredTicketsForUser(userId);
    }

    /**
     * Периодически обновляет статус истекших билетов как "CONDUCTED" и удаляет их из репозитория билетов.
     */
    @Scheduled(fixedRate = 60000)
    @Override
    public void updateExpiredTicketsStatus() {
        logger.info("Запуск обновления статусов для истекших билетов.");

        CompletableFuture.runAsync(() -> {
            List<TicketHistory> ticketsToUpdate = ticketHistoryRepository.findTicketsWithExpiredSchedule();
            logger.info("Найдено {} истекших билетов для обновления статуса.", ticketsToUpdate.size());
            for (TicketHistory ticket : ticketsToUpdate) {
                addTicketHistoryStatus(ticket.getId(), TicketStatus.CONDUCTED);
                ticketRepository.deleteById(ticket.getId());
                logger.info("Билет с ID: {} обновлен на статус CONDUCTED и удален из репозитория.", ticket.getId());
            }
        }).exceptionally(ex -> {
            logger.error("Ошибка при обновлении статусов истекших билетов: {}", ex.getMessage());
            return null;
        });
    }
}
