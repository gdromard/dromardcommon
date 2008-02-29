/*
 * Created on 6 mars 2006
 * By Gabriel Dromard
 */
package net.dromard.common.logging;

import java.io.IOException;

import net.dromard.common.io.FileHelper;


public class SimpleLogger {
    
    private final static String outputFileName = null; //"XMLMailHandlerService.log";
    private final static boolean debug = true;
    private final static boolean info  = true;
    private final static boolean warm  = true;
    private final static boolean error = true;
    
    /**
     * Protected method that print warnings messages if warm flag equals true.
     * @param requester
     * @param warningMsg
     */
    public static final void warm(Class requester, String warningMsg) {
        if(warm) println("<WARNING> ["+getClassName(requester)+"] "+warningMsg, false);
    }

    /**
     * Protected method that print debug messages if debug flag equals true.
     * @param requester
     * @param debugMsg
     */
    public static final void debug(Class requester, String debugMsg) {
        if(debug) println("<DEBUG> ["+getClassName(requester)+"] "+debugMsg, false);
    }

    /**
     * Protected method that print error messages if error flag equals true.
     * @param requester
     * @param errorMsg
     */
    public static final void error(Class requester, String errorMsg) {
        if(error) println("<ERROR> ["+getClassName(requester)+"] "+errorMsg, true);
    }

    /**
     * Protected method that print info messages if info flag equals true.
     * @param requester
     * @param msg
     */
    public static final void info(Class requester, String msg) {
        if(info) println("<INFO> ["+getClassName(requester)+"] "+msg, false);
    }

    /**
     * Get the class name without the package string !
     * @param clazz The class from which you want to retreive the name 
     * @return the class name without the package string !
     */
    protected static final String getClassName(Class clazz) {
        int index = 0;
        if(clazz.getPackage() != null) index = clazz.getPackage().getName().length();
        return (index>0)?clazz.getName().substring(index+1):clazz.getName();
    }

    /**
     * It switch between file or standard output.
     * @param msg The message to print
     * @param err Print error in err output or in standard out put ?
     */
    protected static final void println(String msg, boolean err) {
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
