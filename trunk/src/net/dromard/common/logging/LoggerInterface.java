/*
 * Created on 9 févr. 2005
 * By Gabriel Dromard
 */
package net.dromard.common.logging;


import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.Properties;

import net.dromard.common.io.FileHelper;

/**
 * This is a Logger util class to be overwrited fi you want to use it.
 *
 * <br/>
 * <pre>
 *   +---------+
 *   | History |
 *   +---------+
 *
 * [24/03/2005] by Gabriel Dromard
 *   - Add loggerObjects management.
 *   This attribute tell the class to track object reference in all debug messages.
 *
 * [10/03/2005] by Gabriel Dromard
 *   - Separate reusable code from specific.
 *
 * [06/03/2005] by Gabriel Dromard
 *   - Rename property manager class.
 *
 * [25/02/2005] by Gabriel Dromard
 *   - Add a test on file date to check if we have to reload properties from files.
 *
 * [09/02/2005] by Gabriel Dromard
 *   - Creation.
 * </pre><br/>
 * 
 * @author Gabriel Dromard
 */
public class LoggerInterface {
	protected static boolean staticWarning = true;
	protected static boolean staticDebug = false;

	protected static boolean loggerObjects = true;
	protected static boolean loggerWarning = true;
	protected static boolean loggerDebug = false;

	protected boolean debug = false;
	protected boolean warm = false;

	protected final String className;
	protected final Date objTimeStamp;
	protected final String id;

	protected static final String outputFileName = /**/null; /**"logs.log";/**/

	/**
	 * Print debug on object creation
	 */
	public LoggerInterface(Properties prop) {
		debug = staticDebug;
		warm = staticWarning;
		
		if(getClass().getPackage() != null) 
			className = getClass().getName().substring(getClass().getPackage().getName().length()+1, getClass().getName().length());
		else 
			className = getClass().getName();
		objTimeStamp = new Date();
		id = super.toString().substring(super.toString().indexOf('@'));
		try {
			String sDebug = prop.getProperty(className+".debug");
			String sWarni = prop.getProperty(className+".warning");
			if(sDebug!=null) debug = new Boolean(sDebug).booleanValue();
			if(sWarni!=null) warm = new Boolean(sWarni).booleanValue();
		} catch (Exception ex) {
			ex.printStackTrace();
			debug("Exception while loading debug or warning property for class '"+className+"'");
		}
		if(debug && loggerObjects) debug("Object creation (id: "+id+", time stamp: "+DateFormat.getTimeInstance().format(objTimeStamp)+")");
		//System.out.println("["+className+"] <DEBUG> debug: "+debug+" - warning - "+warm);
	}

	/**
	 * get the life time of this object in milleseconds
	 */
	protected final long getLifeTime() { return new Date().getTime() - objTimeStamp.getTime(); }


	/**
	 * Print debug on object finalization 
	 * @see java.lang.Object#finalize()
	 */
	protected void finalize() throws Throwable {
		if(debug && loggerObjects) debug("Object finalization (id: "+id+", life time: "+(getLifeTime()/1000)+" ms)");
		super.finalize();
	}

	/**
	 * Protected method that print warnings messages if warm flag equals true.
	 * @param warningMsg
	 */
	protected final void warm(String warningMsg) {
		if(warm) println("<WARNING> ["+className+"] "+warningMsg, false);
	}

	/**
	 * Protected method that print debug messages if debug flag equals true.
	 * @param debugMsg
	 */
	protected final void debug(String debugMsg) {
		if(debug) println("<DEBUG> ["+className+"] "+((loggerDebug)?"<id: "+id+"> ":"")+debugMsg, false);
	}

	/**
	 * Protected method that print debug messages if debug flag equals true.
	 * @param errorMsg The error message to be logged.
	 */
	protected final void error(String errorMsg) {
		println("<ERROR> ["+className+"] "+errorMsg, true);
	}

	/**
	 * Protected method that print debug messages if debug flag equals true.
	 * @param msg The message to be logged.
	 */
	protected final void log(String msg) {
		println("<INFO> ["+className+"] "+msg, false);
	}

	/**
	 * Public method that print messages.
	 * @param requester The Class object of the requester object (needed to print class name).
	 * @param msg       The debug message to print.
	 */
	public static final void log(Class requester, String msg) {
		println("<INFO> ["+getClassName(requester)+"] "+msg, false);
	}

    /**
     * Public method that print debug messages.
     * @param requester The Class object of the requester object (needed to print class name).
     * @param errorMsg  The error message to print.
     */
    public static final void error(Class requester, String errorMsg) {
        println("<ERROR> ["+getClassName(requester)+"] "+errorMsg, true);
    }

    /**
     * Public method that print debug messages.
     * @param requester The Class object of the requester object (needed to print class name).
     * @param debugMsg  The debug message to print.
     */
    public static final void debug(Class requester, String debugMsg) {
        println("<DEBUG> ["+getClassName(requester)+"] "+debugMsg, true);
    }

	/**
	 * Get the class name without the package string !
	 * @param clazz The class from which you want to retreive the name 
	 * @return the class name without the package string !
	 */
	public static final String getClassName(Class clazz) {
		int index = 0;
		if(clazz.getPackage() != null) index = clazz.getPackage().getName().length();
		return (index>0)?clazz.getName().substring(index+1):clazz.getName();
	}

	/*
	 * Get a ramdom characters to build the object ID 
	 * Used to differenciate two objects that are instantiate on the same millisecond !!
	private static final char getRandomChar() { return (char)(new Random().nextInt(26)+65); }
	 */

	protected static void println(String msg, boolean err) {
		if(outputFileName != null) {
			try {
				FileHelper.append2File(outputFileName, msg+"\n");
			} catch(IOException ex) {
				ex.printStackTrace();
				System.out.println(msg);
			}
		} else {
			if(err) System.err.println(msg);
			else System.out.println(msg);
		}
	}
}
