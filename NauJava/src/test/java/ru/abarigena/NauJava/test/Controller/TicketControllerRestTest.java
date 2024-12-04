package ru.abarigena.NauJava.test.Controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.abarigena.NauJava.Controller.TicketController.TicketControllerRest;
import ru.abarigena.NauJava.Entities.Ticket.Ticket;
import ru.abarigena.NauJava.Service.TicketService.TicketService;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TicketControllerRest.class)
@AutoConfigureMockMvc(addFilters = false)
public class TicketControllerRestTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TicketService ticketService;

    private Ticket ticket;

    @BeforeEach
    void setUp() {

        ticket = new Ticket();
        ticket.setId(1L);
        ticket.setRow(1);
        ticket.setSeat(5);
        ticket.setUser(null);
        ticket.setHallShedule(null);
    }

    @Test
    void testSearchTickets() throws Exception {
        when(ticketService.getActiveTicketsByIdentifier("testIdentifier")).thenReturn(List.of(ticket));

        mockMvc.perform(post("/api/tickets/search")
                        .param("identifier", "testIdentifier"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].row").value(1))
                .andExpect(jsonPath("$[0].seat").value(5));
    }

    @Test
    void testCancelTicket() throws Exception {
        mockMvc.perform(post("/api/tickets/cancel/{ticketId}", 1L))
                .andExpect(status().isOk());
    }
}
