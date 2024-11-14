package ru.abarigena.NauJava.Service.HallRowService;

import ru.abarigena.NauJava.Entities.HallRow;

import java.util.List;

public interface HallRowService {

    List<HallRow> getRowsByHallId(Long hallId);
}
