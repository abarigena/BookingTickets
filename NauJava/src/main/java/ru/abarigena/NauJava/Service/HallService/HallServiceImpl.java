package ru.abarigena.NauJava.Service.HallService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import ru.abarigena.NauJava.DTO.HallRequestDto;
import ru.abarigena.NauJava.DTO.RowRequestDto;
import ru.abarigena.NauJava.Entities.Hall;
import ru.abarigena.NauJava.Entities.HallRow;
import ru.abarigena.NauJava.Repository.HallRepository;
import ru.abarigena.NauJava.Repository.HallRowRepository;

import java.util.List;

@Service
public class HallServiceImpl implements HallService {

    private final HallRepository hallRepository;
    private final HallRowRepository hallRowRepository;
    private final PlatformTransactionManager transactionManager;

    @Autowired
    public HallServiceImpl(HallRepository hallRepository, HallRowRepository hallRowRepository,
                           PlatformTransactionManager transactionManager)
    {
        this.hallRepository = hallRepository;
        this.hallRowRepository = hallRowRepository;
        this.transactionManager = transactionManager;
    }

    @Override
    public void deleteHall(String hallName)
    {

        TransactionStatus status = transactionManager.getTransaction(new
                DefaultTransactionDefinition());
        try {

            List<HallRow> hallRows = hallRowRepository.findHallRowsByHallName(hallName);
            hallRowRepository.deleteAll(hallRows);

            hallRepository.deleteByName(hallName);

            transactionManager.commit(status);
        }
        catch (DataAccessException e) {
            transactionManager.rollback(status);
            throw e;
        }
    }

    /**
     * @param hallRequestDto
     * @return
     */
    @Override
    public Hall createHall(HallRequestDto hallRequestDto) {
        Hall hall = new Hall();
        hall.setName(hallRequestDto.getName());
        hall.setActive(hallRequestDto.isActive());

        Hall savedHall = hallRepository.save(hall);

        for(RowRequestDto rowDto : hallRequestDto.getRows()) {
            HallRow hallRow = new HallRow();
            hallRow.setRow(rowDto.getRow());
            hallRow.setSeatCount(rowDto.getSeatCount());
            hallRow.setHall(savedHall);
            hallRowRepository.save(hallRow);
        }
        return savedHall;
    }

    /**
     * @param id
     * @param hallRequestDto
     * @return
     */

    @Transactional
    @Override
    public Hall updateHall(Long id, HallRequestDto hallRequestDto) {
        Hall hall = hallRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hall not found"));

        hall.setName(hallRequestDto.getName());
        hall.setActive(hallRequestDto.isActive());

        hallRowRepository.deleteByHall(hall);

        for (RowRequestDto rowDto : hallRequestDto.getRows()) {
            HallRow hallRow = new HallRow();
            hallRow.setRow(rowDto.getRow());
            hallRow.setSeatCount(rowDto.getSeatCount());
            hallRow.setHall(hall);
            hallRowRepository.save(hallRow);
        }
        return hall;
    }

    /**
     * @param id
     * @return
     */
    @Override
    public Hall findHallById(Long id) {
        return hallRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Hall not found"));
    }

    /**
     * @return
     */
    @Override
    public List<Hall> findAllHalls() {
        return (List<Hall>) hallRepository.findAll();
    }
}
