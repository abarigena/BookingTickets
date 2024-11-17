package ru.abarigena.NauJava.Service.HallService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import ru.abarigena.NauJava.Entities.Hall;
import ru.abarigena.NauJava.Entities.HallRow;
import ru.abarigena.NauJava.Repository.HallRepository;
import ru.abarigena.NauJava.Repository.HallRowRepository;

import java.util.List;
import java.util.NoSuchElementException;

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
    public void deleteHall(Long id)
    {

        TransactionStatus status = transactionManager.getTransaction(new
                DefaultTransactionDefinition());
        try {

            List<HallRow> hallRows = hallRowRepository.findHallRowsByHallId(id);
            hallRowRepository.deleteAll(hallRows);

            hallRepository.deleteById(id);

            transactionManager.commit(status);
        }
        catch (DataAccessException e) {
            transactionManager.rollback(status);
            throw e;
        }
    }

    /**
     * @param id
     * @return
     */
    @Override
    public Hall findHallById(Long id) {
        return hallRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Hall not found"));
    }

    /**
     * @return
     */
    @Override
    public List<Hall> findAllHalls() {
        return (List<Hall>) hallRepository.findAll();
    }

    /**
     * @param name
     * @param active
     * @return
     */
    @Override
    public Hall createHall(String name, boolean active) {
        Hall hall = new Hall();
        hall.setName(name);
        hall.setActive(active);
        return hallRepository.save(hall);
    }

    /**
     * @param id
     * @param name
     * @param active
     * @return
     */
    @Override
    public Hall updateHall(Long id, String name, boolean active) {
        // Найти зал по id
        Hall hall = hallRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Hall not found"));

        // Обновить поля
        hall.setName(name);
        hall.setActive(active);

        // Сохранить обновленный зал
        return hallRepository.save(hall);
    }
}
