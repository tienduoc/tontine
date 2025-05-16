package com.tontine.app.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

/**
 * Utility class for date operations.
 */
public final class DateUtils {

    // Supported date formats: yyyy-MM-dd, dd/MM/yyyy, MM-dd-yyyy, yyyyMMdd
    private static final List<DateTimeFormatter> SUPPORTED_FORMATTERS = List.of(
            DateTimeFormatter.ISO_LOCAL_DATE,                          // yyyy-MM-dd
            DateTimeFormatter.ofPattern("dd/MM/yyyy"),                 // dd/MM/yyyy
            DateTimeFormatter.ofPattern("MM-dd-yyyy"),                 // MM-dd-yyyy
            DateTimeFormatter.ofPattern("yyyyMMdd")                    // yyyyMMdd
    );

    private DateUtils() {
        // Private constructor to prevent instantiation
    }

    /**
     * Parses a date string using supported formats:
     * <ul>
     *     <li>yyyy-MM-dd</li>
     *     <li>dd/MM/yyyy</li>
     *     <li>MM-dd-yyyy</li>
     *     <li>yyyyMMdd</li>
     * </ul>
     *
     * @param dateStr the date string to parse
     * @return an Optional containing the parsed LocalDate, or empty if parsing fails
     */
    public static Optional<LocalDate> parseDate(String dateStr) {
        for (DateTimeFormatter formatter : SUPPORTED_FORMATTERS) {
            try {
                return Optional.of(LocalDate.parse(dateStr, formatter));
            } catch (DateTimeParseException ignored) {
                // continue to next formatter
            }
        }
        return Optional.empty();
    }

    /**
     * Parses a date string using supported formats and returns the LocalDate.
     * Throws an IllegalArgumentException if parsing fails.
     *
     * @param date the date string to parse
     * @return the parsed LocalDate
     * @throws IllegalArgumentException if the date string cannot be parsed using supported formats
     */
    public static LocalDate parseDateOrThrow(String date) {
        return parseDate(date).orElseThrow(
                () -> new IllegalArgumentException("Invalid date format: " + date)
        );
    }
}
