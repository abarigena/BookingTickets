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
            return "error"; // Переход на страницу с ошибкой
        }
    }

    @GetMapping("/create")
    public String createFilm(Model model){
        model.addAttribute("film", new Film());
        return "filmCreate";
    }

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
            // Обработка ошибки загрузки
            return "error"; // Переход на страницу с ошибкой
        }
    }


    @GetMapping("/edit/{id}")
    public String editFilm(@PathVariable("id") Long id, Model model){
        Film film = filmService.findFimById(id);
        model.addAttribute("film", film);
        return "filmEdit";
    }

    @GetMapping
    public String showFilms(Model model){
        List<Film> films = filmService.findAllFilms();
        model.addAttribute("films", films);
        return "films";
    }

    @PostMapping("/delete/{id}")
    public String deleteFilm(@PathVariable("id") Long id){
        filmService.deleteFilm(id);
        return "redirect:/admin/films";
    }

    @GetMapping("/all")
    @ResponseBody
    public List<Film> getAllFilms(){
        return filmService.findAllFilms();
    }
}
