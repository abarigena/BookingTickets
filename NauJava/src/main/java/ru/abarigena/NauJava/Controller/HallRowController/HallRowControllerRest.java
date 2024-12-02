package ru.abarigena.NauJava.Controller.HallRowController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.abarigena.NauJava.Entities.Hall;
import ru.abarigena.NauJava.Entities.HallRow;
import ru.abarigena.NauJava.Service.HallRowService.HallRowService;
import ru.abarigena.NauJava.Service.HallService.HallService;

/**
 * REST-контроллер для управления рядами в зале.
 */
@RestController
@RequestMapping("/api/hallRows")
public class HallRowControllerRest {
    private final HallRowService hallRowService;
    private final HallService hallService;

    @Autowired
    public HallRowControllerRest(HallRowService hallRowService, HallService hallService) {
        this.hallRowService = hallRowService;
        this.hallService = hallService;
    }

    /**
     * Создает новый ряд в зале.
     *
     * @param hallId    ID зала, к которому относится ряд.
     * @param seatCount Количество мест в ряду.
     * @return Созданный ряд.
     */
    @PostMapping("/create")
    public ResponseEntity<HallRow> createRow(@RequestParam Long hallId, @RequestParam int seatCount) {
        Hall hall = hallService.findHallById(hallId);
        HallRow createdRow = hallRowService.createRow(hall, seatCount);
        return new ResponseEntity<>(createdRow, HttpStatus.CREATED);
    }

    /**
     * Обновляет данные ряда.
     *
     * @param id        ID ряда.
     * @param seatCount Обновленное количество мест в ряду.
     * @return Статус выполнения.
     */
    @PutMapping("/update/{id}")
    public ResponseEntity<HallRow> updateRow(@PathVariable("id") Long id, @RequestParam int seatCount) {
        hallRowService.updateRow(id, seatCount);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Удаляет ряд в зале.
     *
     * @param id     ID ряда.
     * @param hallId ID зала, к которому относится ряд.
     * @return Статус выполнения.
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteRow(@PathVariable("id") Long id, @RequestParam Long hallId) {
        HallRow hallRow = hallRowService.findRowById(id);
        hallRowService.deleteRow(hallRow);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
