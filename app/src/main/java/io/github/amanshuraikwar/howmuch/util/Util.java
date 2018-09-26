package io.github.amanshuraikwar.howmuch.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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

    public static String getCurDateTime() {
        Calendar cal = Calendar.getInstance();
        return new SimpleDateFormat("YYYY-MM-dd--HH:mm:ss", Locale.UK).format(cal.getTime());
    }

    public static String createSpreadSheetTitle() {
        return "HowMuch-" + getCurMonth() + "-" + getCurDateTime();
    }
}
