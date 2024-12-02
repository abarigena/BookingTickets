package ru.abarigena.NauJava.Service.FilmService;

import ru.abarigena.NauJava.Entities.Film;

import java.util.List;

/**
 * Сервис для работы с фильмами.
 */
public interface FilmService {
    /**
     * Находит фильм по его идентификатору.
     *
     * @param id идентификатор фильма
     * @return найденный фильм
     */
    Film findFimById(Long id);

    /**
     * Получает список всех фильмов.
     *
     * @return список всех фильмов
     */
    List<Film> findAllFilms();

    /**
     * Создает новый фильм.
     *
     * @param title название фильма
     * @param minAge минимальный возраст для просмотра
     * @param duration продолжительность фильма в минутах
     * @param description описание фильма
     * @param imageURL URL изображения для фильма
     * @return созданный фильм
     */
    Film createFilm(String title, int minAge, int duration, String description, String imageURL);

    /**
     * Обновляет данные фильма по его идентификатору.
     *
     * @param id идентификатор фильма
     * @param title новое название фильма
     * @param minAge новый минимальный возраст
     * @param duration новая продолжительность фильма
     * @param description новое описание фильма
     * @param imageURL новый URL изображения фильма
     * @return обновленный фильм
     */
    Film updateFilm(Long id, String title, int minAge, int duration, String description, String imageURL);

    /**
     * Удаляет фильм по его идентификатору.
     *
     * @param id идентификатор фильма
     */
    void deleteFilm(Long id);
}
