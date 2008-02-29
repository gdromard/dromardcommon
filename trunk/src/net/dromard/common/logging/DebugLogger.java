/*
 * Created on 31 mai 2006
 * By Gabriel DROMARD
 */
package net.dromard.common.logging;

/**
 * DebugLogger class is usefull to help printing informations with debug info.<br>
 * <br>
 * This class offer some helpfull tools methods for debug purpose.<br>
 * <b><u>NOTE:</u> for performance reason it is preconised to use this methods.</b><br>  
 *
 * <br>
 * <pre>
 *    +---------+
 *    | History |
 *    +---------+
 *
 *  [31/05/2006] by Gabriel Dromard
 *     - Creation.
 *  [02/09/2006] by Gabriel Dromard
 *     - Add JavaDoc comments.
 *     - Genralize methods.
 *     - Add info/error methods.
 *     
 * </pre>
 * <br/>
 * @author Gabriel Dromard
 */
public class DebugLogger {

    /**
     * This methods will print the given message and will also print the class name 
     * from wihch it is called and the line number in the java source file.  
     * 
     * @param info    What to print at begining (ex: [DEBUG], [INFO], [WARNING], [ERROR])
     * @param message The message to print.
     */
    public static void print(String info, String message) {
        try {
            throw new Throwable();
        } catch (Throwable th) {
            print(info, message, th);
        }
    }

    /**
     * This methods will print the given message and will also print the details of the 
     * first stack trace given.
     * 
     * @param info    What to print at begining (ex: [DEBUG], [INFO], [WARNING], [ERROR])
     * @param message The message to print.
     * @param th      The Throwable error/exception.
     */
    public static void print(String info, String message, Throwable th) {
    	int stkNum = 1;
        StackTraceElement elmt = th.getStackTrace()[1];
        while(elmt.getClassName().indexOf(DebugLogger.class.getName()) > -1) elmt = th.getStackTrace()[++stkNum]; 
        if(message == null) message = th.getMessage();
        else if(th.getMessage() != null) message += " ("+th.getMessage()+")";
        System.out.println(info+" "+elmt.getClassName()+"."+elmt.getMethodName()+"("+elmt.getFileName()+":"+elmt.getLineNumber()+") # "+message);
    }
    
    /**
     * This methods will print the given message and will also print the class name 
     * from wihch it is called and the line number in the java source file.  
     * 
     * @param message The message to print.
     */
    public static void debug(String message) {
        print("[DEBUG]", message);
    }
    
    /**
     * This methods will print the given message and will also print the class name 
     * from wihch it is called and the line number in the java source file.  
     * 
     * @param message The message to print.
     */
    public static void info(String message) {
        print("[INFO]", message);
    }

    /**
     * This methods will print the given message and will also print the class name 
     * from wihch it is called and the line number in the java source file.  
     * 
     * @param message The message to print.
     */
    public static void warm(String message) {
        print("[WARNING]", message);
    }

    /**
     * This methods will print the details of the first stack trace given.
     * 
     * @param th      The Throwable error/exception.
     */
    public static void error(Throwable th) {
        error(th, null);
    }

    /**
     * This methods will print the details of the first stack trace given.
     * 
     * @param message The message to print.
     * @param th      The Throwable error/exception.
     */
    public static void error(Throwable th, String message) {
        print("[ERROR]", message, th);
    }
}
