package ru.abarigena.NauJava.test;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.abarigena.NauJava.Entities.Ticket.Ticket;
import ru.abarigena.NauJava.Service.TicketService.TicketService;

@SpringBootTest
class TicketAdd {
    private final TicketService ticketService;

    @Autowired
    public TicketAdd(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @Test
    void testTicketHistory(){
        Ticket ticket = new Ticket();
        ticket.setRow(10);
        ticket.setSeat(10);
        ticketService.addTicket(ticket);

    }
}
