package io.github.amanshuraikwar.howmuch.base.util;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import androidx.annotation.NonNull;

/**
 * Utility class for the app.
 *
 * @author Amanshu Raikwar
 * Created by Amanshu Raikwar on 07/03/18.
 */

public class Util {

    @NonNull
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

    public static String beautifyDate(Date uglyDate) {
        return new SimpleDateFormat("dd MMMM yyyy", Locale.UK).format(uglyDate);
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

        if (beautifulDate.equalsIgnoreCase("Today")) {
            return getCurDate();
        }

        Date date = new SimpleDateFormat("dd MMMM yyyy", Locale.UK).parse(beautifulDate);
        return new SimpleDateFormat("dd-MM-yyyy", Locale.UK).format(date);
    }

    public static long toTimeMillisec(String uglyDate, String uglyTime) throws ParseException {

        Date date =
                new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.UK)
                        .parse(uglyDate + " " + uglyTime);

        return date.getTime();
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

    public static long getFirstDayOfMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        return calendar.getTimeInMillis();
    }

    public static Date getFirstDateOfMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        return calendar.getTime();
    }

    public static long getLastSeventhDay() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -7);
        return cal.getTimeInMillis();
    }

    public static long getCurTimeMillisec() {
        Calendar cal = Calendar.getInstance();
        return cal.getTimeInMillis();
    }

    public static String getDateStrMoved(int move) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, move);
        return new SimpleDateFormat("dd-MM-yyyy", Locale.UK).format(cal.getTime());
    }

    public static String getDateStrMoved(Date date, int move) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, move);
        return new SimpleDateFormat("dd-MM-yyyy", Locale.UK).format(cal.getTime());
    }

    public static Date getDateMoved(Date date, int move) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, move);
        return cal.getTime();
    }

    public static String getMonthName(int month) {
        return new DateFormatSymbols().getMonths()[month-1];
    }

    public static String getWeekDay(String date) throws ParseException {
        Date now = new SimpleDateFormat("dd-MM-yyyy", Locale.UK).parse(date);
        SimpleDateFormat simpleDateformat = new SimpleDateFormat("E", Locale.UK);
        return simpleDateformat.format(now);
    }

    public static int daysInMonth(int month, int year) {
        Calendar myCal = new GregorianCalendar(year, month, 1);
        return myCal.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

}
