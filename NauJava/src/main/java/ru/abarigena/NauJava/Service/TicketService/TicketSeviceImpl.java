package ru.abarigena.NauJava.Service.TicketService;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.abarigena.NauJava.Entities.HallShedule;
import ru.abarigena.NauJava.Entities.Ticket.Ticket;
import ru.abarigena.NauJava.Entities.Ticket.TicketStatus;
import ru.abarigena.NauJava.Repository.TicketRepository;
import ru.abarigena.NauJava.Service.EmailService.EmailService;
import ru.abarigena.NauJava.Service.TicketHistoryServise.TicketHistoryService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@Transactional
public class TicketSeviceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    private final TicketHistoryService ticketHistoryService;
    private final EmailService emailService;

    @Autowired
    public TicketSeviceImpl(TicketRepository ticketRepository, TicketHistoryService ticketHistoryService,
                            EmailService emailService) {
        this.ticketRepository = ticketRepository;
        this.ticketHistoryService = ticketHistoryService;
        this.emailService = emailService;
    }

    /**
     * Добавляет новый билет.
     *
     * @param ticket объект {@link Ticket}
     * @return сохраненный объект {@link Ticket}
     */
    @Override
    public Ticket addTicket(Ticket ticket) {
        Ticket saveTicket = ticketRepository.save(ticket);

        ticketHistoryService.addTicketHistory(saveTicket, TicketStatus.BOOKED);
        return saveTicket;
    }

    /**
     * Удаляет билет и обновляет его статус в истории.
     *
     * @param ticketId ID билета
     * @return удаленный объект {@link Ticket}
     */
    @Override
    public Ticket deleteTicket(Long ticketId) {
        ticketHistoryService.addTicketHistoryStatus(ticketId, TicketStatus.CANCELED);
        Ticket ticket = ticketRepository.findById(ticketId).get();
        ticketRepository.delete(ticket);
        return ticket;
    }

    /**
     * @param hallShedule
     * @return
     */
    @Override
    public List<Ticket> findByHallShedule(HallShedule hallShedule) {
        return ticketRepository.findByHallShedule(hallShedule);
    }

    /**
     * @param shedule
     * @param tickets
     * @param email
     */
    @Override
    public void confirmSeats(HallShedule shedule, List<Ticket> tickets, String email) {
        // Проверка занятых мест
        Map<Integer, List<Integer>> bookedSeats = getBookedSeats(shedule);

        for (Ticket ticket : tickets) {
            if (bookedSeats.getOrDefault(ticket.getRow(), new ArrayList<>())
                    .contains(ticket.getSeat())) {
                throw new IllegalStateException("Seat " + ticket.getRow() + "-" + ticket.getSeat() + " is already booked!");
            }
        }

        // Сохранение билетов
        tickets.forEach(this::addTicket);

        // Формируем информацию о билете, включая время, зал, фильм
        StringBuilder ticketInfo = new StringBuilder("Вы успешно забронировали билеты:\n");
        tickets.forEach(ticket -> {
            ticketInfo.append("Ряд: ").append(ticket.getRow())
                    .append(", Место: ").append(ticket.getSeat())
                    .append("\nФильм: ").append(ticket.getHallShedule().getFilm().getTitle())
                    .append("\nЗал: ").append(ticket.getHallShedule().getHall().getName())
                    .append("\nВремя показа: ").append(ticket.getHallShedule().getStartTime())
                    .append("\n\n");
        });

        // Асинхронная отправка email
        CompletableFuture.runAsync(() -> {
            emailService.sendEmail(email, "Ticket Booking Confirmation", ticketInfo.toString());
        });
    }

    /**
     * @param schedule
     * @return
     */
    @Override
    public Map<Integer, List<Integer>> getBookedSeats(HallShedule schedule) {
        List<Ticket> bookedTickets = ticketRepository.findByHallShedule(schedule); // Получаем все занятые места
        return bookedTickets.stream()
                .collect(Collectors.groupingBy(
                        Ticket::getRow,
                        Collectors.mapping(Ticket::getSeat, Collectors.toList())
                ));
    }
}
