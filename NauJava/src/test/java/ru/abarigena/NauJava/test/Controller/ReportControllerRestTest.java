package ru.abarigena.NauJava.test.Controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.abarigena.NauJava.Controller.ReportController.ReportControllerRest;
import ru.abarigena.NauJava.Entities.Report.Report;
import ru.abarigena.NauJava.Service.ReportService.ReportService;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReportControllerRest.class)
@AutoConfigureMockMvc(addFilters = false)
class ReportControllerRestTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReportService reportService;

    private Report report;

    @BeforeEach
    void setUp() {
        report = new Report();
        report.setId(1L);
        report.setContent("content");
    }

    @Test
    void testGetReportsList() throws Exception {
        when(reportService.findAll()).thenReturn(List.of(report));

        mockMvc.perform(get("/api/reports/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].content").value("content"));
    }

    @Test
    void testGenerateBookedTicketsReport() throws Exception {
        LocalDateTime startDate = LocalDateTime.of(2024, 12, 1, 0, 0, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2024, 12, 31, 23, 59, 59, 999999);

        when(reportService.generatedBookedTicketsReport(startDate, endDate)).thenReturn(1L);

        mockMvc.perform(post("/api/reports/booked")
                        .param("startDate", startDate.toString())
                        .param("endDate", endDate.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(1L));
    }

    @Test
    void testGenerateCanceledTicketsReport() throws Exception {
        LocalDateTime startDate = LocalDateTime.of(2024, 12, 1, 0, 0, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2024, 12, 31, 23, 59, 59, 999999);

        when(reportService.generatedCancelledTicketsReport(startDate, endDate)).thenReturn(2L);

        mockMvc.perform(post("/api/reports/canceled")
                        .param("startDate", startDate.toString())
                        .param("endDate", endDate.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(2L));
    }

    @Test
    void testGenerateFilmPopularityReport() throws Exception {
        LocalDateTime startDate = LocalDateTime.of(2024, 12, 1, 0, 0, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2024, 12, 31, 23, 59, 59, 999999);

        when(reportService.generatedFilmPopularityReport(startDate, endDate)).thenReturn(3L);

        mockMvc.perform(post("/api/reports/popularity")
                        .param("startDate", startDate.toString())
                        .param("endDate", endDate.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(3L));
    }

    @Test
    void testViewReport() throws Exception {
        when(reportService.findById(1L)).thenReturn(report);

        mockMvc.perform(get("/api/reports/find/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.content").value("content"));
    }

    @Test
    void testDeleteReport() throws Exception {
        mockMvc.perform(delete("/api/reports/delete/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("Отчет ID 1 успешно удален."));
    }
}
