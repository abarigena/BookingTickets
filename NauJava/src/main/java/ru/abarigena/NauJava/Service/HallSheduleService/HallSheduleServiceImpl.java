package ru.abarigena.NauJava.Service.HallSheduleService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.abarigena.NauJava.Entities.Film;
import ru.abarigena.NauJava.Entities.Hall;
import ru.abarigena.NauJava.Entities.HallShedule;
import ru.abarigena.NauJava.Repository.HallSheduleRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class HallSheduleServiceImpl implements HallSheduleService {

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
     * Возвращает список всех расписаний.
     *
     * @return список объектов {@link HallShedule}
     */
    @Override
    public List<HallShedule> findAllHallShedules() {
        return (List<HallShedule>) hallSheduleRepository.findAll();
    }

    /**
     * Находит расписания по указанной дате.
     *
     * @param date дата
     * @return список расписаний
     */
    @Override
    public List<HallShedule> findAllHallShedulesByDate(LocalDateTime date) {
        return List.of();
    }

    /**
     * Находит расписания по фильму.
     *
     * @param film объект {@link Film}
     * @return список расписаний
     */
    @Override
    public List<HallShedule> findAllHallShedulesByFilm(Film film) {
        return List.of();
    }

    /**
     * Находит расписания по залу.
     *
     * @param hall объект {@link Hall}
     * @return список расписаний
     */
    @Override
    public List<HallShedule> findAllHallShedulesByHall(Hall hall) {
        return List.of();
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
        HallShedule hallShedule = new HallShedule();

        hallShedule.setStartTime(date);
        hallShedule.setFilm(film);
        hallShedule.setHall(hall);

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
        HallShedule hallShedule = hallSheduleRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Hall not found"));

        hallShedule.setStartTime(date);
        hallShedule.setFilm(film);
        hallShedule.setHall(hall);

        return hallSheduleRepository.save(hallShedule);
    }

    /**
     * Удаляет расписание по его ID.
     *
     * @param id ID расписания
     */
    @Override
    public void deleteHallShedule(Long id) {
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
     * Возвращает расписания с временем начала, сгруппированным по ID.
     *
     * @return карта расписаний
     */
    @Override
    public Map<Long, List<LocalTime>> getSchedules() {
        List<HallShedule> shedules = (List<HallShedule>) hallSheduleRepository.findAll();

        return shedules.stream()
                .collect(Collectors.groupingBy(
                        HallShedule::getId, // Группируем по id
                        Collectors.mapping(
                                shedule -> shedule.getStartTime().toLocalTime(), // Извлекаем LocalTime из startTime
                                Collectors.toList() // Собираем в список
                        )
                ));
    }

    /**
     * @return
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

}
