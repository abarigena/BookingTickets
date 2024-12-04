package ru.abarigena.NauJava.test.Controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.abarigena.NauJava.Controller.HallController.HallControllerRest;
import ru.abarigena.NauJava.Entities.Hall;
import ru.abarigena.NauJava.Service.HallService.HallService;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(HallControllerRest.class)
@AutoConfigureMockMvc(addFilters = false)
class HallControllerRestTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private HallService hallService;

    private Hall hall;

    @BeforeEach
    void setUp() {
        hall = new Hall();
        hall.setId(1L);
        hall.setName("Main Hall");
        hall.setActive(true);
    }

    @Test
    void testGetAllHalls() throws Exception {
        when(hallService.findAllHalls()).thenReturn(List.of(hall));

        mockMvc.perform(get("/api/halls/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Main Hall"))
                .andExpect(jsonPath("$[0].active").value(true));
    }

    @Test
    void testFindHallById() throws Exception {
        when(hallService.findHallById(1L)).thenReturn(hall);

        mockMvc.perform(get("/api/halls/find/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Main Hall"))
                .andExpect(jsonPath("$.active").value(true));
    }

    @Test
    void testCreateHall() throws Exception {
        when(hallService.createHall(anyString(), anyBoolean())).thenReturn(hall);

        mockMvc.perform(post("/api/halls/create")
                        .contentType("application/json")
                        .content("{\"name\":\"Main Hall\", \"active\":true}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Main Hall"))
                .andExpect(jsonPath("$.active").value(true));
    }

    @Test
    void testUpdateHall() throws Exception {
        when(hallService.updateHall(anyLong(), anyString(), anyBoolean())).thenReturn(hall);

        // Выполняем PUT запрос на /update/{id}
        mockMvc.perform(put("/api/halls/update/{id}", 1L)
                        .contentType("application/json")
                        .content("{\"name\":\"Updated Main Hall\", \"active\":false}"))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteHall() throws Exception {
        doNothing().when(hallService).deleteHall(anyLong());

        mockMvc.perform(delete("/api/halls/delete/{id}", 1L))
                .andExpect(status().isNoContent());
    }
}
