package ru.abarigena.NauJava.Service.HallService;

import ru.abarigena.NauJava.DTO.HallRequestDto;
import ru.abarigena.NauJava.Entities.Hall;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface HallService {
    void deleteHall(String hallName);

    Hall createHall(HallRequestDto hallRequestDto);

    Hall updateHall(Long id, HallRequestDto hallRequestDto);

    Hall findHallById(Long id);

    List<Hall> findAllHalls();
}
