package ru.abarigena.NauJava.test.Service.FilmService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.abarigena.NauJava.Entities.Film;
import ru.abarigena.NauJava.Entities.Hall;
import ru.abarigena.NauJava.Entities.HallShedule.HallShedule;
import ru.abarigena.NauJava.Entities.Report.Report;
import ru.abarigena.NauJava.Entities.Report.ReportStatus;
import ru.abarigena.NauJava.Entities.Ticket.TicketHistory;
import ru.abarigena.NauJava.Entities.Ticket.TicketStatus;
import ru.abarigena.NauJava.Entities.User.User;
import ru.abarigena.NauJava.Repository.ReportRepository;
import ru.abarigena.NauJava.Repository.TicketHistoryRepository;
import ru.abarigena.NauJava.Service.ReportService.ReportServiceImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReportServiceTest {
    @Mock
    private TicketHistoryRepository ticketHistoryRepository;

    @Mock
    private ReportRepository reportRepository;

    @InjectMocks
    private ReportServiceImpl reportService;

    private Report report;

    private HallShedule hallShedule;

    @BeforeEach
    void setUp() {
        report = new Report();
        report.setId(1L);
        report.setStatus(ReportStatus.CREATED);

        hallShedule = new HallShedule();
        hallShedule.setId(2L);
        hallShedule.setFilm(new Film());
        hallShedule.setHall(new Hall());
        hallShedule.setStartTime(LocalDateTime.of(2024, 12, 1, 1, 0));
    }

    @Test
    void testGeneratedBookedTicketsReport() {
        LocalDateTime startDate = LocalDateTime.of(2024, 12, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2024, 12, 31, 23, 59);

        TicketHistory ticket = new TicketHistory();
        ticket.setId(1L);
        ticket.setStatus(TicketStatus.BOOKED);
        ticket.setDate(LocalDateTime.of(2024, 12, 5, 10, 0));
        ticket.setUser(new User());
        ticket.setHallShedule(hallShedule);
        when(ticketHistoryRepository.findBookedTickets(startDate, endDate)).thenReturn(List.of(ticket));

        Report mockReport = new Report();
        mockReport.setId(1L);
        when(reportRepository.save(any(Report.class))).thenReturn(mockReport);

        Long reportId = reportService.generatedBookedTicketsReport(startDate, endDate);

        verify(reportRepository, times(1)).save(any(Report.class));
    }

    @Test
    void testGeneratedCancelledTicketsReport() {
        LocalDateTime startDate = LocalDateTime.of(2024, 12, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2024, 12, 31, 23, 59);

        TicketHistory ticket = new TicketHistory();
        ticket.setId(1L);
        ticket.setStatus(TicketStatus.CANCELED);
        ticket.setDate(LocalDateTime.of(2024, 12, 5, 10, 0));
        ticket.setUser(new User());
        ticket.setHallShedule(hallShedule);
        when(ticketHistoryRepository.findCanceledTickets(startDate, endDate)).thenReturn(List.of(ticket));

        Report mockReport = new Report();
        mockReport.setId(1L);
        when(reportRepository.save(any(Report.class))).thenReturn(mockReport);

        Long reportId = reportService.generatedCancelledTicketsReport(startDate, endDate);

        verify(reportRepository, times(1)).save(any(Report.class));
    }

    @Test
    void testGeneratedFilmPopularityReport() {
        LocalDateTime startDate = LocalDateTime.of(2024, 12, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2024, 12, 31, 23, 59);

        Object[] filmData = {"Film 1", 5L};

        List<Object[]> popularityData = new ArrayList<>();
        popularityData.add(filmData);

        when(ticketHistoryRepository.findFilmStatisticsByDate(startDate, endDate)).thenReturn(popularityData);

        Report mockReport = new Report();
        mockReport.setId(1L);
        when(reportRepository.save(any(Report.class))).thenReturn(mockReport);

        Long reportId = reportService.generatedFilmPopularityReport(startDate, endDate);

        verify(reportRepository, times(2)).save(any(Report.class));
    }



    @Test
    void testFindById() {
        Report mockReport = new Report();
        mockReport.setId(1L);
        mockReport.setStatus(ReportStatus.CREATED);
        when(reportRepository.findById(1L)).thenReturn(Optional.of(mockReport));

        Report foundReport = reportService.findById(1L);

        assertNotNull(foundReport);
        assertEquals(1L, foundReport.getId());
        assertEquals(ReportStatus.CREATED, foundReport.getStatus());
    }

    @Test
    void testDeleteReport() {
        Long reportId = 1L;

        doNothing().when(reportRepository).deleteById(reportId);

        reportService.deleteReport(reportId);

        verify(reportRepository, times(1)).deleteById(reportId);
    }

    @Test
    void testFindAll() {
        Report report1 = new Report();
        report1.setId(1L);
        report1.setStatus(ReportStatus.CREATED);

        Report report2 = new Report();
        report2.setId(2L);
        report2.setStatus(ReportStatus.COMPLETED);

        List<Report> reports = List.of(report1, report2);
        when(reportRepository.findAll()).thenReturn(reports);

        List<Report> allReports = reportService.findAll();

        assertNotNull(allReports);
        assertEquals(2, allReports.size());
        assertEquals(1L, allReports.get(0).getId());
        assertEquals(2L, allReports.get(1).getId());
    }


}
