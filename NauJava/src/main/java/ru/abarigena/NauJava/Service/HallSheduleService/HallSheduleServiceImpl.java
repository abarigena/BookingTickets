package ru.abarigena.NauJava.Service.HallSheduleService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.abarigena.NauJava.Entities.Film;
import ru.abarigena.NauJava.Entities.Hall;
import ru.abarigena.NauJava.Entities.HallShedule;
import ru.abarigena.NauJava.Repository.HallSheduleRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class HallSheduleServiceImpl implements HallSheduleService {

    private final HallSheduleRepository hallSheduleRepository;

    @Autowired
    public HallSheduleServiceImpl(HallSheduleRepository hallSheduleRepository) {
        this.hallSheduleRepository = hallSheduleRepository;
    }

    /**
     * @param id
     * @return
     */
    @Override
    public HallShedule findHallSheduleById(Long id) {
        return hallSheduleRepository.findById(id).orElseThrow(NoSuchElementException::new);
    }

    /**
     * @return
     */
    @Override
    public List<HallShedule> findAllHallShedules() {
        return (List<HallShedule>) hallSheduleRepository.findAll();
    }

    /**
     * @param date
     * @return
     */
    @Override
    public List<HallShedule> findAllHallShedulesByDate(LocalDateTime date) {
        return List.of();
    }

    /**
     * @param film
     * @return
     */
    @Override
    public List<HallShedule> findAllHallShedulesByFilm(Film film) {
        return List.of();
    }

    /**
     * @param hall
     * @return
     */
    @Override
    public List<HallShedule> findAllHallShedulesByHall(Hall hall) {
        return List.of();
    }

    /**
     * @param date
     * @param film
     * @param hall
     * @return
     */
    @Override
    public HallShedule createHallShedule(LocalDateTime date, Film film, Hall hall) {
        HallShedule hallShedule = new HallShedule();

        hallShedule.setStartTime(date);
        hallShedule.setFilm(film);
        hallShedule.setHall(hall);

        return hallSheduleRepository.save(hallShedule);
    }

    /**
     * @param id
     * @param date
     * @param film
     * @param hall
     * @return
     */
    @Override
    public HallShedule updateHallShedule(Long id, LocalDateTime date, Film film, Hall hall) {
        HallShedule hallShedule = hallSheduleRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Hall not found"));

        hallShedule.setStartTime(date);
        hallShedule.setFilm(film);
        hallShedule.setHall(hall);

        return hallSheduleRepository.save(hallShedule);
    }

    /**
     * @param id
     */
    @Override
    public void deleteHallShedule(Long id) {
        hallSheduleRepository.deleteById(id);
    }
}
