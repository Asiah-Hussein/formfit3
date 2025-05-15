package com.asiah.formfit.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Utility class for date operations in the Form-Fit application
 */
public class DateUtils {

    // Date format patterns
    public static final String DATE_FORMAT_DISPLAY = "MMM dd, yyyy";
    public static final String DATE_FORMAT_DATABASE = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_FORMAT_TIME_ONLY = "HH:mm:ss";

    /**
     * Format date for display in the UI
     * @param date Date to format
     * @return Formatted date string
     */
    public static String formatForDisplay(Date date) {
        if (date == null) {
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_DISPLAY, Locale.getDefault());
        return sdf.format(date);
    }

    /**
     * Format date for storing in the database
     * @param date Date to format
     * @return Formatted date string
     */
    public static String formatForDatabase(Date date) {
        if (date == null) {
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_DATABASE, Locale.getDefault());
        return sdf.format(date);
    }

    /**
     * Parse date string from database format
     * @param dateString Date string to parse
     * @return Parsed Date object
     */
    public static Date parseFromDatabase(String dateString) {
        if (dateString == null || dateString.isEmpty()) {
            return null;
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_DATABASE, Locale.getDefault());
            return sdf.parse(dateString);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * Get the start of the current day
     * @return Date at start of day
     */
    public static Date getStartOfDay() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * Get the start of the current week
     * @return Date at start of week
     */
    public static Date getStartOfWeek() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * Get the start of the current month
     * @return Date at start of month
     */
    public static Date getStartOfMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * Calculate the duration between two dates in seconds
     * @param startDate Start date
     * @param endDate End date
     * @return Duration in seconds
     */
    public static int getDurationInSeconds(Date startDate, Date endDate) {
        if (startDate == null || endDate == null) {
            return 0;
        }
        long diffInMillis = endDate.getTime() - startDate.getTime();
        return (int) TimeUnit.MILLISECONDS.toSeconds(diffInMillis);
    }

    /**
     * Format duration in seconds to a readable string (MM:SS)
     * @param durationSeconds Duration in seconds
     * @return Formatted duration string
     */
    public static String formatDuration(int durationSeconds) {
        int minutes = durationSeconds / 60;
        int seconds = durationSeconds % 60;
        return String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
    }

    /**
     * Check if a date is today
     * @param date Date to check
     * @return True if date is today
     */
    public static boolean isToday(Date date) {
        if (date == null) {
            return false;
        }
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(date);

        Calendar calendar2 = Calendar.getInstance();

        return calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR) &&
                calendar1.get(Calendar.DAY_OF_YEAR) == calendar2.get(Calendar.DAY_OF_YEAR);
    }

    /**
     * Get the number of days between two dates
     * @param startDate Start date
     * @param endDate End date
     * @return Number of days
     */
    public static int getDaysBetween(Date startDate, Date endDate) {
        if (startDate == null || endDate == null) {
            return 0;
        }
        long diffInMillis = Math.abs(endDate.getTime() - startDate.getTime());
        return (int) TimeUnit.MILLISECONDS.toDays(diffInMillis);
    }
}
