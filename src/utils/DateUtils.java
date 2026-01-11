package utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Utility class for date formatting and parsing operations.
 */
public class DateUtils {

    // Common date formatters
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    public static final DateTimeFormatter DATE_DISPLAY_FORMATTER = DateTimeFormatter.ofPattern("dd MMM yyyy");
    public static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("hh:mm a");
    public static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm a");
    public static final DateTimeFormatter DATETIME_DISPLAY_FORMATTER = DateTimeFormatter
            .ofPattern("dd MMM yyyy, hh:mm a");

    /**
     * Format LocalDate to display string
     * 
     * @param date Date to format
     * @return Formatted string
     */
    public static String formatDate(LocalDate date) {
        if (date == null)
            return "";
        return date.format(DATE_DISPLAY_FORMATTER);
    }

    /**
     * Format LocalDateTime to display string
     * 
     * @param dateTime DateTime to format
     * @return Formatted string
     */
    public static String formatDateTime(LocalDateTime dateTime) {
        if (dateTime == null)
            return "";
        return dateTime.format(DATETIME_DISPLAY_FORMATTER);
    }

    /**
     * Format LocalDateTime to time only
     * 
     * @param dateTime DateTime to format
     * @return Formatted time string
     */
    public static String formatTime(LocalDateTime dateTime) {
        if (dateTime == null)
            return "";
        return dateTime.format(TIME_FORMATTER);
    }

    /**
     * Parse date string to LocalDate
     * 
     * @param dateString Date string in dd/MM/yyyy format
     * @return Parsed LocalDate or null if invalid
     */
    public static LocalDate parseDate(String dateString) {
        if (dateString == null || dateString.trim().isEmpty()) {
            return null;
        }
        try {
            return LocalDate.parse(dateString.trim(), DATE_FORMATTER);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    /**
     * Parse datetime string to LocalDateTime
     * 
     * @param dateTimeString DateTime string
     * @return Parsed LocalDateTime or null if invalid
     */
    public static LocalDateTime parseDateTime(String dateTimeString) {
        if (dateTimeString == null || dateTimeString.trim().isEmpty()) {
            return null;
        }
        try {
            return LocalDateTime.parse(dateTimeString.trim(), DATETIME_FORMATTER);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    /**
     * Get current date as formatted string
     * 
     * @return Current date string
     */
    public static String getCurrentDateString() {
        return LocalDate.now().format(DATE_DISPLAY_FORMATTER);
    }

    /**
     * Get current datetime as formatted string
     * 
     * @return Current datetime string
     */
    public static String getCurrentDateTimeString() {
        return LocalDateTime.now().format(DATETIME_DISPLAY_FORMATTER);
    }

    /**
     * Check if a date is today
     * 
     * @param date Date to check
     * @return true if date is today
     */
    public static boolean isToday(LocalDate date) {
        return date != null && date.equals(LocalDate.now());
    }

    /**
     * Check if a datetime is in the past
     * 
     * @param dateTime DateTime to check
     * @return true if in the past
     */
    public static boolean isPast(LocalDateTime dateTime) {
        return dateTime != null && dateTime.isBefore(LocalDateTime.now());
    }

    /**
     * Check if a datetime is in the future
     * 
     * @param dateTime DateTime to check
     * @return true if in the future
     */
    public static boolean isFuture(LocalDateTime dateTime) {
        return dateTime != null && dateTime.isAfter(LocalDateTime.now());
    }
}
