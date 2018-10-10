package io.github.amanshuraikwar.howmuch.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Utility class for the app.
 *
 * @author Amanshu Raikwar
 * Created by Amanshu Raikwar on 07/03/18.
 */

public class Util {

    public static String getTag(Object object) {
        return object.getClass().getSimpleName();
    }

    public static String getTag(Class cls) {
        return cls.getClass().getSimpleName();
    }

    public static String getCurMonth() {
        Calendar cal = Calendar.getInstance();
        return new SimpleDateFormat("MMMM", Locale.UK).format(cal.getTime());
    }

    public static int getCurMonthNumber() {
        Calendar cal = Calendar.getInstance();
        return Integer.parseInt(new SimpleDateFormat("MM", Locale.UK).format(cal.getTime())) + 3;
    }

    public static int getCurYearNumber() {
        Calendar cal = Calendar.getInstance();
        return Integer.parseInt(new SimpleDateFormat("YY", Locale.UK).format(cal.getTime())) + 1;
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

    public static String getCurDateBeautiful() {
        Calendar cal = Calendar.getInstance();
        return new SimpleDateFormat("dd MMMM yyyy", Locale.UK).format(cal.getTime());
    }

    public static String beautifyDate(String uglyDate) throws ParseException {
        Date date = new SimpleDateFormat("dd-MM-yyyy", Locale.UK).parse(uglyDate);
        return new SimpleDateFormat("dd MMMM yyyy", Locale.UK).format(date);
    }

    public static String unBeautifyDate(String beautifulDate) throws ParseException {
        Date date = new SimpleDateFormat("dd MMMM yyyy", Locale.UK).parse(beautifulDate);
        return new SimpleDateFormat("dd-MM-yyyy", Locale.UK).format(date);
    }

    public static String createSpreadsheetTitle() {
        return "HowMuch-" + getCurMonth() + "-" + getCurDateTime();
    }

    public static List<String> getDefaultSheetTitles() {
        return Arrays.asList("Metadata", "Transactions");
    }

    public static List<List<String>> getDefaultCategoriesWithHeading() {
        return
                Arrays.asList(
                        Collections.singletonList("Categories"),
                        Collections.singletonList("Food"),
                        Collections.singletonList("Health/Medical"),
                        Collections.singletonList("Home"),
                        Collections.singletonList("Transportation"),
                        Collections.singletonList("Personal"),
                        Collections.singletonList("Utilities"),
                        Collections.singletonList("Travel"),
                        Collections.singletonList("Debt"),
                        Collections.singletonList("Other"));
    }

    public static String getDefaultCategoriesSpreadSheetRangeWithHeading() {
        return "Metadata!B2:B";
    }

    public static String getDefaultTransactionsSpreadSheetRangeWithHeading() {
        return "Transactions!B2:F";
    }

    public static String getDefaultCategoriesSpreadSheetRange() {
        return "Metadata!B3:B";
    }

    public static String getDefaultTransactionsSpreadSheetRange() {
        return "Transactions!B3:F";
    }

    public static List<List<String>> getDefaultTransactionsHeading() {
        return
                Arrays.asList(
                        Collections.singletonList("Transactions"),
                        Arrays.asList("Date", "Time", "Amount", "Description", "Category"));
    }
}
