package ru.abarigena.NauJava.test.Controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.abarigena.NauJava.Controller.TicketController.TicketControllerViewRest;
import ru.abarigena.NauJava.Entities.Ticket.Ticket;
import ru.abarigena.NauJava.Entities.Ticket.TicketHistory;
import ru.abarigena.NauJava.Service.TicketHistoryServise.TicketHistoryService;
import ru.abarigena.NauJava.Service.TicketService.TicketService;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.hasSize;

@WebMvcTest(TicketControllerViewRest.class)
@AutoConfigureMockMvc(addFilters = false)
class TicketControllerViewRestTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TicketService ticketService;

    @MockBean
    private TicketHistoryService ticketHistoryService;

    @Test
    void testGetActiveTickets() throws Exception {
        Long userId = 1L;
        Ticket ticket1 = new Ticket();
        Ticket ticket2 = new Ticket();
        List<Ticket> activeTickets = Arrays.asList(ticket1, ticket2);

        when(ticketService.getActiveTicketsByUserId(userId)).thenReturn(activeTickets);

        mockMvc.perform(get("/api/ticketsView/active")
                        .param("userId", String.valueOf(userId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0]").exists())
                .andExpect(jsonPath("$[1]").exists());
    }

    @Test
    void testGetTicketHistory() throws Exception {
        Long userId = 1L;
        TicketHistory history1 = new TicketHistory();
        TicketHistory history2 = new TicketHistory();
        List<TicketHistory> historyList = Arrays.asList(history1, history2);

        when(ticketHistoryService.getCanceledAndExpiredTicketsForUser(userId)).thenReturn(historyList);

        mockMvc.perform(get("/api/ticketsView/history")
                        .param("userId", String.valueOf(userId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0]").exists())
                .andExpect(jsonPath("$[1]").exists());
    }

    @Test
    void testCancelTicket() throws Exception {
        Long ticketId = 1L;
        Ticket ticket = new Ticket();

        when(ticketService.cancelBookTicket(ticketId)).thenReturn(ticket);

        mockMvc.perform(post("/api/ticketsView/cancel/{ticketId}", ticketId))
                .andExpect(status().isOk())  // Статус 200 OK
                .andExpect(content().string("Билет успешно отменен"));
    }
}
