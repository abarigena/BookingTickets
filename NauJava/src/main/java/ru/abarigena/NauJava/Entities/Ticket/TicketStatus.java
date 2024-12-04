package ru.abarigena.NauJava.Entities.Ticket;

/**
 * Перечисление статусов истории билетов.
 */
public enum TicketStatus {
    /**
     * Билет забронирован.
     */
    BOOKED,

    /**
     * Бронь билета отменена.
     */
    CANCELED,

    CONFIRMED,
    /**
     * Билет использован.
     */
    CONDUCTED
}
