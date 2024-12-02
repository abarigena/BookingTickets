package ru.abarigena.NauJava.Service.ReportService;

import ru.abarigena.NauJava.Entities.Report.Report;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Сервис для создания и получения отчетов.
 */
public interface ReportService {
    /**
     * Генерирует отчет о забронированных билетах в заданном промежутке времени.
     *
     * @param startDate начальная дата для отчета
     * @param endDate конечная дата для отчета
     * @return идентификатор сгенерированного отчета
     */
    Long generatedBookedTicketsReport(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Генерирует отчет о отмененных билетах в заданном промежутке времени.
     *
     * @param startDate начальная дата для отчета
     * @param endDate конечная дата для отчета
     * @return идентификатор сгенерированного отчета
     */
    Long generatedCancelledTicketsReport(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Генерирует отчет о популярности фильмов в заданном промежутке времени.
     *
     * @param startDate начальная дата для отчета
     * @param endDate конечная дата для отчета
     * @return идентификатор сгенерированного отчета
     */
    Long generatedFilmPopularityReport(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Находит отчет по его идентификатору.
     *
     * @param id идентификатор отчета
     * @return найденный отчет
     */
    Report findById(Long id);

    /**
     * Удаляет отчет по его идентификатору.
     *
     * @param id идентификатор отчета
     */
    void deleteReport(Long id);

    /**
     * Находит все отчеты.
     *
     * @return список всех отчетов
     */
    List<Report> findAll();
}
