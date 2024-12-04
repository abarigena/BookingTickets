package ru.abarigena.NauJava.test.Service.FilmService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.abarigena.NauJava.Entities.HallShedule.HallShedule;
import ru.abarigena.NauJava.Entities.Ticket.Ticket;
import ru.abarigena.NauJava.Entities.Ticket.TicketHistory;
import ru.abarigena.NauJava.Entities.Ticket.TicketStatus;
import ru.abarigena.NauJava.Entities.User.User;
import ru.abarigena.NauJava.Repository.TicketHistoryRepository;
import ru.abarigena.NauJava.Repository.TicketRepository;
import ru.abarigena.NauJava.Service.TicketHistoryServise.TicketHistoryServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TicketHistoryServiceTest {

    @Mock
    private TicketHistoryRepository ticketHistoryRepository;

    @Mock
    private TicketRepository ticketRepository;

    @InjectMocks
    private TicketHistoryServiceImpl ticketHistoryService;

    private Ticket ticket;

    @BeforeEach
    void setUp() {
        // Создаем новый билет
        ticket = new Ticket();
        ticket.setId(1L);
        ticket.setRow(1);
        ticket.setSeat(1);
        ticket.setHallShedule(new HallShedule());
        ticket.setUser(new User());
    }

    @Test
    void testAddTicketHistory() {
        ticketHistoryService.addTicketHistory(ticket, TicketStatus.CONFIRMED);

        verify(ticketHistoryRepository, times(1)).save(any(TicketHistory.class));
    }

    @Test
    void testAddTicketHistoryStatus() {
        TicketHistory ticketHistory = new TicketHistory();
        ticketHistory.setId(ticket.getId());
        ticketHistory.setStatus(TicketStatus.BOOKED);

        when(ticketHistoryRepository.findById(ticket.getId())).thenReturn(Optional.of(ticketHistory));

        ticketHistoryService.addTicketHistoryStatus(ticket.getId(), TicketStatus.CONDUCTED);

        assertEquals(TicketStatus.CONDUCTED, ticketHistory.getStatus());

        verify(ticketHistoryRepository, times(1)).save(ticketHistory);
    }

    @Test
    void testGetCanceledAndExpiredTicketsForUser() {
        TicketHistory ticketHistory = new TicketHistory();
        ticketHistory.setId(ticket.getId());
        ticketHistory.setStatus(TicketStatus.CANCELED);

        when(ticketHistoryRepository.findCanceledAndExpiredTicketsForUser(1L)).thenReturn(List.of(ticketHistory));

        var result = ticketHistoryService.getCanceledAndExpiredTicketsForUser(1L);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());

        verify(ticketHistoryRepository, times(1)).findCanceledAndExpiredTicketsForUser(1L);
    }
}
