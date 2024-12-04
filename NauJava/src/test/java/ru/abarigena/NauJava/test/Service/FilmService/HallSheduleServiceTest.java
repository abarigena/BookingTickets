package ru.abarigena.NauJava.test.Service.FilmService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.abarigena.NauJava.Entities.Film;
import ru.abarigena.NauJava.Entities.Hall;
import ru.abarigena.NauJava.Entities.HallShedule.GroupedSchedule;
import ru.abarigena.NauJava.Entities.HallShedule.HallShedule;
import ru.abarigena.NauJava.Repository.HallSheduleRepository;
import ru.abarigena.NauJava.Service.HallSheduleService.HallSheduleServiceImpl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HallSheduleServiceTest {
    @Mock
    private HallSheduleRepository hallSheduleRepository;

    @InjectMocks
    private HallSheduleServiceImpl hallSheduleService;

    private Hall hall;
    private Film film;
    private HallShedule hallShedule;

    @BeforeEach
    void setUp() {

        hall = new Hall();
        hall.setId(1L);
        hall.setName("Зал");

        film = new Film();
        film.setId(1L);
        film.setTitle("Название");

        hallShedule = new HallShedule();
        hallShedule.setId(1L);
        hallShedule.setStartTime(LocalDateTime.of(2024, 12, 5, 18, 30));
        hallShedule.setFilm(film);
        hallShedule.setHall(hall);
    }

    @Test
    void testFindHallSheduleById_Success() {
        when(hallSheduleRepository.findById(1L)).thenReturn(Optional.of(hallShedule));

        HallShedule result = hallSheduleService.findHallSheduleById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void testFindHallSheduleById_NotFound() {
        when(hallSheduleRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> hallSheduleService.findHallSheduleById(1L));
    }

    @Test
    void testCreateHallShedule() {
        LocalDateTime startTime = LocalDateTime.of(2024, 12, 5, 18, 30);
        when(hallSheduleRepository.save(any(HallShedule.class))).thenReturn(hallShedule);

        HallShedule result = hallSheduleService.createHallShedule(startTime, film, hall);

        assertNotNull(result);
        assertEquals(startTime, result.getStartTime());
        assertEquals(film, result.getFilm());
        assertEquals(hall, result.getHall());
    }

    @Test
    void testUpdateHallShedule() {
        LocalDateTime newStartTime = LocalDateTime.of(2024, 12, 6, 19, 30);
        when(hallSheduleRepository.findById(1L)).thenReturn(Optional.of(hallShedule));
        when(hallSheduleRepository.save(any(HallShedule.class))).thenReturn(hallShedule);

        HallShedule result = hallSheduleService.updateHallShedule(1L, newStartTime, film, hall);

        assertNotNull(result);
        assertEquals(newStartTime, result.getStartTime());
    }

    @Test
    void testDeleteHallShedule() {
        doNothing().when(hallSheduleRepository).deleteById(1L);

        hallSheduleService.deleteHallShedule(1L);

        verify(hallSheduleRepository, times(1)).deleteById(1L);
    }

    @Test
    void testFindUniqueDays() {
        when(hallSheduleRepository.findDistinctDates()).thenReturn(List.of(java.sql.Date.valueOf("2024-12-05")));

        var result = hallSheduleService.findUniqueDays();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(LocalDate.of(2024, 12, 5), result.get(0));
        verify(hallSheduleRepository, times(1)).findDistinctDates();
    }

    @Test
    void testGetGroupedSchedules() {
        when(hallSheduleRepository.findAll()).thenReturn(List.of(hallShedule));

        Map<LocalDate, Map<Film, Map<Hall, List<HallShedule>>>> result = hallSheduleService.getGroupedSchedules();

        assertNotNull(result);
        assertEquals(1, result.size()); // Одна дата
        assertTrue(result.containsKey(hallShedule.getStartTime().toLocalDate()));

        Map<Film, Map<Hall, List<HallShedule>>> filmMap = result.get(hallShedule.getStartTime().toLocalDate());
        assertEquals(1, filmMap.size()); // Один фильм

        Map<Hall, List<HallShedule>> hallMap = filmMap.get(hallShedule.getFilm());
        assertEquals(1, hallMap.size()); // Один зал

        List<HallShedule> schedules = hallMap.get(hallShedule.getHall());
        assertEquals(1, schedules.size()); // Один сеанс
        assertEquals(hallShedule, schedules.get(0));
    }

    @Test
    void testGetUpcomingSchedules() {
        LocalDateTime today = LocalDateTime.now();
        LocalDateTime weekAhead = today.plusDays(7);

        HallShedule upcomingSchedule = new HallShedule();
        upcomingSchedule.setId(2L);
        upcomingSchedule.setStartTime(today.plusDays(2)); // В пределах недели
        upcomingSchedule.setFilm(film);
        upcomingSchedule.setHall(hall);

        when(hallSheduleRepository.findShedulesGroupedByDayFilmAndHall(any(LocalDateTime.class)))
                .thenReturn(List.of(hallShedule, upcomingSchedule));


        Map<LocalDate, Map<Film, Map<Hall, List<HallShedule>>>> result = hallSheduleService.getUpcomingSchedules();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.containsKey(hallShedule.getStartTime().toLocalDate()));
        assertTrue(result.containsKey(upcomingSchedule.getStartTime().toLocalDate()));
    }

    @Test
    void testGetSchedulesForFilm() {
        when(hallSheduleRepository.findByFilm(any(LocalDateTime.class), eq(film.getTitle())))
                .thenReturn(List.of(hallShedule));

        Map<LocalDate, Map<Hall, List<HallShedule>>> result = hallSheduleService.getSchedulesForFilm(film);

        assertNotNull(result);
        assertEquals(1, result.size()); // Одна дата
        assertTrue(result.containsKey(hallShedule.getStartTime().toLocalDate()));

        Map<Hall, List<HallShedule>> hallMap = result.get(hallShedule.getStartTime().toLocalDate());
        assertEquals(1, hallMap.size()); // Один зал

        List<HallShedule> schedules = hallMap.get(hallShedule.getHall());
        assertEquals(1, schedules.size()); // Один сеанс
        assertEquals(hallShedule, schedules.get(0));
    }

    @Test
    void testGetGroupedSchedulesRest() {
        when(hallSheduleRepository.findAll()).thenReturn(List.of(hallShedule));

        List<GroupedSchedule> result = hallSheduleService.getGroupedSchedulesRest();

        assertNotNull(result);
        assertEquals(1, result.size());

        GroupedSchedule groupedSchedule = result.get(0);
        assertEquals(hallShedule.getStartTime().toLocalDate(), groupedSchedule.getDate());
        assertEquals(hallShedule.getFilm(), groupedSchedule.getFilm());
        assertEquals(hallShedule.getHall(), groupedSchedule.getHall());
        assertEquals(List.of(hallShedule), groupedSchedule.getSchedules());
    }



}
