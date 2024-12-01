package ru.abarigena.NauJava.Service.FilmService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.abarigena.NauJava.Entities.Film;
import ru.abarigena.NauJava.Repository.FilmRepository;
import ru.abarigena.NauJava.Service.TicketService.TicketServiceImpl;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class FilmServiceImpl implements FilmService{
    private static final Logger logger = LoggerFactory.getLogger(TicketServiceImpl.class);

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
        logger.info("Создание нового фильма с названием: {}, минимальный возраст: {}, длительность: {}, описание: {}, изображение: {}",
                title, minAge, duration, description, imageURL);
        Film film = new Film();

        film.setTitle(title);
        film.setMinAge(minAge);
        film.setDuration(duration);
        film.setDescription(description);
        film.setImageUrl(imageURL);
        logger.info("Новый фильм с ID: {} успешно создан", film.getId());

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
        logger.info("Обновление данных фильма с ID: {}. Новое название: {}, минимальный возраст: {}, длительность: {}, описание: {}, изображение: {}",
                id, title, minAge, duration, description, imageURL);
        Film film = filmRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Film not found"));

        film.setTitle(title);
        film.setMinAge(minAge);
        film.setDuration(duration);
        film.setDescription(description);
        film.setImageUrl(imageURL);

        logger.info("Фильм с ID: {} успешно обновлен", id);

        return filmRepository.save(film);
    }

    /**
     * Удаляет фильм по его ID.
     *
     * @param id ID фильма
     */
    @Override
    public void deleteFilm(Long id) {
        logger.info("Удаление фильма с ID: {}", id);
        filmRepository.deleteById(id);
    }
}
