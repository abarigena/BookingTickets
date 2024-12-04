package ru.abarigena.NauJava.test.Service.FilmService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.abarigena.NauJava.Entities.HallShedule.HallShedule;
import ru.abarigena.NauJava.Entities.Ticket.Ticket;
import ru.abarigena.NauJava.Entities.Ticket.TicketStatus;
import ru.abarigena.NauJava.Repository.TicketRepository;
import ru.abarigena.NauJava.Service.TicketHistoryServise.TicketHistoryService;
import ru.abarigena.NauJava.Service.TicketService.TicketServiceImpl;


import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TicketServiceTest {

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private TicketHistoryService ticketHistoryService;

    @Spy
    @InjectMocks
    private TicketServiceImpl ticketService;

    @Test
    void testAddTicket() {
        Ticket ticket = new Ticket();
        ticket.setId(1L);

        when(ticketRepository.save(any(Ticket.class))).thenReturn(ticket);

        doNothing().when(ticketHistoryService).addTicketHistory(any(Ticket.class), any(TicketStatus.class));

        Ticket savedTicket = ticketService.addTicket(ticket);

        assertNotNull(savedTicket);
        assertEquals(ticket.getId(), savedTicket.getId());
        verify(ticketRepository, times(1)).save(any(Ticket.class));
        verify(ticketHistoryService, times(1)).addTicketHistory(any(Ticket.class), any(TicketStatus.class));
    }

    @Test
    void testCancelBookTicket() {
        Long ticketId = 1L;
        Ticket ticket = new Ticket();
        ticket.setId(ticketId);

        when(ticketRepository.findById(ticketId)).thenReturn(Optional.of(ticket));
        doNothing().when(ticketHistoryService).addTicketHistoryStatus(ticketId, TicketStatus.CANCELED);

        Ticket canceledTicket = ticketService.cancelBookTicket(ticketId);

        assertNotNull(canceledTicket);
        verify(ticketRepository, times(1)).findById(ticketId);
        verify(ticketHistoryService, times(1)).addTicketHistoryStatus(ticketId, TicketStatus.CANCELED);
        verify(ticketRepository, times(1)).delete(ticket);
    }

    @Test
    void testGetBookedSeats() {
        HallShedule hallShedule = new HallShedule(); // Создаем объект расписания зала
        Ticket ticket1 = new Ticket();
        ticket1.setRow(1);
        ticket1.setSeat(1);
        Ticket ticket2 = new Ticket();
        ticket2.setRow(1);
        ticket2.setSeat(2);

        List<Ticket> bookedTickets = List.of(ticket1, ticket2);

        when(ticketRepository.findByHallShedule(hallShedule)).thenReturn(bookedTickets);

        Map<Integer, List<Integer>> bookedSeats = ticketService.getBookedSeats(hallShedule);

        assertNotNull(bookedSeats);
        assertEquals(1, bookedSeats.size());
        assertTrue(bookedSeats.containsKey(1));
        assertTrue(bookedSeats.get(1).contains(1));
        assertTrue(bookedSeats.get(1).contains(2));
    }

    @Test
    void testGetActiveTicketsByUserId() {
        Long userId = 1L;
        Ticket ticket1 = new Ticket();

        List<Ticket> tickets = List.of(ticket1);

        when(ticketRepository.findActiveTicketsByUserId(userId)).thenReturn(tickets);

        List<Ticket> activeTickets = ticketService.getActiveTicketsByUserId(userId);

        assertNotNull(activeTickets);
        assertFalse(activeTickets.isEmpty());
        assertEquals(tickets.size(), activeTickets.size());
    }

}
