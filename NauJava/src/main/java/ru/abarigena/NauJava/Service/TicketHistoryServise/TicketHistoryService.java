package ru.abarigena.NauJava.Service.TicketHistoryServise;

import ru.abarigena.NauJava.Entities.Ticket.Ticket;
import ru.abarigena.NauJava.Entities.Ticket.TicketHistory;
import ru.abarigena.NauJava.Entities.Ticket.TicketStatus;

import java.util.List;

/**
 * Сервис для работы с историей билетов.
 */
public interface TicketHistoryService {
    /**
     * Добавляет запись в историю билета с новым статусом.
     *
     * @param ticket билет, для которого добавляется запись
     * @param status новый статус билета
     */
    void addTicketHistory(Ticket ticket, TicketStatus status);

    /**
     * Обновляет статус билета по его идентификатору.
     *
     * @param ticketId идентификатор билета
     * @param status новый статус билета
     */
    void addTicketHistoryStatus(Long ticketId, TicketStatus status);

    /**
     * Получает все отмененные и истекшие билеты для конкретного пользователя.
     *
     * @param userId идентификатор пользователя
     * @return список отмененных и истекших билетов для пользователя
     */
    List<TicketHistory> getCanceledAndExpiredTicketsForUser(Long userId);

    /**
     * Обновляет статус истекших билетов.
     */
    void updateExpiredTicketsStatus();
}
