package io.github.amanshuraikwar.howmuch.util;

/**
 * Created by amanshuraikwar on 07/03/18.
 */

public class LogUtil {

    public static String getLogTag(Object object) {
        return object.getClass().getSimpleName();
    }
}
