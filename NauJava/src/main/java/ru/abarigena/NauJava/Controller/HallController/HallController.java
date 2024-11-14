package ru.abarigena.NauJava.Controller.HallController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.abarigena.NauJava.DTO.HallRequestDto;
import ru.abarigena.NauJava.Service.HallService.HallService;
import ru.abarigena.NauJava.Entities.Hall;

@RestController
@RequestMapping("/halls")
public class HallController {

    private final HallService hallService;

    @Autowired
    public HallController(HallService hallService) {
        this.hallService = hallService;
    }

    @PostMapping("/create")
    public ResponseEntity<Hall> createHall(@RequestBody HallRequestDto hallDto) {
        Hall createdHall = hallService.createHall(hallDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdHall);
    }

    @PostMapping("/edit/{id}")
    public ResponseEntity<Hall> updateHall(@PathVariable Long id, @RequestBody HallRequestDto hallDto) {
        Hall updatedHall = hallService.updateHall(id, hallDto);
        return ResponseEntity.ok(updatedHall);
    }

    @PostMapping("/delete/{hallName}")
    public ResponseEntity<Hall> deleteHall(@PathVariable String hallName) {
        hallService.deleteHall(hallName);
        return ResponseEntity.noContent().build();
    }
}
