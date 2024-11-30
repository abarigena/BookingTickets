package ru.abarigena.NauJava.Entities.Report;

/**
 * Перечисление статусов отчета.
 */
public enum ReportStatus {
    /**
     * Отчет создается.
     */
    CREATED,
    /**
     * Отчет успешно завершен.
     */
    COMPLETED,
    /**
     * Произошла ошибка при формировании отчета.
     */
    ERROR
}
