package com.hf.goalkeeper.core.utils;

/**
 * Created by hanan on 08/07/17.
 */

public class StringUtils {
    public static String getFormatedTime(int minutes, int seconds) {
        String minutesString = getFormatedNumber(minutes);
        String secondsString = getFormatedNumber(seconds);
        return minutesString + ":" + secondsString;
    }

    public static String getFormatedNumber(int number) {
        String ret;
        if (number > 9) {
            ret = String.valueOf(number);
        } else {
            ret = "0" + String.valueOf(number);
        }
        return ret;
    }

    public static String secondsToMinutes(int seconds) {
        int rawMinutes = seconds / 60;
        return getFormatedNumber(rawMinutes);
    }

    public static String secondsToSeconds(int seconds) {
        int rawSeconds = seconds % 60;
        return getFormatedNumber(rawSeconds);
    }
}
