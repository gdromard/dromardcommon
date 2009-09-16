/*
 * Created on 20 sept. 2004
 * By Gabriel Dromard
 */
package net.dromard.common.util;

import java.util.Date;

/**
 * This is a tool Class that make difference between two date
 * @author Gabriel Dromard
 */
public class DateDifference {
    private long millis = 0L;
    private long seconds = 0;
    private long minutes = 0;
    private long hours = 0;
    private long days = 0;
    private long difference = 0L;

    /**
     * This is a tool Class that make difference between two date
     * @param begin First date
     * @param end   Second date
     */
    public DateDifference(final Date begin, final Date end) {
        this(Math.abs(end.getTime() - begin.getTime()));
    }

    /**
     * This is a tool Class that make difference between two date
     * @param begin First date
     * @param end   Second date
     */
    public DateDifference(final long begin, final long end) {
        this(Math.abs(end - begin));
    }

    public DateDifference(final long difference) {
        this.difference = difference;
        if (difference > 0L) {
            millis = difference % 1000L;
            seconds = difference / 1000;
            minutes = seconds / 60L;
            seconds = seconds % 60L;
            if (minutes > 0L) {
                hours = minutes / 60L;
                minutes = minutes % 60L;
            }
            if (hours > 0L) {
                days = hours / 24L;
                hours = hours % 24L;
            }
        }
    }

    public int getMillis() {
        return (int) millis;
    }

    public int getSeconds() {
        return (int) seconds;
    }

    public int getMinutes() {
        return (int) minutes;
    }

    public int getHours() {
        return (int) hours;
    }

    public int getDays() {
        return (int) days;
    }

    public long getTimeElapse() {
        return difference;
    }

    /**
     * Its formated as "D day(s) HH:MM:SS.ms"
     * @return a String representing the elapsed time between the two dates
     */
    @Override
    public String toString() {
        if (difference < 1000) {
            return millis + "ms";
        }
        String result = "";
        if (days > 0) {
            result += days + " day" + (days > 1 ? "s" : "");
        }
        if (hours > 0) {
            result += (result.length() > 0 ? ", " : "") + hours + " hour" + (hours > 1 ? "s" : "");
        }
        if (minutes > 0) {
            result += (result.length() > 0 ? ", " : "") + minutes + " min";
        }
        if (seconds > 0) {
            result += (result.length() > 0 ? ", " : "") + seconds + "s";
        }
        return result;
    }
}
