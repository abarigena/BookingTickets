package ru.abarigena.NauJava.Repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.abarigena.NauJava.Entities.Ticket.TicketHistory;
import ru.abarigena.NauJava.Entities.Ticket.TicketStatus;
import ru.abarigena.NauJava.Entities.User.User;

import java.time.LocalDateTime;
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

    /**
     * Находит все забронированные билеты в указанном диапазоне дат.
     *
     * @param startDate начальная дата диапазона
     * @param endDate конечная дата диапазона
     * @return список забронированных билетов
     */
    @Query("SELECT t FROM TicketHistory t " +
            "WHERE (t.status = 'BOOKED' OR t.status = 'CONDUCTED') " +
            "AND t.date BETWEEN :startDate AND :endDate")
    List<TicketHistory> findBookedTickets(@Param("startDate") LocalDateTime startDate,
                                          @Param("endDate") LocalDateTime endDate);


    /**
     * Находит все отмененные билеты в указанном диапазоне дат.
     *
     * @param startDate начальная дата диапазона
     * @param endDate конечная дата диапазона
     * @return список отмененных билетов
     */
    @Query("SELECT t FROM TicketHistory t " +
            "WHERE t.status = 'CANCELED' " +
            "AND t.date BETWEEN :startDate AND :endDate")
    List<TicketHistory> findCanceledTickets(@Param("startDate") LocalDateTime startDate,
                                            @Param("endDate") LocalDateTime endDate);


    /**
     * Находит статистику по фильмам на основе количества забронированных или проведенных билетов
     * в заданном диапазоне дат, сгруппированную по названию фильма.
     *
     * @param startDate начальная дата диапазона
     * @param endDate конечная дата диапазона
     * @return список статистики по фильмам, где каждый элемент содержит название фильма
     *         и количество забронированных или проведенных билетов для этого фильма
     */
    @Query("SELECT t.hallShedule.film.title, COUNT(t) AS ticketCount " +
            "FROM TicketHistory t " +
            "WHERE (t.status = 'BOOKED' OR t.status = 'CONDUCTED') " +
            "AND t.date BETWEEN :startDate AND :endDate " +
            "GROUP BY t.hallShedule.film.title " +
            "ORDER BY ticketCount DESC")
    List<Object[]> findFilmStatisticsByDate(@Param("startDate") LocalDateTime startDate,
                                            @Param("endDate") LocalDateTime endDate);


}
