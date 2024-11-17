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
     * @param id
     * @return
     */
    @Override
    public Film findFimById(Long id) {
        return filmRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Film not found"));
    }

    /**
     * @return
     */
    @Override
    public List<Film> findAllFilms() {
        return (List<Film>) filmRepository.findAll();
    }

    /**
     * @param title
     * @param minAge
     * @param duration
     * @param description
     * @param imageURL
     * @return
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
     * @param id
     * @param title
     * @param minAge
     * @param duration
     * @param description
     * @param imageURL
     * @return
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
     * @param id
     */
    @Override
    public void deleteFilm(Long id) {
        filmRepository.deleteById(id);
    }
}
