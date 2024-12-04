package ru.abarigena.NauJava.test.Controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.abarigena.NauJava.Controller.FilmController.FilmControllerUserRest;
import ru.abarigena.NauJava.Entities.Film;
import ru.abarigena.NauJava.Entities.Hall;
import ru.abarigena.NauJava.Entities.HallShedule.HallShedule;
import ru.abarigena.NauJava.Service.FilmService.FilmService;
import ru.abarigena.NauJava.Service.HallSheduleService.HallSheduleService;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FilmControllerUserRest.class)
@AutoConfigureMockMvc(addFilters = false)
public class FilmControllerUserRestTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FilmService filmService;

    @MockBean
    private HallSheduleService hallSheduleService;

    private Film film;
    private Hall hall;
    private HallShedule hallShedule;

    @BeforeEach
    void setUp() {
        film = new Film();
        film.setId(1L);
        film.setTitle("Test Movie");

        hall = new Hall();
        hall.setId(1L);
        hall.setName("Main Hall");

        hallShedule = new HallShedule();
        hallShedule.setId(1L);
        hallShedule.setHall(hall);
        hallShedule.setStartTime(LocalDate.now().atTime(18, 30));
    }

    @Test
    void testViewFilmWithSchedules() throws Exception {
        when(filmService.findFimById(1L)).thenReturn(film);

        when(hallSheduleService.getSchedulesForFilm(film)).thenReturn(Map.of(
                LocalDate.now(), Map.of(hall, List.of(hallShedule))
        ));

        mockMvc.perform(get("/api/filmsUser/{id}", 1L)
                        .param("day", LocalDate.now().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.film.title").value("Test Movie"))
                .andExpect(jsonPath("$.uniqueDays[0]").value(LocalDate.now().toString()));

    }

    @Test
    void testViewFilmWithoutSchedules() throws Exception {
        when(filmService.findFimById(1L)).thenReturn(film);

        when(hallSheduleService.getSchedulesForFilm(film)).thenReturn(Map.of());

        mockMvc.perform(get("/api/filmsUser/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.film.title").value("Test Movie"))
                .andExpect(jsonPath("$.uniqueDays[0]").value(LocalDate.now().toString()))
                .andExpect(jsonPath("$.filteredSchedules").isEmpty());
    }
}
