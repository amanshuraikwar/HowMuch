package io.github.amanshuraikwar.howmuch.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.annotation.Nonnull;

import io.github.amanshuraikwar.howmuch.Constants;
import io.github.amanshuraikwar.howmuch.data.network.sheets.SpreadSheetException;
import kotlin.jvm.Throws;

/**
 * Utility class for the app.
 *
 * @author Amanshu Raikwar
 * Created by Amanshu Raikwar on 07/03/18.
 */

public class Util {

    @Nonnull
    public static String getTag(Object object) {
        return object.getClass().getSimpleName();
    }

    public static String getTag(Class cls) {
        return cls.getClass().getSimpleName();
    }

    public static int getHourOfDay() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.HOUR_OF_DAY);
    }

    public static int getMinute() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.MINUTE);
    }

    public static int getDayOfMonth() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.DAY_OF_MONTH);
    }

    public static int getMonthOfYear() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.MONTH);
    }

    public static int getYear() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.YEAR);
    }

    public static String getDate(int dayOfMonth, int monthOfYear, int year) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, monthOfYear, dayOfMonth);
        return new SimpleDateFormat("dd-MM-yyyy", Locale.UK).format(cal.getTime());
    }

    public static String getTime(int minute, int hourOfDay) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MINUTE, minute);
        cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
        return new SimpleDateFormat("HH:mm:ss", Locale.UK).format(cal.getTime());
    }

    public static String getCurMonth() {
        Calendar cal = Calendar.getInstance();
        return new SimpleDateFormat("MMMM", Locale.UK).format(cal.getTime());
    }

    public static int getCurMonthNumber() {
        Calendar cal = Calendar.getInstance();
        return Integer.parseInt(new SimpleDateFormat("MM", Locale.UK).format(cal.getTime()));
    }

    public static int getMonthNumber(String dateStr) throws ParseException {
        Date date = new SimpleDateFormat("dd-MM-yyyy", Locale.UK).parse(dateStr);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return Integer.parseInt(new SimpleDateFormat("MM", Locale.UK).format(cal.getTime()));
    }

    public static int getCurYearNumber() {
        Calendar cal = Calendar.getInstance();
        return Integer.parseInt(new SimpleDateFormat("YYYY", Locale.UK).format(cal.getTime()));
    }

    public static int getYearNumber(String dateStr) throws ParseException {
        Date date = new SimpleDateFormat("dd-MM-yyyy", Locale.UK).parse(dateStr);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return Integer.parseInt(new SimpleDateFormat("YYYY", Locale.UK).format(cal.getTime()));
    }

    public static String getCurDateTime() {
        Calendar cal = Calendar.getInstance();
        return new SimpleDateFormat("YYYY-MM-dd--HH:mm:ss", Locale.UK).format(cal.getTime());
    }

    public static String getCurTime() {
        Calendar cal = Calendar.getInstance();
        return new SimpleDateFormat("HH:mm:ss", Locale.UK).format(cal.getTime());
    }

    public static String getCurDate() {
        Calendar cal = Calendar.getInstance();
        return new SimpleDateFormat("dd-MM-yyyy", Locale.UK).format(cal.getTime());
    }

    public static int[] getDateParts(String dateStr) throws ParseException {
        Date date = new SimpleDateFormat("dd-MM-yyyy", Locale.UK).parse(dateStr);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return new int[] {
                cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.MONTH), cal.get(Calendar.YEAR)
        };
    }

    public static int[] getTimeParts(String timeStr) throws ParseException {
        Date date = new SimpleDateFormat("HH:mm:ss", Locale.UK).parse(timeStr);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return new int[] {
                cal.get(Calendar.MINUTE), cal.get(Calendar.HOUR_OF_DAY)
        };
    }

    public static boolean compareDateTime(String dateStr1, String dateStr2) throws ParseException {
        Date date1 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.UK).parse(dateStr1);
        Date date2 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.UK).parse(dateStr2);
        return date2.after(date1);
    }

    public static boolean compareTime(String timeStr1, String timeStr2) throws ParseException {
        Date date1 = new SimpleDateFormat("HH:mm:ss", Locale.UK).parse(timeStr1);
        Date date2 = new SimpleDateFormat("HH:mm:ss", Locale.UK).parse(timeStr2);
        return date2.after(date1);
    }

    public static String getCurDateBeautiful() {
        return "Today";
    }

    public static String beautifyDate(String uglyDate) throws ParseException {

        if (uglyDate.equals(getCurDate())) {
            return "Today";
        }

        Date date = new SimpleDateFormat("dd-MM-yyyy", Locale.UK).parse(uglyDate);
        return new SimpleDateFormat("dd MMMM yyyy", Locale.UK).format(date);
    }

    public static String beautifyTime(String uglyTime) throws ParseException {
        Date date = new SimpleDateFormat("HH:mm:ss", Locale.UK).parse(uglyTime);
        return new SimpleDateFormat("hh:mm aa", Locale.UK).format(date);
    }

    public static String unBeautifyTime(String beautifulTime) throws ParseException {
        Date date = new SimpleDateFormat("hh:mm aa", Locale.UK).parse(beautifulTime);
        return new SimpleDateFormat("HH:mm:ss", Locale.UK).format(date);
    }

    public static String unBeautifyDate(String beautifulDate) throws ParseException {

        if (beautifulDate.equals("Today")) {
            return getCurDate();
        }

        Date date = new SimpleDateFormat("dd MMMM yyyy", Locale.UK).parse(beautifulDate);
        return new SimpleDateFormat("dd-MM-yyyy", Locale.UK).format(date);
    }

    public static String getDefaultCategoriesSpreadSheetRange() {
        return "Metadata!B3:B";
    }

    public static String getDefaultTransactionsSheetTitle() {
        return "Transactions";
    }

    public static int getDefaultTransactionsSpreadSheetStartPosition() {
        return 4;
    }

    public static String getCellRange(String sheetTitle,
                                      String startCol,
                                      String endCol,
                                      int position) {
        return sheetTitle + "!" + startCol + position + ":" + endCol;
    }

    public static int getRowNumber(String cellRange) {
        return Integer.parseInt(cellRange.split("[a-zA-Z]+-[0-9][!][A-Z]+|[:][A-Z]+")[1]);
    }
}
