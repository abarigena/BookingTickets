package ru.abarigena.NauJava.Repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.abarigena.NauJava.Entities.HallShedule;
import ru.abarigena.NauJava.Entities.Ticket.Ticket;

import java.time.LocalDateTime;
import java.util.List;

@RepositoryRestResource
public interface TicketRepository extends CrudRepository<Ticket, Long> {

    /**
     * Находит все билеты, принадлежащие пользователю по его номеру телефона.
     *
     * @param phoneNumber номер телефона пользователя, чьи билеты нужно найти
     * @return список билетов, принадлежащих пользователю с указанным номером телефона
     */
    @Query("select t from Ticket t where t.user.phoneNumber = :phoneNumber ")
    List<Ticket> findByUserPhone(String phoneNumber);

    /**
     * Находит билеты для заданного расписания зала.
     *
     * @param schedule расписание зала
     * @return список билетов, соответствующих указанному расписанию
     */
    @Query("SELECT t FROM Ticket t WHERE t.hallShedule = :schedule")
    List<Ticket> findByHallShedule(@Param("schedule") HallShedule schedule);

    /**
     * Находит активные билеты пользователя по его идентификатору.
     *
     * @param userId идентификатор пользователя
     * @return список активных билетов пользователя
     */
    @Query("SELECT t FROM Ticket t " +
            "JOIN t.hallShedule hs " +
            "WHERE t.user.id = :userId AND hs.startTime > CURRENT_TIMESTAMP")
    List<Ticket> findActiveTicketsByUserId(@Param("userId") Long userId);
}
