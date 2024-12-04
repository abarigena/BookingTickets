package ru.abarigena.NauJava.test.Controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.abarigena.NauJava.Controller.HallSheduleController.HallScheduleControllerUserRest;
import ru.abarigena.NauJava.Entities.Film;
import ru.abarigena.NauJava.Entities.Hall;
import ru.abarigena.NauJava.Entities.HallRow;
import ru.abarigena.NauJava.Entities.HallShedule.HallShedule;
import ru.abarigena.NauJava.Entities.Ticket.Ticket;
import ru.abarigena.NauJava.Entities.User.User;
import ru.abarigena.NauJava.Service.HallRowService.HallRowService;
import ru.abarigena.NauJava.Service.HallSheduleService.HallSheduleService;
import ru.abarigena.NauJava.Service.TicketService.TicketService;
import ru.abarigena.NauJava.Service.UserService.UserService;

import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(HallScheduleControllerUserRest.class)
@AutoConfigureMockMvc(addFilters = false)
class HallScheduleControllerUserRestTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private HallSheduleService hallSheduleService;

    @MockBean
    private HallRowService hallRowService;

    @MockBean
    private TicketService ticketService;

    @MockBean
    private UserService userService;

    private HallShedule hallShedule;
    private HallRow hallRow;
    private User user;
    private Ticket ticket;
    private Film film;

    @BeforeEach
    void setUp() {
        film = new Film();
        film.setId(1L);
        film.setTitle("Test Film");
        film.setDuration(120);

        hallShedule = new HallShedule();
        hallShedule.setId(1L);
        hallShedule.setHall(new Hall());
        hallShedule.getHall().setId(1L);
        hallShedule.getHall().setName("Main Hall");
        hallShedule.setFilm(film);

        hallRow = new HallRow();
        hallRow.setId(1L);
        hallRow.setRow(1);
        hallRow.setSeatCount(10);
        hallRow.setHall(hallShedule.getHall());

        user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");

        ticket = new Ticket();
        ticket.setId(1L);
        ticket.setRow(1);
        ticket.setSeat(1);
        ticket.setUser(user);
        ticket.setHallShedule(hallShedule);
    }

    @Test
    void testGetHallSchema() throws Exception {
        List<HallRow> hallRows = List.of(hallRow);
        Map<Integer, List<Integer>> bookedSeats = Map.of(1, List.of(1));

        when(hallSheduleService.findHallSheduleById(1L)).thenReturn(hallShedule);
        when(hallRowService.findRowByHall(hallShedule.getHall())).thenReturn(hallRows);
        when(ticketService.getBookedSeats(hallShedule)).thenReturn(bookedSeats);

        mockMvc.perform(get("/api/bookings/pickPlace/{scheduleId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.schedule.id").value(1L))
                .andExpect(jsonPath("$.schedule.film.id").value(1L))
                .andExpect(jsonPath("$.schedule.film.title").value("Test Film"))
                .andExpect(jsonPath("$.hallRows[0].id").value(1L));
    }

    @Test
    void testConfirmSeats_Failure() throws Exception {
        List<String> selectedSeats = List.of("1-1");

        when(hallSheduleService.findHallSheduleById(1L)).thenReturn(hallShedule);
        when(userService.findById(1L)).thenReturn(user);
        doThrow(new IllegalStateException("Место уже занято")).when(ticketService)
                .confirmSeats(any(HallShedule.class), anyList(), eq(user.getEmail()));

        mockMvc.perform(post("/api/bookings/pickPlace/{scheduleId}", 1L)
                        .param("userId", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[\"1-1\"]"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Место уже занято"));

        verify(ticketService, times(1)).confirmSeats(eq(hallShedule), anyList(), eq(user.getEmail()));
    }

}
