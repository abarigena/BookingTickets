package ru.abarigena.NauJava.test.Service.FilmService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import ru.abarigena.NauJava.Entities.Hall;
import ru.abarigena.NauJava.Entities.HallRow;
import ru.abarigena.NauJava.Repository.HallRepository;
import ru.abarigena.NauJava.Repository.HallRowRepository;
import ru.abarigena.NauJava.Service.HallService.HallServiceImpl;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HallServiceTest {

    @Mock
    private HallRepository hallRepository;

    @Mock
    private HallRowRepository hallRowRepository;

    @Mock
    private PlatformTransactionManager transactionManager;

    @Mock
    private TransactionStatus transactionStatus;

    @InjectMocks
    private HallServiceImpl hallService;

    @Test
    void testDeleteHall_Success() {
        Hall hall = new Hall();
        hall.setId(1L);
        hall.setName("Test Hall");
        hall.setActive(true);

        HallRow hallRow = new HallRow();
        hallRow.setId(1L);
        hallRow.setRow(1);
        hallRow.setSeatCount(10);
        hallRow.setHall(hall);

        lenient().when(hallRepository.findById(hall.getId())).thenReturn(Optional.of(hall));
        when(hallRowRepository.findHallRowsByHallId(hall.getId())).thenReturn(Arrays.asList(hallRow));
        when(transactionManager.getTransaction(any())).thenReturn(transactionStatus);

        hallService.deleteHall(hall.getId());

        verify(hallRowRepository, times(1)).deleteAll(Arrays.asList(hallRow));
        verify(hallRepository, times(1)).deleteById(hall.getId());
        verify(transactionManager, times(1)).commit(transactionStatus);
    }

    @Test
    void testFindHallById_Found() {
        Hall hall = new Hall();
        hall.setId(1L);
        hall.setName("Test Hall");
        hall.setActive(true);

        when(hallRepository.findById(anyLong())).thenReturn(Optional.of(hall));

        Hall result = hallService.findHallById(1L);

        assertNotNull(result);
        assertEquals(hall.getId(), result.getId());
    }

    @Test
    void testFindHallById_NotFound() {
        Long hallId = 1L;
        when(hallRepository.findById(hallId)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> hallService.findHallById(hallId));
    }

    @Test
    void testFindAllHalls() {
        Hall hall1 = new Hall();
        hall1.setId(1L);
        hall1.setName("Hall 1");
        hall1.setActive(true);

        Hall hall2 = new Hall();
        hall2.setId(2L);
        hall2.setName("Hall 2");
        hall2.setActive(true);

        List<Hall> halls = Arrays.asList(hall1, hall2);
        when(hallRepository.findAll()).thenReturn(halls);

        List<Hall> result = hallService.findAllHalls();

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void testCreateHall() {
        String hallName = "New Hall";
        boolean active = true;

        Hall hall = new Hall();
        hall.setId(1L);
        hall.setName(hallName);
        hall.setActive(active);

        when(hallRepository.save(any(Hall.class))).thenReturn(hall);

        Hall createdHall = hallService.createHall(hallName, active);

        assertNotNull(createdHall);
        assertEquals(hallName, createdHall.getName());
        assertTrue(createdHall.isActive());
    }

    @Test
    void testUpdateHall() {
        Long hallId = 1L;
        String newName = "Updated Hall";
        boolean newActiveStatus = false;

        Hall hall = new Hall();
        hall.setId(hallId);
        hall.setName("Old Name");
        hall.setActive(true);

        when(hallRepository.findById(hallId)).thenReturn(Optional.of(hall));
        when(hallRepository.save(any(Hall.class))).thenReturn(hall);

        Hall updatedHall = hallService.updateHall(hallId, newName, newActiveStatus);

        assertNotNull(updatedHall);
        assertEquals(newName, updatedHall.getName());
        assertFalse(updatedHall.isActive());
    }

}
