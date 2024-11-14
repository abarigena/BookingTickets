package ru.abarigena.NauJava.Repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.abarigena.NauJava.Entities.Ticket.TicketHistory;

@RepositoryRestResource
public interface TicketHistoryRepository extends CrudRepository<TicketHistory, Long> {
}
