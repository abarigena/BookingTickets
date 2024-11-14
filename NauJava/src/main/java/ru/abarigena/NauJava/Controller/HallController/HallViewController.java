package ru.abarigena.NauJava.Controller.HallController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.abarigena.NauJava.DTO.HallRequestDto;
import ru.abarigena.NauJava.DTO.RowRequestDto;
import ru.abarigena.NauJava.Entities.Hall;
import ru.abarigena.NauJava.Service.HallRowService.HallRowService;
import ru.abarigena.NauJava.Service.HallService.HallService;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/halls")
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
    public String createHallView() {
        return "createHall";
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<HallRequestDto> getHall(@PathVariable Long id) {
        Hall hall = hallService.findHallById(id);
        List<RowRequestDto> rows = hallRowService.getRowsByHallId(hall.getId()).stream()
                .map(row -> new RowRequestDto(row.getRow(), row.getSeatCount()))
                .collect(Collectors.toList());

        HallRequestDto hallDto = new HallRequestDto();
        hallDto.setName(hall.getName());
        hallDto.setActive(hall.isActive());
        hallDto.setRows(rows);

        return ResponseEntity.ok(hallDto);
    }

    @GetMapping("/edit/{id}")
    public String editHallView(@PathVariable Long id, Model model) {
        Hall hall = hallService.findHallById(id);
        List<RowRequestDto> rows = hallRowService.getRowsByHallId(hall.getId()).stream()
                .map(row -> new RowRequestDto(row.getRow(), row.getSeatCount()))
                .collect(Collectors.toList());

        model.addAttribute("hall", hall);
        model.addAttribute("rows", rows);
        return "editHall"; // Страница редактирования зала
    }
}
