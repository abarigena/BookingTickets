package ru.abarigena.NauJava.Repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.abarigena.NauJava.Entities.HallShedule;

import java.time.LocalDateTime;
import java.util.List;

@RepositoryRestResource
public interface HallSheduleRepository extends CrudRepository<HallShedule, Long> {

    @Query("select hs from HallShedule hs " +
        "join fetch hs.film f "+
        "join fetch hs.hall h " +
        "where hs.startTime >= :startDate "+
        "order by DATE(hs.startTime), f.title, h.name")
    List<HallShedule> findShedulesGroupedByDayFilmAndHall(@Param("startDate") LocalDateTime startDate);
}
