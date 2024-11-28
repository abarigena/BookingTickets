package ru.abarigena.NauJava.Controller.HallRowController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.abarigena.NauJava.Entities.Hall;
import ru.abarigena.NauJava.Entities.HallRow;
import ru.abarigena.NauJava.Service.HallRowService.HallRowService;
import ru.abarigena.NauJava.Service.HallService.HallService;

/**
 * Контроллер для управления рядами в зале.
 */
@Controller
@RequestMapping("/admin/hallRows")
public class HallRowController {

    private final HallRowService hallRowService;
    private final HallService hallService;

    @Autowired
    public HallRowController(HallRowService hallRowService, HallService hallService) {
        this.hallRowService = hallRowService;
        this.hallService = hallService;
    }

    /**
     * Создание нового ряда в зале.
     *
     * @param hallId    идентификатор зала
     * @param seatCount количество мест в ряду
     * @return перенаправление на страницу редактирования зала
     */
    @PostMapping("/create")
    public String createRow(@RequestParam Long hallId, @RequestParam int seatCount) {
        Hall hall = hallService.findHallById(hallId);
        hallRowService.createRow(hall, seatCount);
        return "redirect:/admin/halls/edit/" + hallId;  // Перенаправление на страницу редактирования зала
    }

    /**
     * Удаление ряда.
     *
     * @param id     идентификатор ряда
     * @param hallId идентификатор зала
     * @return перенаправление на страницу редактирования зала
     */
    @PostMapping("/delete/{id}")
    public String deleteRow(@PathVariable("id") Long id, @RequestParam Long hallId) {
        HallRow hallRow = hallRowService.findRowById(id);
        hallRowService.deleteRow(hallRow);
        return "redirect:/admin/halls/edit/" + hallId;
    }

    /**
     * Обновление ряда.
     *
     * @param id        идентификатор ряда
     * @param seatCount новое количество мест
     * @param hallId    идентификатор зала
     * @return перенаправление на страницу редактирования зала
     */
    @PostMapping("/update/{id}")
    public String updateRow(@PathVariable("id") Long id, @RequestParam int seatCount, @RequestParam Long hallId) {
        hallRowService.updateRow(id, seatCount); // Вызов метода из сервиса
        return "redirect:/admin/halls/edit/" + hallId;
    }

}
