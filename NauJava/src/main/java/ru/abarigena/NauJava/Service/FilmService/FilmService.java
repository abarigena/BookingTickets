package ru.abarigena.NauJava.Service.FilmService;

import ru.abarigena.NauJava.Entities.Film;

import java.util.List;

public interface FilmService {
    Film findFimById(Long id);

    List<Film> findAllFilms();

    Film createFilm(String title, int minAge, int duration, String description, String imageURL);

    Film updateFilm(Long id, String title, int minAge, int duration, String description, String imageURL);

    void deleteFilm(Long id);
}
