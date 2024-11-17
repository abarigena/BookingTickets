package ru.abarigena.NauJava.Controller.HallController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.abarigena.NauJava.Service.HallRowService.HallRowService;
import ru.abarigena.NauJava.Service.HallService.HallService;
import ru.abarigena.NauJava.Entities.Hall;

import java.util.List;

@RestController
@RequestMapping("/api/halls")
public class HallController {

    private final HallService hallService;
    private final HallRowService hallRowService;

    @Autowired
    public HallController(HallService hallService, HallRowService hallRowService) {
        this.hallService = hallService;
        this.hallRowService = hallRowService;
    }

    @PostMapping("/delete/{id}")
    public ResponseEntity<Hall> deleteHall(@PathVariable Long id) {
        hallService.deleteHall(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/all")
    @ResponseBody
    public List<Hall> getAllHalls() {
        return hallService.findAllHalls();
    }
}
