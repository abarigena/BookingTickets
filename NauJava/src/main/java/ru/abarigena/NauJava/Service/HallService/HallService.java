package ru.abarigena.NauJava.Service.HallService;

import ru.abarigena.NauJava.Entities.Hall;

import java.util.List;

public interface HallService {
    void deleteHall(Long id);

    Hall findHallById(Long id);

    List<Hall> findAllHalls();

    Hall createHall(String name, boolean active);

    Hall updateHall(Long id, String name, boolean active);
}
