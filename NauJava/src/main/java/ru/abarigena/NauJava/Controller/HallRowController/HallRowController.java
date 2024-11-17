package ru.abarigena.NauJava.Controller.HallRowController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.abarigena.NauJava.Entities.Hall;
import ru.abarigena.NauJava.Entities.HallRow;
import ru.abarigena.NauJava.Service.HallRowService.HallRowService;
import ru.abarigena.NauJava.Service.HallService.HallService;

@Controller
@RequestMapping("/hallRows")
public class HallRowController {

    private final HallRowService hallRowService;
    private final HallService hallService;

    @Autowired
    public HallRowController(HallRowService hallRowService, HallService hallService) {
        this.hallRowService = hallRowService;
        this.hallService = hallService;
    }

    @PostMapping("/create")
    public String createRow(@RequestParam Long hallId, @RequestParam int seatCount) {
        Hall hall = hallService.findHallById(hallId);
        hallRowService.createRow(hall, seatCount);
        return "redirect:/halls/edit/" + hallId;  // Перенаправление на страницу редактирования зала
    }

    // Обработка удаления ряда
    @PostMapping("/delete/{id}")
    public String deleteRow(@PathVariable("id") Long id, @RequestParam Long hallId) {
        HallRow hallRow = hallRowService.findRowById(id);
        hallRowService.deleteRow(hallRow);
        return "redirect:/halls/edit/" + hallId;  // Перенаправление на страницу редактирования зала
    }
}
