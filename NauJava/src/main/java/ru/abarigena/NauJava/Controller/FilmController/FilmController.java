package ru.abarigena.NauJava.Controller.FilmController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.abarigena.NauJava.Entities.Film;
import ru.abarigena.NauJava.Service.FilmService.FilmService;

import java.util.List;

@Controller
@RequestMapping("/films")
public class FilmController {

    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @PostMapping("/create")
    public String createFilm(@ModelAttribute Film film){
        Film exist = filmService.createFilm(film.getTitle(), film.getMinAge(), film.getDuration(),
                film.getDescription(), film.getImageUrl());
        return "redirect:/films/edit/" + exist.getId();
    }

    @GetMapping("/create")
    public String createFilm(Model model){
        model.addAttribute("film", new Film());
        return "filmCreate";
    }

    @PostMapping("/edit/{id}")
    public String editFilm(@PathVariable("id") Long id, @ModelAttribute Film film){
        filmService.updateFilm(id, film.getTitle(), film.getMinAge(), film.getDuration(),
                film.getDescription(), film.getImageUrl());
        return "redirect:/films";
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
        return "redirect:/films";
    }

    @GetMapping("/all")
    @ResponseBody
    public List<Film> getAllFilms(){
        return filmService.findAllFilms();
    }
}
