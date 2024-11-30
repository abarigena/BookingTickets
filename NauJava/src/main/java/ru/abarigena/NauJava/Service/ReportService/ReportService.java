package ru.abarigena.NauJava.Service.ReportService;

import ru.abarigena.NauJava.Entities.Report.Report;

import java.time.LocalDateTime;
import java.util.List;

public interface ReportService {
    Long generatedBookedTicketsReport(LocalDateTime startDate, LocalDateTime endDate);

    Long generatedCancelledTicketsReport(LocalDateTime startDate, LocalDateTime endDate);

    Long generatedFilmPopularityReport(LocalDateTime startDate, LocalDateTime endDate);

    Report findById(Long id);

    void deleteReport(Long id);

    List<Report> findAll();
}
