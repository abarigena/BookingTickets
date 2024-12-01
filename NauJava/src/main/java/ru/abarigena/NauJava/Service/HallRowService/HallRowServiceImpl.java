package ru.abarigena.NauJava.Service.HallRowService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.abarigena.NauJava.Entities.Hall;
import ru.abarigena.NauJava.Entities.HallRow;
import ru.abarigena.NauJava.Repository.HallRowRepository;
import ru.abarigena.NauJava.Service.TicketService.TicketServiceImpl;

import java.util.List;

@Service
public class HallRowServiceImpl implements HallRowService {
    private static final Logger logger = LoggerFactory.getLogger(TicketServiceImpl.class);

    private final HallRowRepository hallRowRepository;

    @Autowired
    public HallRowServiceImpl(HallRowRepository hallRowRepository) {
        this.hallRowRepository = hallRowRepository;
    }
    /**
     * Находит все ряды в зале по его ID.
     *
     * @param hallId ID зала
     * @return список рядов
     */
    @Override
    public List<HallRow> getRowsByHallId(Long hallId) {
        return hallRowRepository.findHallRowsByHallId(hallId);
    }

    /**
     * Создает новый ряд в указанном зале с заданным количеством мест.
     *
     * @param hall      объект зала
     * @param seatCount количество мест
     * @return созданный объект {@link HallRow}
     */
    @Override
    public HallRow createRow(Hall hall, int seatCount) {
        // Находим последний ряд для данного зала, если есть
        Integer maxRow = hallRowRepository.findMaxRowByHall(hall);

        // Если нет рядов, начинаем с первого
        int newRow = (maxRow == null) ? 1 : maxRow + 1;

        HallRow hallRow = new HallRow();
        hallRow.setRow(newRow);
        hallRow.setSeatCount(seatCount);
        hallRow.setHall(hall);

        return hallRowRepository.save(hallRow);
    }

    /**
     * Удаляет указанный ряд и перенумеровывает остальные.
     *
     * @param hallRow объект ряда
     */
    @Override
    public void deleteRow(HallRow hallRow) {
        hallRowRepository.delete(hallRow);

        // Перенумерация всех рядов в зале
        List<HallRow> rows = hallRowRepository.findByHallOrderByRow(hallRow.getHall());
        int newRowNumber = 1;

        for (HallRow row : rows) {
            row.setRow(newRowNumber++);
            hallRowRepository.save(row);
        }
    }

    /**
     * Обновляет количество мест в ряду с указанным ID.
     *
     * @param id        ID ряда
     * @param seatCount новое количество мест
     * @return обновленный объект {@link HallRow}
     */
    @Override
    public HallRow updateRow(Long id, int seatCount) {
        // Найти ряд по id
        HallRow hallRow = hallRowRepository.findById(id).orElseThrow(() -> new RuntimeException("HallRow not found"));

        // Обновить количество мест
        hallRow.setSeatCount(seatCount);

        // Сохранить обновленный ряд
        return hallRowRepository.save(hallRow);
    }

    /**
     * Находит ряд по его ID.
     *
     * @param id ID ряда
     * @return объект {@link HallRow}
     */
    @Override
    public HallRow findRowById(Long id) {
        return hallRowRepository.findById(id).orElseThrow(() -> new RuntimeException("HallRow not found"));
    }

    /**
     * Находит ряды зала, отсортированные по номеру.
     *
     * @param hall объект {@link Hall}, для которого нужно получить ряды
     * @return список рядов зала, отсортированный по номеру
     */
    @Override
    public List<HallRow> findRowByHall(Hall hall) {
        return hallRowRepository.findByHallOrderByRow(hall);
    }
}
