package ru.abarigena.NauJava.Service.HallRowService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.abarigena.NauJava.Entities.HallRow;
import ru.abarigena.NauJava.Repository.HallRowRepository;

import java.util.List;

@Service
public class HallRowServiceImpl implements HallRowService {

    private final HallRowRepository hallRowRepository;

    @Autowired
    public HallRowServiceImpl(HallRowRepository hallRowRepository) {
        this.hallRowRepository = hallRowRepository;
    }
    /**
     * @param hallId
     * @return
     */
    @Override
    public List<HallRow> getRowsByHallId(Long hallId) {
        return hallRowRepository.findHallRowsByHallId(hallId);
    }
}
