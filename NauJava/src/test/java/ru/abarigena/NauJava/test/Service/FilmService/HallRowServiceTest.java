package ru.abarigena.NauJava.test.Service.FilmService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.abarigena.NauJava.Entities.Hall;
import ru.abarigena.NauJava.Entities.HallRow;
import ru.abarigena.NauJava.Repository.HallRowRepository;
import ru.abarigena.NauJava.Service.HallRowService.HallRowServiceImpl;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HallRowServiceTest {

    @Mock
    private HallRowRepository hallRowRepository;

    @InjectMocks
    private HallRowServiceImpl hallRowService;

    @Test
    void testGetRowsByHallId() {
        Hall hall = new Hall();
        hall.setId(1L);
        HallRow hallRow = new HallRow();
        hallRow.setId(1L);
        hallRow.setRow(1);
        hallRow.setSeatCount(10);
        hallRow.setHall(hall);

        when(hallRowRepository.findHallRowsByHallId(hall.getId())).thenReturn(Arrays.asList(hallRow));

        List<HallRow> rows = hallRowService.getRowsByHallId(hall.getId());

        assertNotNull(rows);
        assertEquals(1, rows.size());
        assertEquals(hallRow.getId(), rows.get(0).getId());
    }

    @Test
    void testDeleteRow() {
        Hall hall = new Hall();
        hall.setId(1L);
        HallRow hallRow = new HallRow();
        hallRow.setId(1L);
        hallRow.setRow(1);
        hallRow.setSeatCount(10);
        hallRow.setHall(hall);

        when(hallRowRepository.findByHallOrderByRow(hall)).thenReturn(Arrays.asList(hallRow));
        doNothing().when(hallRowRepository).delete(hallRow);
        when(hallRowRepository.save(any(HallRow.class))).thenReturn(hallRow);

        hallRowService.deleteRow(hallRow);

        verify(hallRowRepository, times(1)).delete(hallRow);
        verify(hallRowRepository, times(1)).save(any(HallRow.class));
    }

    @Test
    void testUpdateRow() {
        HallRow hallRow = new HallRow();
        hallRow.setId(1L);
        hallRow.setRow(1);
        hallRow.setSeatCount(10);

        when(hallRowRepository.findById(hallRow.getId())).thenReturn(Optional.of(hallRow));
        when(hallRowRepository.save(hallRow)).thenReturn(hallRow);

        HallRow updatedRow = hallRowService.updateRow(hallRow.getId(), 20);

        assertNotNull(updatedRow);
        assertEquals(20, updatedRow.getSeatCount());
    }

    @Test
    void testCreateRow() {
        Hall hall = new Hall();
        hall.setId(1L);
        hall.setName("Test Hall");

        HallRow hallRow = new HallRow();
        hallRow.setRow(1);
        hallRow.setSeatCount(10);
        hallRow.setHall(hall);

        when(hallRowRepository.findMaxRowByHall(hall)).thenReturn(null);
        when(hallRowRepository.save(any(HallRow.class))).thenReturn(hallRow);

        HallRow createdRow = hallRowService.createRow(hall, 10);


        assertNotNull(createdRow);
        assertEquals(1, createdRow.getRow());
        assertEquals(10, createdRow.getSeatCount());
    }

    @Test
    void testFindRowById() {
        HallRow hallRow = new HallRow();
        hallRow.setId(1L);
        hallRow.setRow(1);
        hallRow.setSeatCount(10);

        when(hallRowRepository.findById(hallRow.getId())).thenReturn(Optional.of(hallRow));

        HallRow foundRow = hallRowService.findRowById(hallRow.getId());

        assertNotNull(foundRow);
        assertEquals(hallRow.getId(), foundRow.getId());
    }

    @Test
    void testFindRowByHall() {
        Hall hall = new Hall();
        hall.setId(1L);
        HallRow hallRow1 = new HallRow();
        hallRow1.setRow(1);
        hallRow1.setSeatCount(10);
        hallRow1.setHall(hall);

        HallRow hallRow2 = new HallRow();
        hallRow2.setRow(2);
        hallRow2.setSeatCount(12);
        hallRow2.setHall(hall);

        when(hallRowRepository.findByHallOrderByRow(hall)).thenReturn(Arrays.asList(hallRow1, hallRow2));

        var rows = hallRowService.findRowByHall(hall);

        assertNotNull(rows);
        assertEquals(2, rows.size());
    }

}
