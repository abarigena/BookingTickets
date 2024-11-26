package ru.abarigena.NauJava.Repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.abarigena.NauJava.Entities.Film;
import ru.abarigena.NauJava.Entities.Hall;
import ru.abarigena.NauJava.Entities.HallShedule;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RepositoryRestResource
public interface HallSheduleRepository extends CrudRepository<HallShedule, Long> {

    /**
     * Находит расписания, отсортированные по дню, фильму и залу.
     *
     * @param startDate начальная дата
     * @return список расписаний
     */
    @Query("select hs from HallShedule hs " +
        "join fetch hs.film f "+
        "join fetch hs.hall h " +
        "where hs.startTime >= :startDate and h.active = true  "+
        "order by DATE(hs.startTime), f.title, h.name")
    List<HallShedule> findShedulesGroupedByDayFilmAndHall(@Param("startDate") LocalDateTime startDate);

    /**
     * Находит расписания по фильму и залу.
     *
     * @param film объект фильма
     * @param hall объект зала
     * @return список расписаний
     */
    @Query("SELECT s FROM HallShedule s WHERE s.film = :film AND s.hall = :hall")
    List<HallShedule> findByFilmAndHall(@Param("film") Film film, @Param("hall") Hall hall);

    /**
     * Находит уникальные даты расписаний.
     *
     * @return список уникальных дат
     */
    @Query(value = "SELECT DISTINCT DATE(start_time) AS date FROM hall_shedules order by date(start_time)", nativeQuery = true)
    List<java.sql.Date> findDistinctDates();


}
