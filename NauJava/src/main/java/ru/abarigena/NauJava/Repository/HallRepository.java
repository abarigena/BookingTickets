package ru.abarigena.NauJava.Repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.abarigena.NauJava.Entities.Hall;

import java.util.Optional;

@RepositoryRestResource
public interface HallRepository extends CrudRepository<Hall, Long> {

    /**
     * Удаляет зал по его названию.
     *
     * @param hallName название зала, который необходимо удалить
     */
    void deleteByName(String hallName);

}
