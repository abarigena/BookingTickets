package ru.abarigena.NauJava.Service.HallRowService;

import ru.abarigena.NauJava.Entities.Hall;
import ru.abarigena.NauJava.Entities.HallRow;

import java.util.List;

/**
 * Сервис для работы с рядами в зале.
 */
public interface HallRowService {

    /**
     * Получает список рядов для конкретного зала по его идентификатору.
     *
     * @param hallId идентификатор зала
     * @return список рядов в зале
     */
    List<HallRow> getRowsByHallId(Long hallId);

    /**
     * Создает новый ряд в зале.
     *
     * @param hall зал, к которому будет добавлен ряд
     * @param seatCount количество мест в ряду
     * @return созданный ряд
     */
    HallRow createRow(Hall hall, int seatCount);

    /**
     * Удаляет ряд из зала.
     *
     * @param hallRow ряд, который необходимо удалить
     */
    void deleteRow(HallRow hallRow);

    /**
     * Обновляет количество мест в ряду.
     *
     * @param id идентификатор ряда
     * @param seatCount новое количество мест в ряду
     * @return обновленный ряд
     */
    HallRow updateRow(Long id, int seatCount);

    /**
     * Находит ряд по его идентификатору.
     *
     * @param id идентификатор ряда
     * @return найденный ряд
     */
    HallRow findRowById(Long id);

    /**
     * Находит ряды в конкретном зале.
     *
     * @param hall зал, для которого ищутся ряды
     * @return список рядов в зале
     */
    List<HallRow> findRowByHall(Hall hall);
}
