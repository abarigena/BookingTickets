package ru.abarigena.NauJava.Service.TicketService;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.abarigena.NauJava.Entities.HallShedule.HallShedule;
import ru.abarigena.NauJava.Entities.Ticket.Ticket;
import ru.abarigena.NauJava.Entities.Ticket.TicketStatus;
import ru.abarigena.NauJava.Entities.User.User;
import ru.abarigena.NauJava.Repository.TicketRepository;
import ru.abarigena.NauJava.Repository.UserRepository;
import ru.abarigena.NauJava.Service.EmailService.EmailService;
import ru.abarigena.NauJava.Service.TicketHistoryServise.TicketHistoryService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@Transactional
public class TicketServiceImpl implements TicketService {
    private static final Logger logger = LoggerFactory.getLogger(TicketServiceImpl.class);

    private final TicketRepository ticketRepository;
    private final TicketHistoryService ticketHistoryService;
    private final EmailService emailService;
    private final UserRepository userRepository;

    @Autowired
    public TicketServiceImpl(TicketRepository ticketRepository, TicketHistoryService ticketHistoryService,
                             EmailService emailService, UserRepository userRepository) {
        this.ticketRepository = ticketRepository;
        this.ticketHistoryService = ticketHistoryService;
        this.emailService = emailService;
        this.userRepository = userRepository;
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
        logger.info("Билет успешно добавлен: {}", saveTicket);
        return saveTicket;
    }

    /**
     * Удаляет билет и обновляет его статус в истории.
     *
     * @param ticketId ID билета
     * @return удаленный объект {@link Ticket}
     */
    @Override
    public Ticket cancelBookTicket(Long ticketId) {
        ticketHistoryService.addTicketHistoryStatus(ticketId, TicketStatus.CANCELED);
        Ticket ticket = ticketRepository.findById(ticketId).get();
        ticketRepository.delete(ticket);
        logger.info("Бронирование билета с ID {} отменено", ticketId);
        return ticket;
    }

    /**
     * Получает список билетов для заданного расписания зала.
     *
     * @param hallShedule расписание зала
     * @return список билетов
     */
    @Override
    public List<Ticket> findByHallShedule(HallShedule hallShedule) {
        return ticketRepository.findByHallShedule(hallShedule);
    }

    /**
     * Подтверждает выбранные места и отправляет уведомление по email.
     *
     * @param shedule расписание сеанса
     * @param tickets список билетов для подтверждения
     * @param email   email пользователя
     */
    @Override
    public void confirmSeats(HallShedule shedule, List<Ticket> tickets, String email) {
        logger.info("Подтверждение мест для сеанса: {}", shedule);
        // Проверка занятых мест
        Map<Integer, List<Integer>> bookedSeats = getBookedSeats(shedule);

        for (Ticket ticket : tickets) {
            if (bookedSeats.getOrDefault(ticket.getRow(), new ArrayList<>())
                    .contains(ticket.getSeat())) {
                logger.error("Место {}-{} уже занято", ticket.getRow(), ticket.getSeat());
                throw new IllegalStateException("Место " + ticket.getRow() + "-" + ticket.getSeat() + " Уже забронировано!");
            }
        }

        tickets.forEach(this::addTicket);

        StringBuilder ticketInfo = new StringBuilder("Вы успешно забронировали билеты:\n");
        tickets.forEach(ticket -> {
            ticketInfo.append("Ряд: ").append(ticket.getRow())
                    .append(", Место: ").append(ticket.getSeat())
                    .append("\nФильм: ").append(ticket.getHallShedule().getFilm().getTitle())
                    .append("\nЗал: ").append(ticket.getHallShedule().getHall().getName())
                    .append("\nВремя показа: ").append(ticket.getHallShedule().getStartTime())
                    .append("\n\n");
        });

        CompletableFuture.runAsync(() -> {
            emailService.sendEmail(email, "Информация о забронированных билетах", ticketInfo.toString());
        });
    }

    /**
     * Получает занятые места для заданного расписания.
     *
     * @param schedule расписание зала
     * @return карта, где ключ — ряд, а значение — список занятых мест
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

    /**
     * Получает активные билеты пользователя.
     *
     * @param userId ID пользователя
     * @return список активных билетов
     */
    @Override
    public List<Ticket> getActiveTicketsByUserId(Long userId) {
        return ticketRepository.findActiveTicketsByUserId(userId);
    }

    /**
     * @param identifier
     * @return
     */
    @Override
    public List<Ticket> getActiveTicketsByIdentifier(String identifier) {
        User user = userRepository.findByUsernameOrEmailOrPhone(identifier);
        return getActiveTicketsByUserId(user.getId());
    }
}
