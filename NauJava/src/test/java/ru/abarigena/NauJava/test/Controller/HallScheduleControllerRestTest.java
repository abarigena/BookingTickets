package ru.abarigena.NauJava.test.Controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.abarigena.NauJava.Controller.HallSheduleController.HallSheduleControllerRest;
import ru.abarigena.NauJava.Entities.Film;
import ru.abarigena.NauJava.Entities.Hall;
import ru.abarigena.NauJava.Entities.HallShedule.GroupedSchedule;
import ru.abarigena.NauJava.Entities.HallShedule.HallShedule;
import ru.abarigena.NauJava.Service.FilmService.FilmService;
import ru.abarigena.NauJava.Service.HallService.HallService;
import ru.abarigena.NauJava.Service.HallSheduleService.HallSheduleService;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(HallSheduleControllerRest.class)
@AutoConfigureMockMvc(addFilters = false)
class HallScheduleControllerRestTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private HallSheduleService hallSheduleService;

    @MockBean
    private FilmService filmService;

    @MockBean
    private HallService hallService;

    private Film film;
    private Hall hall;

    @BeforeEach
    void setUp() {
        hall = new Hall();
        hall.setId(1L);
        hall.setName("Main Hall");
        hall.setActive(true);

        film = new Film();
        film.setId(1L);
        film.setTitle("Sample Film");
    }

    @Test
    void testCreateHallShedule() throws Exception {
        List<String> dates = List.of("2024-12-05");
        List<String> times = List.of("10:00", "12:00");

        when(filmService.findFimById(1L)).thenReturn(film);
        when(hallService.findHallById(1L)).thenReturn(hall);

        mockMvc.perform(post("/api/hallShedules/create")
                        .param("filmId", "1")
                        .param("hallId", "1")
                        .param("dates", String.join(",", dates))
                        .param("times", String.join(",", times)))
                .andExpect(status().isOk());
    }

    @Test
    void testEditHallShedule() throws Exception {
        Long scheduleId = 1L;
        String newDate = "2024-12-05";
        String newTime = "14:00";
        Long filmId = 1L;
        Long hallId = 1L;

        when(filmService.findFimById(filmId)).thenReturn(film);
        when(hallService.findHallById(hallId)).thenReturn(hall);

        mockMvc.perform(put("/api/hallShedules/update/{id}", scheduleId)
                        .param("date", newDate)
                        .param("time", newTime)
                        .param("filmId", String.valueOf(filmId))
                        .param("hallId", String.valueOf(hallId)))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteHallShedule() throws Exception {
        Long scheduleId = 1L;

        doNothing().when(hallSheduleService).deleteHallShedule(scheduleId);

        mockMvc.perform(delete("/api/hallShedules/delete/{id}", scheduleId))
                .andExpect(status().isNoContent());

        verify(hallSheduleService, times(1)).deleteHallShedule(scheduleId);
    }

    @Test
    void testGetHallSchedules() throws Exception {
        LocalDate day = LocalDate.of(2024, 12, 05);
        Film film = new Film();
        film.setId(1L);
        film.setTitle("Sample Film");

        Hall hall = new Hall();
        hall.setId(1L);
        hall.setName("Main Hall");
        hall.setActive(true);

        List<HallShedule> schedules = List.of();

        List<GroupedSchedule> groupedSchedules = List.of(new GroupedSchedule(day, film, hall, schedules));

        when(hallSheduleService.getGroupedSchedulesRest()).thenReturn(groupedSchedules);

        mockMvc.perform(get("/api/hallShedules/all")
                        .param("day", day.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].date").value(day.toString()))
                .andExpect(jsonPath("$[0].film.title").value("Sample Film"))
                .andExpect(jsonPath("$[0].hall.name").value("Main Hall"));
    }

}
