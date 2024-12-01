package ru.abarigena.NauJava.Service.ReportService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.abarigena.NauJava.Entities.Report.Report;
import ru.abarigena.NauJava.Entities.Report.ReportStatus;
import ru.abarigena.NauJava.Entities.Ticket.TicketHistory;
import ru.abarigena.NauJava.Repository.ReportRepository;
import ru.abarigena.NauJava.Repository.TicketHistoryRepository;
import ru.abarigena.NauJava.Service.TicketService.TicketServiceImpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.CompletableFuture;

@Service
public class ReportServiceImpl implements ReportService {
    private static final Logger logger = LoggerFactory.getLogger(TicketServiceImpl.class);
    private final TicketHistoryRepository ticketHistoryRepository;
    private final ReportRepository reportRepository;

    @Autowired
    public ReportServiceImpl(TicketHistoryRepository ticketHistoryRepository, ReportRepository reportRepository) {
        this.ticketHistoryRepository = ticketHistoryRepository;
        this.reportRepository = reportRepository;
    }

    /**
     * Генерирует отчет по забронированным билетам в указанном диапазоне дат.
     * Отчет генерируется асинхронно.
     *
     * @param startDate начальная дата диапазона
     * @param endDate конечная дата диапазона
     * @return ID сгенерированного отчета
     */
    @Override
    public Long generatedBookedTicketsReport(LocalDateTime startDate, LocalDateTime endDate) {
        Report report = new Report();
        report.setStatus(ReportStatus.CREATED);
        reportRepository.save(report);
        logger.info("Создание отчета о забронированных билетах с диапазоном: {} - {}", startDate, endDate);

        CompletableFuture.runAsync(() -> {
            try {
                List<TicketHistory> tickets = ticketHistoryRepository.findBookedTickets(startDate, endDate);
                StringBuilder content = new StringBuilder("Забронированные билеты:\n");
                tickets.forEach(ticket -> content.append(ticket.toString()).append("\n"));

                report.setContent(content.toString());
                report.setStatus(ReportStatus.COMPLETED);
                logger.info("Отчет о забронированных билетах с диапазоном: {} - {} успешно сгенерирован", startDate, endDate);
            } catch (Exception e) {
                report.setStatus(ReportStatus.ERROR);
                report.setContent("Ошибка: " + e.getMessage());
                logger.error("Ошибка при генерации отчета о забронированных билетах с диапазоном: {} - {}", startDate, endDate, e);
            }
            reportRepository.save(report);
        });

        return report.getId();
    }

    /**
     * Генерирует отчет по отмененным билетам в указанном диапазоне дат.
     * Отчет генерируется асинхронно.
     *
     * @param startDate начальная дата диапазона
     * @param endDate конечная дата диапазона
     * @return ID сгенерированного отчета
     */
    @Override
    public Long generatedCancelledTicketsReport(LocalDateTime startDate, LocalDateTime endDate) {
        Report report = new Report();
        report.setStatus(ReportStatus.CREATED);
        reportRepository.save(report);
        logger.info("Создание отчета по отмененным билетам с диапазоном: {} - {}", startDate, endDate);

        CompletableFuture.runAsync(() -> {
            try {
                List<TicketHistory> tickets = ticketHistoryRepository.findCanceledTickets(startDate, endDate);
                StringBuilder content = new StringBuilder("Отмененные билеты:\n");
                tickets.forEach(ticket -> content.append(ticket.toString()).append("\n"));

                report.setContent(content.toString());
                report.setStatus(ReportStatus.COMPLETED);
                logger.info("Отчет по отмененным билетам с диапазоном: {} - {} успешно сгенерирован", startDate, endDate);
            } catch (Exception e) {
                report.setStatus(ReportStatus.ERROR);
                report.setContent("Ошибка: " + e.getMessage());
                logger.error("Ошибка при генерации отчета по отмененным билетам с диапазоном: {} - {}", startDate, endDate, e);
            }
            reportRepository.save(report);
        });

        return report.getId();
    }

    /**
     * Генерирует отчет по популярности фильмов на основе количества билетов,
     * забронированных или проведенных в указанном диапазоне дат.
     * Отчет генерируется асинхронно.
     *
     * @param startDate начальная дата диапазона
     * @param endDate конечная дата диапазона
     * @return ID сгенерированного отчета
     */
    @Override
    public Long generatedFilmPopularityReport(LocalDateTime startDate, LocalDateTime endDate) {
        Report report = new Report();
        report.setStatus(ReportStatus.CREATED);
        reportRepository.save(report);
        logger.info("Создание отчета по популярности фильмов с диапазоном: {} - {}", startDate, endDate);

        CompletableFuture.runAsync(() -> {
            try {
                List<Object[]> popularityData = ticketHistoryRepository.findFilmStatisticsByDate(startDate, endDate);
                StringBuilder content = new StringBuilder("Популярность фильмов:\n");
                popularityData.forEach(row -> {
                    String filmTitle = (String) row[0];
                    Long bookedCount = (Long) row[1];
                    content.append("Фильм: ").append(filmTitle)
                            .append(", Забронировано билетов: ").append(bookedCount).append("\n");
                });

                report.setContent(content.toString());
                report.setStatus(ReportStatus.COMPLETED);
                logger.info("Отчет по популярности фильмов с диапазоном: {} - {} успешно сгенерирован", startDate, endDate);
            } catch (Exception e) {
                report.setStatus(ReportStatus.ERROR);
                report.setContent("Ошибка: " + e.getMessage());
                logger.error("Ошибка при генерации отчета по популярности фильмов с диапазоном: {} - {}", startDate, endDate, e);
            }
            reportRepository.save(report);
        });

        return report.getId();
    }

    /**
     * Находит отчет по его ID.
     *
     * @param id ID отчета
     * @return найденный отчет
     * @throws NoSuchElementException если отчет не найден
     */
    @Override
    public Report findById(Long id) {
        return reportRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Report not found"));
    }

    /**
     * Удаляет отчет по его ID.
     *
     * @param id ID отчета
     */
    @Override
    public void deleteReport(Long id) {
        logger.info("Удаление отчета с ID: {}", id);
        reportRepository.deleteById(id);
    }

    /**
     * Находит все отчеты.
     *
     * @return список всех отчетов
     */
    @Override
    public List<Report> findAll() {
        return (List<Report>) reportRepository.findAll();
    }
}
