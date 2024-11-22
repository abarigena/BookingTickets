package ru.abarigena.NauJava.Controller.HallController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.abarigena.NauJava.Entities.Hall;
import ru.abarigena.NauJava.Entities.HallRow;
import ru.abarigena.NauJava.Service.HallRowService.HallRowService;
import ru.abarigena.NauJava.Service.HallService.HallService;

import java.util.List;

@Controller
@RequestMapping("/admin/halls")
public class HallViewController {

    private final HallService hallService;
    private final HallRowService hallRowService;

    @Autowired
    public HallViewController(HallService hallService, HallRowService hallRowService) {
        this.hallService = hallService;
        this.hallRowService = hallRowService;
    }

    @GetMapping
    public String getAllHalls(Model model) {
        List<Hall> halls = hallService.findAllHalls(); // Получаем все залы
        model.addAttribute("halls", halls); // Добавляем зал в модель для передачи в HTML страницу
        return "halls"; // Отображаем страницу halls.html
    }

    @GetMapping("/create")
    public String createHallForm(Model model) {
        model.addAttribute("hall", new Hall());  // Пустой объект для создания нового зала
        return "createHall";
    }

    // Страница для редактирования существующего зала
    @GetMapping("/edit/{id}")
    public String editHallForm(@PathVariable("id") Long id, Model model) {
        Hall hall = hallService.findHallById(id);
        List<HallRow> rows = hallRowService.getRowsByHallId(id);
        model.addAttribute("hall", hall);
        model.addAttribute("rows", rows);  // Существующие ряды для редактирования
        return "editHall";
    }

    // Обработка создания нового зала
    @PostMapping("/create")
    public String createHall(@ModelAttribute Hall hall) {
        Hall exist = hallService.createHall(hall.getName(), hall.isActive());
        return "redirect:/admin/halls/edit/" + exist.getId();  // Перенаправление после создания
    }

    // Обработка обновления существующего зала
    @PostMapping("/edit/{id}")
    public String updateHall(@PathVariable("id") Long id, @ModelAttribute Hall hall) {
        hallService.updateHall(id, hall.getName(), hall.isActive());
        return "redirect:/admin/halls";  // Перенаправление после обновления
    }

    @PostMapping("/delete/{id}")
    public String deleteHall(@PathVariable Long id) {
        hallService.deleteHall(id);
        return "redirect:/admin/halls";  // Перенаправление на страницу со всеми залами после удаления
    }



}
