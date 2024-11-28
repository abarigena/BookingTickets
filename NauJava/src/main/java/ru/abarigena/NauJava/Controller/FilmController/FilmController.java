package ru.abarigena.NauJava.Controller.FilmController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.abarigena.NauJava.Entities.Film;
import ru.abarigena.NauJava.Service.FilmService.FilmService;
import ru.abarigena.NauJava.Service.S3Service.S3Service;

import java.io.IOException;
import java.util.List;

/**
 * Контроллер для управления фильмами.
 */
@Controller
@RequestMapping("/admin/films")
public class FilmController {

    private final FilmService filmService;
    private final S3Service s3Service;

    @Autowired
    public FilmController(FilmService filmService, S3Service s3Service) {
        this.filmService = filmService;
        this.s3Service = s3Service;
    }

    /**
     * Создание нового фильма.
     *
     * @param film  данные фильма
     * @param image изображение фильма
     * @return перенаправление на страницу редактирования фильма или страницу ошибки
     */
    @PostMapping("/create")
    public String createFilm(@ModelAttribute Film film, @RequestParam("image") MultipartFile image){
        try {
            // Загрузка изображения в S3
            String imageUrl = s3Service.uploadImage(image);

            // Создание фильма с полученным URL изображения
            Film exist = filmService.createFilm(film.getTitle(), film.getMinAge(), film.getDuration(),
                    film.getDescription(), imageUrl);

            return "redirect:/admin/films/edit/" + exist.getId();
        } catch (IOException e) {
            e.printStackTrace();
            // Обработка ошибки загрузки
            return "error";
        }
    }

    /**
     * Отображение формы создания фильма.
     *
     * @param model объект модели
     * @return путь к шаблону страницы создания фильма
     */
    @GetMapping("/create")
    public String createFilm(Model model){
        model.addAttribute("film", new Film());
        return "admin/filmCreate";
    }

    /**
     * Редактирование фильма.
     *
     * @param id               идентификатор фильма
     * @param film             обновленные данные фильма
     * @param image            новое изображение фильма
     * @param existingImageUrl текущий URL изображения
     * @return перенаправление на страницу со списком фильмов или страницу ошибки
     */
    @PostMapping("/edit/{id}")
    public String editFilm(@PathVariable("id") Long id, @ModelAttribute Film film, @RequestParam("image") MultipartFile image,
                           @RequestParam("existingImageUrl") String existingImageUrl) {
        try {
            // Если изображение не выбрано, используем существующий URL
            String imageUrl = image.isEmpty() ? existingImageUrl : s3Service.uploadImage(image);

            // Обновление данных фильма с новым URL изображения
            filmService.updateFilm(id, film.getTitle(), film.getMinAge(), film.getDuration(),
                    film.getDescription(), imageUrl);

            return "redirect:/admin/films";
        } catch (IOException e) {
            e.printStackTrace();
            return "error";
        }
    }

    /**
     * Отображение формы редактирования фильма.
     *
     * @param id    идентификатор фильма
     * @param model объект модели
     * @return путь к шаблону страницы редактирования фильма
     */
    @GetMapping("/edit/{id}")
    public String editFilm(@PathVariable("id") Long id, Model model){
        Film film = filmService.findFimById(id);
        model.addAttribute("film", film);
        return "admin/filmEdit";
    }

    /**
     * Отображение списка фильмов.
     *
     * @param model объект модели
     * @return путь к шаблону страницы списка фильмов
     */
    @GetMapping
    public String showFilms(Model model){
        List<Film> films = filmService.findAllFilms();
        model.addAttribute("films", films);
        return "admin/films";
    }

    /**
     * Удаление фильма.
     *
     * @param id идентификатор фильма
     * @return перенаправление на страницу со списком фильмов
     */
    @PostMapping("/delete/{id}")
    public String deleteFilm(@PathVariable("id") Long id){
        filmService.deleteFilm(id);
        return "redirect:/admin/films";
    }

    /**
     * Получение списка всех фильмов в формате JSON.
     *
     * @return список фильмов
     */
    @GetMapping("/all")
    @ResponseBody
    public List<Film> getAllFilms(){
        return filmService.findAllFilms();
    }
}
