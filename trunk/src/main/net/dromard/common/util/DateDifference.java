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
    private String sDifference = "0ms";
    
	/**
	 * This is a tool Class that make difference between two date
	 * @param begin First date
	 * @param end   Second date
	 */
	public DateDifference(Date begin, Date end) {
		this(end.getTime()-begin.getTime());
	}
	
	public DateDifference(long difference) {
		this.difference = difference;
        if(difference > 0L) {
        	millis = difference % 1000L;
            seconds = difference / 1000;
            minutes = seconds / 60L;
            seconds = seconds % 60L;
            if(minutes > 0L) {
                hours = minutes / 60L;
                minutes = minutes % 60L;
            }
            if(hours > 0L) {
                days = hours / 24L;
                hours = hours % 24L;
            }
	        sDifference = (days > 1)?days+" Days ":(days > 0)?days+" Day ":"";
	        sDifference += ""  + ((hours == 0)  ?"00" :((hours < 10)  ?"0"+hours  :hours  +""));
	        sDifference += ":" + ((minutes == 0)?"00" :((minutes < 10)?"0"+minutes:minutes+""));
	        sDifference += ":" + ((seconds == 0)?"00" :((seconds < 10)?"0"+seconds:seconds+""));
	        sDifference += "." + ((millis == 0) ?"000":((millis < 10)?"00"+millis :(millis < 100) ?"0"+millis:millis+""));
        }
	}
	public int getMillis()  { return (int)millis; }
	public int getSeconds() { return (int)seconds; }
	public int getMinutes() { return (int)minutes; }
	public int getHours()   { return (int)hours; }
	public int getDays()    { return (int)days; }
	/**
	 * Its formated as "D day(s) HH:MM:SS.ms"
	 * @return a String representing the elapse time beetwen the tow dates
	 */
	public String displayDifference() { return sDifference; }
	public String getDifference() { return displayDifference(); }
	public long getTimeElapse() { return difference; }
}
