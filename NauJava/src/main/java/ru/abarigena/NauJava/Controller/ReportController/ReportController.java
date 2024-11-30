package ru.abarigena.NauJava.Controller.ReportController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.abarigena.NauJava.Entities.Report.Report;
import ru.abarigena.NauJava.Service.ReportService.ReportService;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Контроллер для работы с отчетами.
 * Обрабатывает запросы на создание отчетов, отображение списка отчетов,
 * просмотр подробностей отчета и удаление отчетов.
 */
@Controller
@RequestMapping("/admin/reports")
public class ReportController {
    private final ReportService reportService;

    @Autowired
    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    /**
     * Отображает список всех отчетов.
     *
     * @param model модель для передачи данных в шаблон
     * @return имя шаблона для отображения списка отчетов
     */
    @GetMapping
    public String getReportsList(Model model) {
        List<Report> reports = reportService.findAll();
        model.addAttribute("reports", reports);
        return "admin/report/reportsList";
    }

    /**
     * Отображает страницу для создания отчета.
     *
     * @return имя шаблона для страницы создания отчета
     */
    @GetMapping("/create")
    public String reportCreate() {
        return "admin/report/reportCreate";
    }

    /**
     * Создает отчет по забронированным билетам в указанном диапазоне дат.
     * После создания, перенаправляет на страницу списка отчетов с сообщением об ID созданного отчета.
     *
     * @param startDate начальная дата диапазона
     * @param endDate конечная дата диапазона
     * @param redirectAttributes атрибуты для перенаправления
     * @return перенаправление на страницу списка отчетов
     */
    @GetMapping("/booked")
    public String generateBookedTicketsReport(@RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
                                              @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
                                              RedirectAttributes redirectAttributes) {
        Long report = reportService.generatedBookedTicketsReport(startDate, endDate);
        redirectAttributes.addFlashAttribute("message", "Report ID: " + report);
        return "redirect:/admin/reports";
    }

    /**
     * Создает отчет по отмененным билетам в указанном диапазоне дат.
     * После создания, перенаправляет на страницу списка отчетов с сообщением об ID созданного отчета.
     *
     * @param startDate начальная дата диапазона
     * @param endDate конечная дата диапазона
     * @param redirectAttributes атрибуты для перенаправления
     * @return перенаправление на страницу списка отчетов
     */
    @GetMapping("/canceled")
    public String generateCanceledTicketsReport(@RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
                                                @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
                                                RedirectAttributes redirectAttributes) {
        Long report = reportService.generatedCancelledTicketsReport(startDate, endDate);
        redirectAttributes.addFlashAttribute("message", "Report ID: " + report);
        return "redirect:/admin/reports";
    }

    /**
     * Создает отчет по популярности фильмов на основе забронированных или проведенных билетов
     * в указанном диапазоне дат.
     * После создания, перенаправляет на страницу списка отчетов с сообщением об ID созданного отчета.
     *
     * @param startDate начальная дата диапазона
     * @param endDate конечная дата диапазона
     * @param redirectAttributes атрибуты для перенаправления
     * @return перенаправление на страницу списка отчетов
     */
    @GetMapping("/popularity")
    public String generateFilmPopularityReport(@RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
                                               @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
                                               RedirectAttributes redirectAttributes) {
        Long report = reportService.generatedFilmPopularityReport(startDate, endDate);
        redirectAttributes.addFlashAttribute("message", "Report ID: " + report);
        return "redirect:/admin/reports";
    }

    /**
     * Отображает отчет по его ID.
     * Если отчет в процессе создания, отображается статус "Создается".
     * Если произошла ошибка при создании отчета, отображается сообщение об ошибке.
     *
     * @param id ID отчета
     * @param model модель для передачи данных в шаблон
     * @return имя шаблона для отображения отчета
     */
    @GetMapping("/{id}")
    public String viewReport(@PathVariable("id") Long id, Model model) {
        Report report = reportService.findById(id);
        model.addAttribute("report", report);
        return "admin/report/reportView";
    }

    /**
     * Удаляет отчет по его ID.
     * После удаления, перенаправляет на страницу списка отчетов с сообщением об успешном удалении.
     *
     * @param id ID отчета
     * @param redirectAttributes атрибуты для перенаправления
     * @return перенаправление на страницу списка отчетов
     */
    @PostMapping("/delete/{id}")
    public String deleteReport(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            reportService.deleteReport(id);
            redirectAttributes.addFlashAttribute("message", "Отчет ID " + id + " Удаление успешно.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка при удалении отчета ID " + id + ": " + e.getMessage());
        }
        return "redirect:/admin/reports";
    }
}
