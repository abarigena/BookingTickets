package ru.abarigena.NauJava.Service.HallRowService;

import ru.abarigena.NauJava.Entities.Hall;
import ru.abarigena.NauJava.Entities.HallRow;

import java.util.List;

public interface HallRowService {

    List<HallRow> getRowsByHallId(Long hallId);

    HallRow createRow(Hall hall, int seatCount);

    void deleteRow(HallRow hallRow);

    HallRow updateRow(Long id, int seatCount);

    HallRow findRowById(Long id);

    List<HallRow> findRowByHall(Hall hall);
}
