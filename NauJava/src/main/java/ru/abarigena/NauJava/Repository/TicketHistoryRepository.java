package ru.abarigena.NauJava.Repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.abarigena.NauJava.Entities.Ticket.TicketHistory;
import ru.abarigena.NauJava.Entities.User.User;

import java.util.List;

@RepositoryRestResource
public interface TicketHistoryRepository extends CrudRepository<TicketHistory, Long> {
    /**
     * Находит билеты пользователя со статусами "CANCELED" или "CONDUCTED".
     *
     * @param userId идентификатор пользователя
     * @return список отмененных или завершенных билетов
     */
    @Query("SELECT t FROM TicketHistory t " +
            "WHERE t.user.id = :userId AND (t.status = 'CANCELED' OR t.status = 'CONDUCTED')")
    List<TicketHistory> findCanceledAndExpiredTicketsForUser(@Param("userId") Long userId);

    /**
     * Находит билеты с истекшим временем сеанса, кроме тех, которые уже завершены или отменены.
     *
     * @return список билетов с истекшим временем сеанса
     */
    @Query("SELECT t FROM TicketHistory t WHERE t.hallShedule.startTime < CURRENT_TIMESTAMP AND t.status != 'CONDUCTED' " +
            "AND t.status != 'CANCELED'")
    List<TicketHistory> findTicketsWithExpiredSchedule();
}
