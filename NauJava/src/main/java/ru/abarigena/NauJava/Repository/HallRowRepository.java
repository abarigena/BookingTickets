package ru.abarigena.NauJava.Repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.abarigena.NauJava.Entities.Hall;
import ru.abarigena.NauJava.Entities.HallRow;

import java.util.List;

/**
 * Репозиторий для управления объектами {@link HallRow}.
 */
@RepositoryRestResource
public interface HallRowRepository extends CrudRepository<HallRow, Long> {

    /**
     * Находит все ряды в зале по его названию.
     *
     * @param hallName название зала, для которого нужно найти ряды
     * @return список рядов, относящихся к указанному залу
     */
    @Query("SELECT hr FROM HallRow hr WHERE hr.hall.name = :hallName")
    List<HallRow> findHallRowsByHallName(@Param("hallName") String hallName);

    /**
     * Находит ряд по его номеру и залу.
     *
     * @param row номер ряда
     * @param hall объект зала, к которому принадлежит ряд
     * @return объект {@link HallRow}, соответствующий указанному ряду и залу, или null, если такой ряд не найден
     */
    HallRow findByRowAndHall(long row, Hall hall);

    /**
     * Удаляет все ряды, связанные с указанным залом.
     *
     * @param hall объект зала
     */
    @Modifying
    @Query("DELETE FROM HallRow hr WHERE hr.hall = :hall")
    void deleteByHall(@Param("hall") Hall hall);

    /**
     * Находит ряды в зале по ID зала.
     *
     * @param hallId ID зала
     * @return список рядов
     */
    @Query("SELECT hr FROM HallRow hr WHERE hr.hall.id = :hallId")
    List<HallRow> findHallRowsByHallId(@Param("hallId") Long hallId);

    /**
     * Находит максимальный номер ряда в зале.
     *
     * @param hall объект зала
     * @return максимальный номер ряда
     */
    @Query("SELECT MAX(h.row) FROM HallRow h WHERE h.hall = :hall")
    Integer findMaxRowByHall(Hall hall);

    /**
     * Находит все ряды в зале, отсортированные по номеру.
     *
     * @param hall объект зала
     * @return список рядов
     */
    List<HallRow> findByHallOrderByRow(Hall hall);
}
