package com.tontine.app.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;

/**
 * Utility class for date operations.
 */
public final class DateUtils {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

    private DateUtils() {
        // Private constructor to prevent instantiation
    }

    /**
     * Parses a date string in the format "yyyyMMdd" to a LocalDate.
     *
     * @param date the date string to parse
     * @return an Optional containing the parsed LocalDate, or empty if parsing fails
     */
    public static Optional<LocalDate> parseDate(String date) {
        try {
            return Optional.of(LocalDate.parse(date, DATE_FORMATTER));
        } catch (DateTimeParseException e) {
            return Optional.empty();
        }
    }

    /**
     * Parses a date string in the format "yyyyMMdd" to a LocalDate, throwing an exception if parsing fails.
     *
     * @param date the date string to parse
     * @return the parsed LocalDate
     * @throws IllegalArgumentException if the date string is not in the expected format
     */
    public static LocalDate parseDateOrThrow(String date) {
        return parseDate(date).orElseThrow(
            () -> new IllegalArgumentException("Invalid date format yyyyMMdd: " + date)
        );
    }
}
