package ru.abarigena.NauJava.Repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.abarigena.NauJava.Entities.Ticket.Ticket;

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
}
