package ru.abarigena.NauJava.test.Controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import ru.abarigena.NauJava.Controller.FilmController.FilmControllerRest;
import ru.abarigena.NauJava.Entities.Film;
import ru.abarigena.NauJava.Service.FilmService.FilmService;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.springframework.http.MediaType.APPLICATION_JSON;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(FilmControllerRest.class)
@AutoConfigureMockMvc(addFilters = false)
class FilmControllerRestTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FilmService filmService;

    private Film film;

    @BeforeEach
    void setUp() {
        film = new Film();
        film.setId(1L);
        film.setTitle("Test Movie");
        film.setMinAge(12);
        film.setDuration(120);
        film.setDescription("Description of Test Movie");
        film.setImageUrl("http://example.com/image.jpg");
    }

    @Test
    void testGetAllFilms() throws Exception {
        List<Film> films = List.of(film);

        when(filmService.findAllFilms()).thenReturn(films);

        mockMvc.perform(get("/api/films/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Test"))
                .andExpect(jsonPath("$[0].minAge").value(12));
    }

    @Test
    void testGetFilm() throws Exception {
        when(filmService.findFimById(1L)).thenReturn(film);

        mockMvc.perform(get("/api/films/find/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test"))
                .andExpect(jsonPath("$.description").value("Description"));
    }

    @Test
    void testCreateFilm() throws Exception {
        when(filmService.createFilm(anyString(), anyInt(), anyInt(), anyString(), anyString()))
                .thenReturn(film);

        mockMvc.perform(post("/api/films/create")
                        .contentType(APPLICATION_JSON)
                        .content("{\"title\":\"Test Movie\", \"minAge\":12, \"duration\":120, \"description\":\"Description of Test Movie\", \"imageUrl\":\"http://example.com/image.jpg\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Test Movie"))
                .andExpect(jsonPath("$.description").value("Description of Test Movie"));
    }

    @Test
    void testUpdateFilm() throws Exception {
        doReturn(film)
                .when(filmService).updateFilm(eq(1L), anyString(), anyInt(), anyInt(), anyString(), anyString());

        mockMvc.perform(put("/api/films/update/{id}", 1L)
                        .contentType(APPLICATION_JSON)
                        .content("{\"title\":\"Updated Test Movie\", \"minAge\":13, \"duration\":130, \"description\":\"Updated Description\", \"imageUrl\":\"http://example.com/updated_image.jpg\"}"))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteFilm() throws Exception {
        doNothing().when(filmService).deleteFilm(1L);

        mockMvc.perform(delete("/api/films/delete/{id}", 1L))
                .andExpect(status().isNoContent());
    }

    @Test
    void testCreateFilm_Error() throws Exception {
        when(filmService.createFilm(anyString(), anyInt(), anyInt(), anyString(), anyString()))
                .thenThrow(new RuntimeException("Error creating film"));

        mockMvc.perform(post("/api/films/create")
                        .contentType(APPLICATION_JSON)
                        .content("{\"title\":\"Test Movie\", \"minAge\":12, \"duration\":120, \"description\":\"Description of Test Movie\", \"imageUrl\":\"http://example.com/image.jpg\"}"))
                .andExpect(status().isInternalServerError());
    }

}
