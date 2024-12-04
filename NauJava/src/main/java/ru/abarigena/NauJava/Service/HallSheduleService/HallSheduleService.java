package ru.abarigena.NauJava.Service.HallSheduleService;

import ru.abarigena.NauJava.Entities.Film;
import ru.abarigena.NauJava.Entities.Hall;
import ru.abarigena.NauJava.Entities.HallShedule.GroupedSchedule;
import ru.abarigena.NauJava.Entities.HallShedule.HallShedule;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Сервис для работы с расписаниями залов.
 */
public interface HallSheduleService {
    /**
     * Находит расписание по его идентификатору.
     *
     * @param id идентификатор расписания
     * @return найденное расписание
     */
    HallShedule findHallSheduleById(Long id);

    /**
     * Создает новое расписание для фильма в зале на указанную дату.
     *
     * @param date дата начала сеанса
     * @param film фильм, для которого создается расписание
     * @param hall зал, в котором будет проходить сеанс
     * @return созданное расписание
     */
    HallShedule createHallShedule(LocalDateTime date, Film film, Hall hall);

    /**
     * Обновляет расписание по его идентификатору.
     *
     * @param id идентификатор расписания
     * @param date новая дата начала сеанса
     * @param film новый фильм для расписания
     * @param hall новый зал для расписания
     * @return обновленное расписание
     */
    HallShedule updateHallShedule(Long id, LocalDateTime date, Film film, Hall hall);

    /**
     * Удаляет расписание по его идентификатору.
     *
     * @param id идентификатор расписания
     */
    void deleteHallShedule(Long id);

    /**
     * Получает уникальные дни для всех расписаний.
     *
     * @return список уникальных дней
     */
    List<LocalDate> findUniqueDays();

    /**
     * Получает расписания, сгруппированные по датам, фильмам и залам.
     *
     * @return расписания, сгруппированные по датам, фильмам и залам
     */
    Map<LocalDate, Map<Film, Map<Hall, List<HallShedule>>>> getGroupedSchedules();

    /**
     * Получает расписания, сгруппированные по датам, фильмам и залам, для предстоящих сеансов.
     *
     * @return расписания для предстоящих сеансов
     */
    Map<LocalDate, Map<Film, Map<Hall, List<HallShedule>>>> getUpcomingSchedules();

    /**
     * Получает расписания для конкретного фильма, сгруппированные по датам и залам.
     *
     * @param film фильм, для которого нужно получить расписания
     * @return расписания для фильма, сгруппированные по датам и залам
     */
    Map<LocalDate, Map<Hall, List<HallShedule>>> getSchedulesForFilm(Film film);

    /**
     * Группирует данные в список объектов {@link GroupedSchedule}.
     * @return Список объектов {@link GroupedSchedule}
     */
    List<GroupedSchedule> getGroupedSchedulesRest();
}
