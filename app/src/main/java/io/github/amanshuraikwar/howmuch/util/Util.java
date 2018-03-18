package io.github.amanshuraikwar.howmuch.util;

import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalTime;
import org.threeten.bp.OffsetDateTime;
import org.threeten.bp.format.DateTimeFormatter;

/**
 * Created by amanshuraikwar on 09/03/18.
 */

public class Util {

    public static boolean areSameDate(OffsetDateTime dateTime1, OffsetDateTime dateTime2) {
        return formatDate(dateTime1).equals(formatDate(dateTime2));
    }

    public static boolean isAmountValid(String amount) {
        return ! ( amount.equals("") || amount.equals("0") );
    }

    public static OffsetDateTime getCurDateTime() {
        return OffsetDateTime.now();
    }

    public static LocalTime getCurTime() {
        return LocalTime.now();
    }

    public static String formatTime(OffsetDateTime dateTime) {
        return dateTime.toLocalTime().format(DateTimeFormatter.ofPattern("h:mm a"));
    }

    public static String formatDate(OffsetDateTime offsetDateTime) {
        LocalDate localDate = offsetDateTime.toLocalDate();
        if (localDate.equals(LocalDate.now())) {
            return "Today";
        } else if (localDate.equals(LocalDate.now().minusDays(1))) {
            return "Yesterday";
        } else {
            return localDate.format(DateTimeFormatter.ofPattern("d MMM yyyy"));
        }
    }

    public static String formatAmount(int amount) {
        return format(amount);
    }

    // borrowed from https://stackoverflow.com/a/30770931/5011101
    private static final char[] SUFFIXES = {'k', 'm', 'g', 't', 'p', 'e' };

    private static String format(long number) {
        if(number < 1000) {
            // No need to format this
            return String.valueOf(number);
        }
        // Convert to a string
        final String string = String.valueOf(number);
        // The suffix we're using, 1-based
        final int magnitude = (string.length() - 1) / 3;
        // The number of digits we must show before the prefix
        final int digits = (string.length() - 1) % 3 + 1;

        // Build the string
        char[] value = new char[4];
        for(int i = 0; i < digits; i++) {
            value[i] = string.charAt(i);
        }
        int valueLength = digits;
        // Can and should we add a decimal point and an additional number?
        if(digits == 1 && string.charAt(1) != '0') {
            value[valueLength++] = '.';
            value[valueLength++] = string.charAt(1);
        }
        value[valueLength++] = SUFFIXES[magnitude - 1];
        return new String(value, 0, valueLength);
    }
}