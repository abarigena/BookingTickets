package ru.abarigena.NauJava.Service.HallSheduleService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.abarigena.NauJava.Entities.Film;
import ru.abarigena.NauJava.Entities.Hall;
import ru.abarigena.NauJava.Entities.HallShedule;
import ru.abarigena.NauJava.Repository.HallSheduleRepository;
import ru.abarigena.NauJava.Service.TicketService.TicketServiceImpl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class HallSheduleServiceImpl implements HallSheduleService {
    private static final Logger logger = LoggerFactory.getLogger(TicketServiceImpl.class);

    private final HallSheduleRepository hallSheduleRepository;

    @Autowired
    public HallSheduleServiceImpl(HallSheduleRepository hallSheduleRepository) {
        this.hallSheduleRepository = hallSheduleRepository;
    }

    /**
     * Находит расписание по его ID.
     *
     * @param id ID расписания
     * @return объект {@link HallShedule}
     */
    @Override
    public HallShedule findHallSheduleById(Long id) {
        return hallSheduleRepository.findById(id).orElseThrow(NoSuchElementException::new);
    }

    /**
     * Создает новое расписание.
     *
     * @param date дата и время начала
     * @param film объект {@link Film}
     * @param hall объект {@link Hall}
     * @return созданное расписание {@link HallShedule}
     */
    @Override
    public HallShedule createHallShedule(LocalDateTime date, Film film, Hall hall) {
        logger.info("Создание нового расписания для фильма: {} в зале: {} на дату: {}", film.getTitle(), hall.getName(), date);
        HallShedule hallShedule = new HallShedule();

        hallShedule.setStartTime(date);
        hallShedule.setFilm(film);
        hallShedule.setHall(hall);

        logger.info("Расписание успешно создано: {}", hallShedule);
        return hallSheduleRepository.save(hallShedule);
    }

    /**
     * Обновляет расписание.
     *
     * @param id   ID расписания
     * @param date новая дата и время начала
     * @param film объект {@link Film}
     * @param hall объект {@link Hall}
     * @return обновленное расписание {@link HallShedule}
     */
    @Override
    public HallShedule updateHallShedule(Long id, LocalDateTime date, Film film, Hall hall) {
        logger.info("Обновление расписания с ID: {} на новую дату: {}, для фильма: {} в зале: {}", id, date, film.getTitle(), hall.getName());

        HallShedule hallShedule = hallSheduleRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Hall not found"));

        hallShedule.setStartTime(date);
        hallShedule.setFilm(film);
        hallShedule.setHall(hall);

        logger.info("Расписание успешно обновлено: {}", hallShedule);
        return hallSheduleRepository.save(hallShedule);
    }

    /**
     * Удаляет расписание по его ID.
     *
     * @param id ID расписания
     */
    @Override
    public void deleteHallShedule(Long id) {
        logger.info("Удаление расписания с ID: {}", id);
        hallSheduleRepository.deleteById(id);
    }

    /**
     * Находит уникальные даты расписаний.
     *
     * @return список уникальных дат
     */
    @Override
    public List<LocalDate> findUniqueDays() {
        List<java.sql.Date> dates = hallSheduleRepository.findDistinctDates();
        List<LocalDate> localDates = dates.stream()
                .map(date -> date.toLocalDate())
                .collect(Collectors.toList());
        return localDates;
    }

    /**
     * Возвращает расписания, сгруппированные по дате, фильму и залу.
     *
     * @return карта сгруппированных расписаний
     */
    @Override
    public Map<LocalDate, Map<Film, Map<Hall, List<HallShedule>>>> getGroupedSchedules() {
        List<HallShedule> schedules = (List<HallShedule>) hallSheduleRepository.findAll();
        return schedules.stream()
                .collect(Collectors.groupingBy(
                        schedule -> schedule.getStartTime().toLocalDate(),
                        Collectors.groupingBy(
                                HallShedule::getFilm,
                                Collectors.groupingBy(
                                        HallShedule::getHall
                                )
                        )
                ));
    }

    /**
     * Возвращает расписание сеансов на ближайшие 7 дней, сгруппированное по дате, фильму и залу.
     *
     * @return карта расписания, где ключами являются даты, фильмы и залы, а значениями — списки сеансов
     */
    @Override
    public Map<LocalDate, Map<Film, Map<Hall, List<HallShedule>>>> getUpcomingSchedules() {
        LocalDateTime today = LocalDateTime.now();
        LocalDateTime weekAhead = today.plusDays(7);

        List<HallShedule> schedules = hallSheduleRepository.findShedulesGroupedByDayFilmAndHall(today);

        return schedules.stream()
                .filter(schedule -> !schedule.getStartTime().isAfter(weekAhead)) // Ограничение на 7 дней
                .collect(Collectors.groupingBy(
                        schedule -> schedule.getStartTime().toLocalDate(), // Группировка по дате
                        Collectors.groupingBy(
                                HallShedule::getFilm, // Вложенная группировка по фильму
                                Collectors.groupingBy(HallShedule::getHall) // Ещё одна вложенная группировка по залу
                        )
                ));
    }

    /**
     * Получает расписания для конкретного фильма, сгруппированные по датам и залам.
     *
     * @param film фильм, для которого нужно получить расписания
     * @return расписания для фильма, сгруппированные по датам и залам
     */
    @Override
    public Map<LocalDate, Map<Hall, List<HallShedule>>> getSchedulesForFilm(Film film) {

        LocalDateTime today = LocalDateTime.now();
        LocalDateTime weekAhead = today.plusDays(7);

        List<HallShedule> schedules = hallSheduleRepository.findByFilm(today, film.getTitle());

        return schedules.stream()
                .filter(schedule -> schedule.getStartTime().isBefore(weekAhead)) // на неделю вперёд
                .collect(Collectors.groupingBy(
                        schedule -> schedule.getStartTime().toLocalDate(), // Группировка по дате
                        Collectors.groupingBy(HallShedule::getHall)        // Вложенная группировка по залу
                ));
    }

}
