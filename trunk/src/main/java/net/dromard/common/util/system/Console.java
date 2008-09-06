/*
 * Created on 10 mars 2006
 * By Gabriel DROMARD
 */
package net.dromard.common.util.system;

import java.io.IOException;

/**
 * This class is a System tool library.
 * 
 * <br/>
 * <pre>
 *   +---------+
 *   | History |
 *   +---------+
 * 
 * 03/12/2004 by Gabriel Dromard
 *   Add methods Console.printLineInframe(text);
 *
 * 23/01/2004 by Gabriel Dromard
 *   Creation.
 * 
 * </pre>
 * <br/>
 * 
 * @author Gabriel DROMARD
 */
public class Console {
    private final static int NB_CHAR_IN_LINE = 50;
    private final static int NB_CHAR_IN_TAB = 4;
    private final static char CHAR_CORNER = '+', CHAR_MIDDLE = '|', CHAR_UPPER = '-';
    //private static String LINE = buildLine(CHAR_CORNER, CHAR_UPPER, "", CHAR_CORNER, NB_CHAR_IN_LINE);
    
    
    /**
     * Print memory statistics
     */
    public static final void printMemoryStat() {
        System.out.println("Total Memory: " + Runtime.getRuntime().totalMemory());
        System.out.println("Free  Memory: " + Runtime.getRuntime().freeMemory());
        System.out.println("Used  Memory: " + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()));
    }
    
    /**
     * Build a line of <code>'nbCharInLine'</code> characters
     * @param firstChar The first char
     * @param in All char in the line
     * @param text The tect to fill in !
     * @param lastChar The last char
     * @param nbCharInLine The number of char in the line
     */
    public static final String buildLine(char firstChar, char in, String text, char lastChar, int nbCharInLine) {
        String line = "";
        while(line.length() < nbCharInLine) {
            if(line.length() == 0) line += firstChar;
            else if(line.length() == 2 && text.length()>0) line += text;
            else if(line.length() == nbCharInLine-1) line += lastChar;
            else line += in;
        }
        return line;
    }
    
    /**
     * Ask a question to a user from Console
     * @param question Question to ask to the user
     * @param defaultAnswer The default answer can be what ever you want. It is prompt in the question. If the user simply type ENTER this default value is get
     * @return The answer from the user
     */
    public static final String ask(String question, String defaultAnswer) throws IOException {
        // Construct prompt
        String defaultAnswerPrompt = " ";
        if(defaultAnswer != null && defaultAnswer.length() > 0) defaultAnswerPrompt = " ["+defaultAnswer+"] ";
        // Ask the question to the user
        String answer = read(question + defaultAnswerPrompt);
        // Put default answer if user simply type ENTER
        if(answer.length() == 0) answer = defaultAnswer;
        return answer;
    }
    
    /**
     * Ask the user if he is sure
     * @param message Message to ensure
     * @param defaultAnswer This set the default answer to return if the user just type ENTER
     * @return Sure or not Sure
     * @see #ask
     */
    public static final boolean areYouSure(String message, boolean defaultAnswer) throws IOException {
        String answer = ask(message, (defaultAnswer)?"YES":"NO");
        // Test answer
        if(answer.equalsIgnoreCase("YES")) return true;
        // If answer does not iquals to correctAnswer ... return false
        return false;
    }
    
    /**
     * This static method print a menu with title in frame
     * @param menu Each of menu entries First entry is the menu title
     * @param defaultAnswer The default answer to retreive if the user simply type ENTER key
     * @throws IOException
     */
    public static final String printMenu(Object[] menu, String defaultAnswer) throws IOException {
        String tab = "";
        String line = printFrame(menu[0].toString());
        while(tab.length() < NB_CHAR_IN_TAB) tab += " ";
        for(int i=1; i<menu.length; ++i) System.out.println(buildLine(CHAR_MIDDLE, ' ', tab+i+" - "+menu[i], CHAR_CORNER, NB_CHAR_IN_LINE));
        System.out.println(line);
        return Console.ask("What do you want to:", defaultAnswer);
    }
    
    /**
     * This static method print frame
     * @param title Title to print into
     * @return the line used
     */
    public static final String printFrame(String title) {
        String line = buildLine(CHAR_CORNER, CHAR_UPPER, "", CHAR_CORNER, NB_CHAR_IN_LINE);
        String content = buildLine(CHAR_MIDDLE, ' ', title, CHAR_MIDDLE, NB_CHAR_IN_LINE);
        System.out.println(line);
        System.out.println(content);
        System.out.println(line);
        return line;
    }
    
    /**
     * Print line in a frame.
     * @param text The text to print into the line.
     */
    public static final void printLineInframe(String text) {
        System.out.println(buildLine(CHAR_MIDDLE, ' ', text, CHAR_MIDDLE, NB_CHAR_IN_LINE));
    }
    
    /**
     * This static method get the content from system.in
     * @param prompt The prompt to print before asking user to type in
     * @throws IOException
     */
    public static final String read(String prompt) throws IOException {
        if(prompt != null) System.out.print(prompt);
        String entered = "";
        char c;
        while ((c = (char)System.in.read()) != '\n') {
            // store what entered
            if(c!='\r') entered += c;
        }
        return entered;
    }
    
    /**
     * This static method get the content from system.in without prompting typed text
     * @param prompt The prompt to print before asking user to type in
     * @throws IOException
     */
    public static final String readPassword(String prompt) throws IOException {
        System.out.print(prompt);
        
        MaskingThread maskingthread = null;
        Thread thread = null;
        maskingthread = new MaskingThread(prompt);
        thread = new Thread(maskingthread);
        thread.start();
        
        String entered = "";
        char c;
        while ((c = (char)System.in.read()) != '\n') {
            // store what entered
            if(c!='\r') entered += c;
        }
        // assume enter pressed, stop masking
        thread.interrupt();
        //thread.start();
        maskingthread.stopMasking();
        // Empty the last line
        System.out.print("                        \r");
        System.out.print("                        \r");
        return entered;
    }
}

/**
 * This class attempts to erase characters echoed to the Console.
 */
class MaskingThread extends Thread {
    private boolean stop = false;
    private String prompt;
    
    /**
     *@param prompt The prompt displayed to the user
     */
    public MaskingThread(String prompt) { this.prompt = prompt; }
    
    /**
     * Begin masking until asked to stop.
     */
    public void run() {
        while(!stop) {
            // attempt masking at this rate
            try { Thread.sleep(1); } catch (InterruptedException iex) { iex.printStackTrace(); }
            if (!stop) System.out.print("                                  " + "\r" + prompt);
            System.out.flush();
        }
    }
    
    /**
     * Instruct the thread to stop masking.
     */
    public void stopMasking() { this.stop = true; }
}
