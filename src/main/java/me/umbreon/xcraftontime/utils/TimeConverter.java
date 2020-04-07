package me.umbreon.xcraftontime.utils;

public class TimeConverter {

    public static long secondsToDays(long seconds) {
        return seconds / (24 * 3600);
    }

    public static long secondsToHours(long seconds) {
        seconds = seconds % (24 * 3600);
        return seconds / 3600;
    }

    public static long secondsToMinutes(long seconds) {
        seconds %= 3600;
        return seconds / 60;
    }

    public static long daysToSeconds(long days) {
        return days * 86400;
    }

    public static long hoursToSeconds(long hours) {
        return hours * 3600;
    }

    public static long minutesToSeconds(long minutes){
        return minutes * 60;
    }
}



