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

    /**
     * Отображение всех залов.
     *
     * @param model объект модели для передачи данных в шаблон
     * @return путь к шаблону со списком залов
     */
    @GetMapping
    public String getAllHalls(Model model) {
        List<Hall> halls = hallService.findAllHalls();
        model.addAttribute("halls", halls);
        return "admin/halls";
    }

    /**
     * Отображение формы создания нового зала.
     *
     * @param model объект модели
     * @return путь к шаблону создания зала
     */
    @GetMapping("/create")
    public String createHallForm(Model model) {
        model.addAttribute("hall", new Hall());
        return "admin/createHall";
    }

    /**
     * Отображение формы редактирования существующего зала.
     *
     * @param id    идентификатор зала
     * @param model объект модели
     * @return путь к шаблону редактирования зала
     */
    @GetMapping("/edit/{id}")
    public String editHallForm(@PathVariable("id") Long id, Model model) {
        Hall hall = hallService.findHallById(id);
        List<HallRow> rows = hallRowService.getRowsByHallId(id);
        model.addAttribute("hall", hall);
        model.addAttribute("rows", rows);  // Существующие ряды для редактирования
        return "admin/editHall";
    }

    /**
     * Обработка создания нового зала.
     *
     * @param hall объект зала
     * @return перенаправление на страницу редактирования созданного зала
     */
    @PostMapping("/create")
    public String createHall(@ModelAttribute Hall hall) {
        Hall exist = hallService.createHall(hall.getName(), hall.isActive());
        return "redirect:/admin/halls/edit/" + exist.getId();
    }

    /**
     * Обработка обновления существующего зала.
     *
     * @param id   идентификатор зала
     * @param hall обновленные данные зала
     * @return перенаправление на страницу со списком залов
     */
    @PostMapping("/edit/{id}")
    public String updateHall(@PathVariable("id") Long id, @ModelAttribute Hall hall) {
        hallService.updateHall(id, hall.getName(), hall.isActive());
        return "redirect:/admin/halls";
    }

    /**
     * Удаление зала.
     *
     * @param id идентификатор зала
     * @return перенаправление на страницу со списком залов
     */
    @PostMapping("/delete/{id}")
    public String deleteHall(@PathVariable Long id) {
        hallService.deleteHall(id);
        return "redirect:/admin/halls";  // Перенаправление на страницу со всеми залами после удаления
    }
}
