package ru.abarigena.NauJava.test.Service.FilmService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import ru.abarigena.NauJava.Entities.Film;
import ru.abarigena.NauJava.Repository.FilmRepository;
import ru.abarigena.NauJava.Service.FilmService.FilmServiceImpl;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class FilmServiceTest {

    @Mock
    private FilmRepository filmRepository;

    @InjectMocks
    private FilmServiceImpl filmService;

    @Test
    void testFindFilmById_Found() {
        Long filmId = 1L;
        Film film = new Film();
        film.setId(filmId);
        when(filmRepository.findById(filmId)).thenReturn(Optional.of(film));

        Film result = filmService.findFimById(filmId);

        assertNotNull(result);
        assertEquals(filmId, result.getId());
    }

    @Test
    void testFindFilmById_NotFound() {
        Long filmId = 1L;
        when(filmRepository.findById(filmId)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> filmService.findFimById(filmId));
    }

    @Test
    void testFindAllFilms() {
        List<Film> films = Arrays.asList(new Film(), new Film());
        when(filmRepository.findAll()).thenReturn(films);

        List<Film> result = filmService.findAllFilms();

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void testCreateFilm() {
        String title = "Название";
        int minAge = 18;
        int duration = 120;
        String description = "Описание";
        String imageURL = "http://image.url";

        Film film = new Film();
        film.setId(1L);
        film.setTitle(title);
        film.setMinAge(minAge);
        film.setDuration(duration);
        film.setDescription(description);
        film.setImageUrl(imageURL);

        when(filmRepository.save(any(Film.class))).thenReturn(film);

        Film createdFilm = filmService.createFilm(title, minAge, duration, description, imageURL);

        assertNotNull(createdFilm);
        assertEquals(title, createdFilm.getTitle());
        assertEquals(minAge, createdFilm.getMinAge());
        assertEquals(duration, createdFilm.getDuration());
    }

    @Test
    void testUpdateFilm() {
        Long filmId = 1L;
        String newTitle = "Обновленный";
        int newMinAge = 16;
        int newDuration = 90;
        String newDescription = "Обновленное описание";
        String newImageURL = "http://updated.image.url";

        Film existingFilm = new Film();
        existingFilm.setId(filmId);
        existingFilm.setTitle("Старое название");

        when(filmRepository.findById(filmId)).thenReturn(Optional.of(existingFilm));
        when(filmRepository.save(any(Film.class))).thenReturn(existingFilm);

        Film updatedFilm = filmService.updateFilm(filmId, newTitle, newMinAge, newDuration, newDescription, newImageURL);

        assertNotNull(updatedFilm);
        assertEquals(newTitle, updatedFilm.getTitle());
        assertEquals(newMinAge, updatedFilm.getMinAge());
        assertEquals(newDuration, updatedFilm.getDuration());
    }

    @Test
    void testDeleteFilm() {
        Long filmId = 1L;

        doNothing().when(filmRepository).deleteById(filmId);

        assertDoesNotThrow(() -> filmService.deleteFilm(filmId));

        verify(filmRepository, times(1)).deleteById(filmId);
    }
}
