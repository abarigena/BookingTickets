package ru.abarigena.NauJava.Controller.FilmController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.abarigena.NauJava.Entities.Film;
import ru.abarigena.NauJava.Service.FilmService.FilmService;

import java.util.List;

/**
 * REST-контроллер для управления фильмами.
 */
@RestController
@RequestMapping("/api/films")
public class FilmControllerRest {
    private final FilmService filmService;

    @Autowired
    public FilmControllerRest(FilmService filmService) {
        this.filmService = filmService;
    }

    /**
     * Получает список всех фильмов.
     *
     * @return список фильмов
     */
    @GetMapping("/all")
    public List<Film> getAllFilms() {
        return filmService.findAllFilms();
    }

    /**
     * Находит фильм по его ID.
     *
     * @param id ID фильма
     * @return найденный фильм
     */
    @GetMapping("/find/{id}")
    public Film getFilm(@PathVariable Long id) {
        return filmService.findFimById(id);
    }

    /**
     * Создает новый фильм.
     *
     * @param film создает обьект фильма
     * @return созданный фильм
     */
    @PostMapping("/create")
    public ResponseEntity<Film> createFilm(
            @RequestBody Film film) {

        try {

            Film createdFilm = filmService.createFilm(film.getTitle(), film.getMinAge(), film.getDuration(), film.getDescription()
                    , film.getImageUrl());

            return new ResponseEntity<>(createdFilm, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Обновляет информацию о фильме.
     *
     * @param id   ID фильма
     * @param film объект фильма с новыми данными
     * @return статус обновления
     */
    @PutMapping("/update/{id}")
    public ResponseEntity<Film> updateFilm(@PathVariable Long id, @RequestBody Film film) {
        try {
            filmService.updateFilm(id, film.getTitle(), film.getMinAge(), film.getDuration(),
                    film.getDescription(), film.getImageUrl());
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Удаляет фильм по его ID.
     *
     * @param id ID фильма
     * @return статус удаления
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteFilm(@PathVariable Long id) {
        filmService.deleteFilm(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
