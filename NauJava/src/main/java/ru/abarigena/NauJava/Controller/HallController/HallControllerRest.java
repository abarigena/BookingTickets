package ru.abarigena.NauJava.Controller.HallController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.abarigena.NauJava.Entities.Hall;
import ru.abarigena.NauJava.Service.HallService.HallService;

import java.util.List;

/**
 * REST-контроллер для управления залами.
 */
@RestController
@RequestMapping("/api/halls")
public class HallControllerRest {
    private final HallService hallService;

    @Autowired
    public HallControllerRest(HallService hallService) {
        this.hallService = hallService;
    }

    /**
     * Возвращает список всех залов.
     *
     * @return Список залов.
     */
    @GetMapping("/all")
    public List<Hall> getAll() {
        return hallService.findAllHalls();
    }

    /**
     * Возвращает зал по его ID.
     *
     * @param id ID зала.
     * @return Зал.
     */
    @GetMapping("/find/{id}")
    public Hall findById(@PathVariable Long id) {
        return hallService.findHallById(id);
    }

    /**
     * Создает новый зал.
     *
     * @param hall Объект зала.
     * @return Созданный зал.
     */
    @PostMapping("/create")
    public ResponseEntity<Hall> createHall(@RequestBody Hall hall) {
        Hall createdHall = hallService.createHall(hall.getName(), hall.isActive());
        return new ResponseEntity<>(createdHall, HttpStatus.CREATED);
    }

    /**
     * Обновляет данные зала.
     *
     * @param id   ID зала.
     * @param hall Объект зала с обновленными данными.
     * @return Статус выполнения.
     */
    @PutMapping("/update/{id}")
    public ResponseEntity<Hall> updateHall(@PathVariable Long id, @RequestBody Hall hall) {
        hallService.updateHall(id, hall.getName(), hall.isActive());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Удаляет зал по его ID.
     *
     * @param id ID зала.
     * @return Статус выполнения.
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteHall(@PathVariable Long id) {
        hallService.deleteHall(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
