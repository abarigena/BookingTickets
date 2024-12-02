package ru.abarigena.NauJava.Service.TicketService;

import ru.abarigena.NauJava.Entities.HallShedule;
import ru.abarigena.NauJava.Entities.Ticket.Ticket;

import java.util.List;
import java.util.Map;

/**
 * Сервис для управления билетами.
 */
public interface TicketService {
    /**
     * Добавляет новый билет.
     *
     * @param ticket объект {@link Ticket}, который нужно добавить
     * @return добавленный билет
     */
    Ticket addTicket(Ticket ticket);

    /**
     * Отменяет бронирование билета по его идентификатору.
     *
     * @param ticketId идентификатор билета
     * @return отмененный билет
     */
    Ticket cancelBookTicket(Long ticketId);

    /**
     * Находит все билеты для указанного расписания зала.
     *
     * @param hallShedule расписание зала
     * @return список билетов для указанного расписания
     */
    List<Ticket> findByHallShedule(HallShedule hallShedule);

    /**
     * Подтверждает выбранные места для указанного расписания и отправляет подтверждение на email.
     *
     * @param shedule расписание зала
     * @param tickets список билетов
     * @param email адрес электронной почты для подтверждения
     */
    void confirmSeats(HallShedule shedule, List<Ticket> tickets, String email);

    /**
     * Получает карту занятых мест для указанного расписания зала.
     *
     * @param schedule расписание зала
     * @return карта занятых мест, где ключ - номер ряда, значение - список занятых мест
     */
    Map<Integer, List<Integer>> getBookedSeats(HallShedule schedule);

    /**
     * Возвращает список активных билетов пользователя по его идентификатору.
     *
     * @param userId идентификатор пользователя
     * @return список активных билетов
     */
    List<Ticket> getActiveTicketsByUserId(Long userId);

    /**
     * Возвращает список активных билетов пользователя по идентификатору (логин, email или телефон).
     *
     * @param identifier логин, email или телефон пользователя
     * @return список активных билетов
     */
    List<Ticket> getActiveTicketsByIdentifier(String identifier);
}
