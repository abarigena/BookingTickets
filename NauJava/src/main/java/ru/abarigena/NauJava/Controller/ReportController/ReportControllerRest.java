package ru.abarigena.NauJava.Controller.ReportController;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.abarigena.NauJava.Entities.Report.Report;
import ru.abarigena.NauJava.Service.ReportService.ReportService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/reports")
public class ReportControllerRest {
    private final ReportService reportService;

    public ReportControllerRest(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Report>> getReportsList() {
        List<Report> reports = reportService.findAll();
        return ResponseEntity.ok(reports);
    }

    @PostMapping("/booked")
    public ResponseEntity<Long> generateBookedTicketsReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        Long reportId = reportService.generatedBookedTicketsReport(startDate, endDate);
        return ResponseEntity.ok(reportId);
    }

    @PostMapping("/canceled")
    public ResponseEntity<Long> generateCanceledTicketsReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        Long reportId = reportService.generatedCancelledTicketsReport(startDate, endDate);
        return ResponseEntity.ok(reportId);
    }

    @PostMapping("/popularity")
    public ResponseEntity<Long> generateFilmPopularityReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        Long reportId = reportService.generatedFilmPopularityReport(startDate, endDate);
        return ResponseEntity.ok(reportId);
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<Report> viewReport(@PathVariable Long id) {
        Report report = reportService.findById(id);
        return ResponseEntity.ok(report);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteReport(@PathVariable Long id) {
        reportService.deleteReport(id);
        return ResponseEntity.ok("Отчет ID " + id + " успешно удален.");
    }
}
