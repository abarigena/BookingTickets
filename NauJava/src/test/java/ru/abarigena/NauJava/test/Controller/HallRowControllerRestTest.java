package ru.abarigena.NauJava.test.Controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import ru.abarigena.NauJava.Controller.HallRowController.HallRowControllerRest;
import ru.abarigena.NauJava.Entities.Hall;
import ru.abarigena.NauJava.Entities.HallRow;
import ru.abarigena.NauJava.Service.HallRowService.HallRowService;
import ru.abarigena.NauJava.Service.HallService.HallService;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(HallRowControllerRest.class)
@AutoConfigureMockMvc(addFilters = false)
class HallRowControllerRestTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private HallRowService hallRowService;

    @MockBean
    private HallService hallService;

    private Hall hall;
    private HallRow hallRow;

    @BeforeEach
    void setUp() {
        hall = new Hall();
        hall.setId(1L);
        hall.setName("Main Hall");
        hall.setActive(true);

        hallRow = new HallRow();
        hallRow.setId(1L);
        hallRow.setSeatCount(10);
        hallRow.setRow(1);
        hallRow.setHall(hall);
    }

    @Test
    void testCreateRow() throws Exception {
        when(hallService.findHallById(1L)).thenReturn(hall);
        when(hallRowService.createRow(hall, 10)).thenReturn(hallRow);

        mockMvc.perform(post("/api/hallRows/create")
                        .param("hallId", "1")
                        .param("seatCount", "10"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.seatCount").value(10))
                .andExpect(jsonPath("$.row").value(1))
                .andExpect(jsonPath("$.hall.id").value(1));
    }

    @Test
    void testUpdateRow() throws Exception {
        when(hallRowService.updateRow(1L, 20)).thenReturn(hallRow);

        mockMvc.perform(put("/api/hallRows/update/{id}", 1L)
                        .param("seatCount", "20"))
                .andExpect(status().isOk());

        verify(hallRowService, times(1)).updateRow(1L, 20);
    }

    @Test
    void testDeleteRow() throws Exception {
        when(hallRowService.findRowById(1L)).thenReturn(hallRow);
        doNothing().when(hallRowService).deleteRow(hallRow);

        mockMvc.perform(delete("/api/hallRows/delete/{id}", 1L)
                        .param("hallId", "1"))
                .andExpect(status().isNoContent());

        verify(hallRowService, times(1)).deleteRow(hallRow);
    }
}
