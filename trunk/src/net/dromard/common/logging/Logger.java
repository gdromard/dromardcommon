/*
 * Created on 21 oct. 2004
 * By Gabriel Dromard
 */
package net.dromard.common.logging;

import java.io.PrintStream;
import java.text.DateFormat;

/**
 * Little static class that manage log messages.
 * With additionnal informations like the current Date of the msg.
 * @author Gabriel Dromard
 */
public class Logger {
	protected static PrintStream out   = System.out;
	protected static PrintStream error = System.err;
	protected static PrintStream debug = System.err;
	
	/**
	 * Reassigns the "standard" output stream.
	 * @param out
	 */
	public static void setOut(PrintStream out) { Logger.out=out; System.setOut(out); }
	
	/**
	 * Reassigns the "standard" error output stream.
	 * @param error
	 */
	public static void setError(PrintStream error) { Logger.error = error; System.setOut(error); }
	
	/**
	 * Reassigns the debug output stream. By default is rederected into error output stream
	 * @param debug
	 */
	public static void setDebug(PrintStream debug) { Logger.debug = debug; System.setOut(debug); }
	
	/**
	 * A little static methods that is usefull for printing messages.
	 * @param message Message to print.
	 */
	public static void log(String message) {
		String date = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM).format(new java.util.Date(System.currentTimeMillis()));
		out.println("<"+date+"> "+message);
	}
	
	/**
	 * A little static methods that is usefull for printing error messages.
	 * @param message Message to print.
	 */
	public static void log(Exception ex) {
		String date = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM).format(new java.util.Date(System.currentTimeMillis()));
		error.print("<"+date+"> ");
		ex.printStackTrace(error);
	}
	
	/**
	 * A little static methods that is usefull for printing error messages.
	 * @param message Message to print.
	 */
	public static void log(String message, Exception ex) {
		String date = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM).format(new java.util.Date(System.currentTimeMillis()));
		error.print("<"+date+"> "+message+" .:. ");
		ex.printStackTrace(error);
	}
	
	/**
	 * A little static methods that is usefull for printing debug messages.
     * @param caller  The caller class.
	 * @param message Message to print.
	 */
	public static void logDebug(Class caller, String message) {
		String date = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM).format(new java.util.Date(System.currentTimeMillis()));
		debug.println("<"+date+"> <"+caller.getName()+"> "+message);
	}
	
	/**
	 * A little static methods that is usefull for printing error messages.
	 * @param message Message to print.
	 */
	public static void logDebug(Class caller, String message, Exception ex) {
		String date = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM).format(new java.util.Date(System.currentTimeMillis()));
		debug.print("<"+date+"> <"+caller.getName()+"> "+message+" .:. ");
		ex.printStackTrace(debug);
	}
}
