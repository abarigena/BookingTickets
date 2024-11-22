package ru.abarigena.NauJava.Service.FilmService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import ru.abarigena.NauJava.Entities.Film;
import ru.abarigena.NauJava.Repository.FilmRepository;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class FilmServiceImpl implements FilmService{

    private FilmRepository filmRepository;

    @Autowired
    public FilmServiceImpl(FilmRepository filmRepository) {
        this.filmRepository = filmRepository;
    }

    /**
     * Находит фильм по его ID.
     *
     * @param id ID фильма
     * @return объект {@link Film}
     */
    @Override
    public Film findFimById(Long id) {
        return filmRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Film not found"));
    }

    /**
     * Возвращает список всех фильмов.
     *
     * @return список фильмов
     */
    @Override
    public List<Film> findAllFilms() {
        return (List<Film>) filmRepository.findAll();
    }

    /**
     * Создает новый фильм с указанными параметрами.
     *
     * @param title       название фильма
     * @param minAge      минимальный возраст
     * @param duration    длительность
     * @param description описание
     * @param imageURL    URL изображения
     * @return созданный объект {@link Film}
     */
    @Override
    public Film createFilm(String title, int minAge, int duration, String description, String imageURL) {
        Film film = new Film();

        film.setTitle(title);
        film.setMinAge(minAge);
        film.setDuration(duration);
        film.setDescription(description);
        film.setImageUrl(imageURL);

        return filmRepository.save(film);
    }

    /**
     * Обновляет данные фильма с указанным ID.
     *
     * @param id          ID фильма
     * @param title       название
     * @param minAge      минимальный возраст
     * @param duration    длительность
     * @param description описание
     * @param imageURL    URL изображения
     * @return обновленный объект {@link Film}
     */
    @Override
    public Film updateFilm(Long id, String title, int minAge, int duration, String description, String imageURL) {
        Film film = filmRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Film not found"));

        film.setTitle(title);
        film.setMinAge(minAge);
        film.setDuration(duration);
        film.setDescription(description);
        film.setImageUrl(imageURL);

        return filmRepository.save(film);
    }

    /**
     * Удаляет фильм по его ID.
     *
     * @param id ID фильма
     */
    @Override
    public void deleteFilm(Long id) {
        filmRepository.deleteById(id);
    }
}
