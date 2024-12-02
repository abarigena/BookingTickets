package ru.abarigena.NauJava.Controller.ReportController;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.abarigena.NauJava.Entities.Report.Report;
import ru.abarigena.NauJava.Service.ReportService.ReportService;

import java.time.LocalDateTime;
import java.util.List;

/**
 * REST-контроллер для управления отчетами.
 */
@RestController
@RequestMapping("/api/reports")
public class ReportControllerRest {
    private final ReportService reportService;

    public ReportControllerRest(ReportService reportService) {
        this.reportService = reportService;
    }

    /**
     * Возвращает список всех отчетов.
     *
     * @return Список отчетов.
     */
    @GetMapping("/all")
    public ResponseEntity<List<Report>> getReportsList() {
        List<Report> reports = reportService.findAll();
        return ResponseEntity.ok(reports);
    }

    /**
     * Генерирует отчет о проданных билетах за указанный период.
     *
     * @param startDate Начальная дата.
     * @param endDate   Конечная дата.
     * @return ID созданного отчета.
     */
    @PostMapping("/booked")
    public ResponseEntity<Long> generateBookedTicketsReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        Long reportId = reportService.generatedBookedTicketsReport(startDate, endDate);
        return ResponseEntity.ok(reportId);
    }

    /**
     * Генерирует отчет об отмененных билетах за указанный период.
     *
     * @param startDate Начальная дата.
     * @param endDate   Конечная дата.
     * @return ID созданного отчета.
     */
    @PostMapping("/canceled")
    public ResponseEntity<Long> generateCanceledTicketsReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        Long reportId = reportService.generatedCancelledTicketsReport(startDate, endDate);
        return ResponseEntity.ok(reportId);
    }

    /**
     * Генерирует отчет о популярности фильмов за указанный период.
     *
     * @param startDate Начальная дата.
     * @param endDate   Конечная дата.
     * @return ID созданного отчета.
     */
    @PostMapping("/popularity")
    public ResponseEntity<Long> generateFilmPopularityReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        Long reportId = reportService.generatedFilmPopularityReport(startDate, endDate);
        return ResponseEntity.ok(reportId);
    }

    /**
     * Возвращает данные конкретного отчета.
     *
     * @param id ID отчета.
     * @return Найденный отчет.
     */
    @GetMapping("/find/{id}")
    public ResponseEntity<Report> viewReport(@PathVariable Long id) {
        Report report = reportService.findById(id);
        return ResponseEntity.ok(report);
    }

    /**
     * Удаляет отчет по его ID.
     *
     * @param id ID отчета.
     * @return Сообщение об успешном удалении.
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteReport(@PathVariable Long id) {
        reportService.deleteReport(id);
        return ResponseEntity.ok("Отчет ID " + id + " успешно удален.");
    }
}
