package ru.abarigena.NauJava.Service.HallService;

import ru.abarigena.NauJava.Entities.Hall;

import java.util.List;

/**
 * Сервис для работы с залами.
 */
public interface HallService {

    /**
     * Удаляет зал по его идентификатору.
     *
     * @param id идентификатор зала
     */
    void deleteHall(Long id);

    /**
     * Находит зал по его идентификатору.
     *
     * @param id идентификатор зала
     * @return найденный зал
     */
    Hall findHallById(Long id);

    /**
     * Получает список всех залов.
     *
     * @return список всех залов
     */
    List<Hall> findAllHalls();

    /**
     * Создает новый зал.
     *
     * @param name название зала
     * @param active статус активности зала
     * @return созданный зал
     */
    Hall createHall(String name, boolean active);

    /**
     * Обновляет данные зала.
     *
     * @param id идентификатор зала
     * @param name новое название зала
     * @param active новый статус активности зала
     * @return обновленный зал
     */
    Hall updateHall(Long id, String name, boolean active);
}
