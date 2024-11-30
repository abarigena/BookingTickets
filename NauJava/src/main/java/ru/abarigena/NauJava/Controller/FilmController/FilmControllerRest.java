package ru.abarigena.NauJava.Controller.FilmController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.abarigena.NauJava.Entities.Film;
import ru.abarigena.NauJava.Service.FilmService.FilmService;

import java.util.List;

@RestController
@RequestMapping("/api/films")
public class FilmControllerRest {
    private final FilmService filmService;

    @Autowired
    public FilmControllerRest(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping("/all")
    public List<Film> getAllFilms() {
        return filmService.findAllFilms();
    }

    @GetMapping("/find/{id}")
    public Film getFilm(@PathVariable Long id) {
        return filmService.findFimById(id);
    }

    @PostMapping("/create")
    public ResponseEntity<Film> createFilm(
            @RequestParam String title,
            @RequestParam int minAge,
            @RequestParam int duration,
            @RequestParam String description,
            @RequestParam String imageUrl) {

        try {

            Film createdFilm = filmService.createFilm(title, minAge, duration, description, imageUrl);

            return new ResponseEntity<>(createdFilm, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

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

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteFilm(@PathVariable Long id) {
        filmService.deleteFilm(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
