package ru.abarigena.NauJava.Repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.abarigena.NauJava.Entities.Report.Report;

/**
 * Репозиторий для управления сущностями {@link Report}.
 * Предоставляет методы для выполнения CRUD-операций с объектами в базе данных.
 */
@RepositoryRestResource
public interface ReportRepository extends CrudRepository<Report, Long> {
}
