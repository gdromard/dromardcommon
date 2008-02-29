/**
 * 	File : TestTagJspWriter.java 13 juin 07
 */

package net.dromard.web.mock.test;

import java.io.IOException;

import javax.servlet.jsp.JspWriter;

/**
 * An implemenation of JspWriter so as to test Tag classes.
 * <br>
 * @author          st22085
 */
public class MockJspWriter extends JspWriter {
    /** Default buffer size. */
    private static final int BUFFER_SIZE = 1024;
    /** Internal buffer. */
    private StringBuffer buffer = new StringBuffer();
    /** Say if print action has to be redirected to console output. */
    private boolean printToConsole = false;

    /**
     * Retreive the last output and reset buffer.
     * @return The buffer before being reseted
     */
    public final String getLastOutput() {
        String result = buffer.toString();
        buffer = new StringBuffer();
        return result;
    }

    /**
     * Say if print action has to be redirected to console output.
     * @return True if print action is redirected to console output.
     */
    public final boolean isConsoleOutputEnabled() {
        return printToConsole;
    }

    /**
     * Say if print action has to be redirected to console output.
     * @param output True if print action must be redirected to console output.
     */
    public final void setConsoleOutputEnabled(final boolean output) {
        printToConsole = output;
    }

    /**
     * Default constructor.
     */
    public MockJspWriter() {
        super(BUFFER_SIZE, true);
    }

    /**
     * Constructor that let you enable output redirection to console.
     * @param printToConsole true to redirect output to console.
     */
    public MockJspWriter(final boolean printToConsole) {
        this();
        this.printToConsole = printToConsole;
    }

    /**
     * Default implementation.
     * @see javax.servlet.jsp.JspWriter
     * @throws IOException (not used in this implementation.)
     */
    public final void clear() throws IOException {
    }

    /**
     * Default implementation.
     * @see javax.servlet.jsp.JspWriter
     * @throws IOException (not used in this implementation.)
     */
    public final void clearBuffer() throws IOException {
    }

    /**
     * Default implementation.
     * @see javax.servlet.jsp.JspWriter
     * @throws IOException (not used in this implementation.)
     */
    public final void close() throws IOException {
    }

    /**
     * Default implementation.
     * @see javax.servlet.jsp.JspWriter
     * @throws IOException (not used in this implementation.)
     */
    public final void flush() throws IOException {
    }

    /**
     * Default implementation.
     * @return 0
     * @see javax.servlet.jsp.JspWriter
     */
    public final int getRemaining() {
        return 0;
    }

    /**
     * Default implementation.
     * @see javax.servlet.jsp.JspWriter
     * @throws IOException (not used in this implementation.)
     */
    public final void newLine() throws IOException {
        print("\n");
    }

    /**
     * Default implementation.
     * @see javax.servlet.jsp.JspWriter
     * @param arg0 argument
     * @throws IOException (not used in this implementation.)
     */
    public final void print(final boolean arg0) throws IOException {
        print("" + arg0);
    }

    /**
     * Default implementation.
     * @see javax.servlet.jsp.JspWriter
     * @param arg0 argument
     * @throws IOException (not used in this implementation.)
     */
    public final void print(final char arg0) throws IOException {
        print("" + arg0);
    }

    /**
     * Default implementation.
     * @see javax.servlet.jsp.JspWriter
     * @param arg0 argument
     * @throws IOException (not used in this implementation.)
     */
    public final void print(final int arg0) throws IOException {
        print("" + arg0);
    }

    /**
     * Default implementation.
     * @see javax.servlet.jsp.JspWriter
     * @param arg0 argument
     * @throws IOException (not used in this implementation.)
     */
    public final void print(final long arg0) throws IOException {
        print("" + arg0);
    }

    /**
     * Default implementation.
     * @see javax.servlet.jsp.JspWriter
     * @param arg0 argument
     * @throws IOException (not used in this implementation.)
     */
    public final void print(final float arg0) throws IOException {
        print("" + arg0);
    }

    /**
     * Default implementation.
     * @see javax.servlet.jsp.JspWriter
     * @param arg0 argument
     * @throws IOException (not used in this implementation.)
     */
    public final void print(final double arg0) throws IOException {
        print("" + arg0);
    }

    /**
     * Default implementation.
     * @see javax.servlet.jsp.JspWriter
     * @param arg0 argument
     * @throws IOException (not used in this implementation.)
     */
    public final void print(final char[] arg0) throws IOException {
        print(new String(arg0));
    }

    /**
     * Default implementation.
     * @see javax.servlet.jsp.JspWriter
     * @param arg0 argument
     * @throws IOException (not used in this implementation.)
     */
    public final void print(final String arg0) throws IOException {
        if (printToConsole) {
            System.out.print(arg0);
        }
        buffer.append(arg0);
    }

    /**
     * Default implementation.
     * @see javax.servlet.jsp.JspWriter
     * @param arg0 argument
     * @throws IOException (not used in this implementation.)
     */
    public final void print(final Object arg0) throws IOException {
        print(arg0.toString());
    }

    /**
     * Default implementation.
     * @see javax.servlet.jsp.JspWriter
     * @throws IOException (not used in this implementation.)
     */
    public final void println() throws IOException {
        newLine();
    }

    /**
     * Default implementation.
     * @see javax.servlet.jsp.JspWriter
     * @param arg0 argument
     * @throws IOException (not used in this implementation.)
     */
    public final void println(final boolean arg0) throws IOException {
        print(arg0);
        newLine();
    }

    /**
     * Default implementation.
     * @see javax.servlet.jsp.JspWriter
     * @param arg0 argument
     * @throws IOException (not used in this implementation.)
     */
    public final void println(final char arg0) throws IOException {
        print(arg0);
        newLine();
    }

    /**
     * Default implementation.
     * @see javax.servlet.jsp.JspWriter
     * @param arg0 argument
     * @throws IOException (not used in this implementation.)
     */
    public final void println(final int arg0) throws IOException {
        print(arg0);
        newLine();
    }

    /**
     * Default implementation.
     * @see javax.servlet.jsp.JspWriter
     * @param arg0 argument
     * @throws IOException (not used in this implementation.)
     */
    public final void println(final long arg0) throws IOException {
        print(arg0);
        newLine();
    }

    /**
     * Default implementation.
     * @see javax.servlet.jsp.JspWriter
     * @param arg0 argument
     * @throws IOException (not used in this implementation.)
     */
    public final void println(final float arg0) throws IOException {
        print(arg0);
        newLine();
    }

    /**
     * Default implementation.
     * @see javax.servlet.jsp.JspWriter
     * @param arg0 argument
     * @throws IOException (not used in this implementation.)
     */
    public final void println(final double arg0) throws IOException {
        print(arg0);
        newLine();
    }

    /**
     * Default implementation.
     * @see javax.servlet.jsp.JspWriter
     * @param arg0 argument
     * @throws IOException (not used in this implementation.)
     */
    public final void println(final char[] arg0) throws IOException {
        print(arg0);
        newLine();
    }

    /**
     * Default implementation.
     * @see javax.servlet.jsp.JspWriter
     * @param arg0 argument
     * @throws IOException (not used in this implementation.)
     */
    public final void println(final String arg0) throws IOException {
        print(arg0);
        newLine();
    }

    /**
     * Default implementation.
     * @see javax.servlet.jsp.JspWriter
     * @param arg0 argument
     * @throws IOException (not used in this implementation.)
     */
    public final void println(final Object arg0) throws IOException {
        print(arg0);
        newLine();
    }

    /**
     * Default implementation.
     * @see javax.servlet.jsp.JspWriter
     * @param arg0 argument
     * @param arg1 argument
     * @param arg2 argument
     * @throws IOException (not used in this implementation.)
     */
    public final void write(final char[] arg0, final int arg1, final int arg2) throws IOException {
        byte[] bytes = new byte[arg0.length];
        for (int i = 0; i < arg0.length; ++i) {
            bytes[i] = (byte) arg0[i];
        }
        print(new String(bytes, arg1, arg2));
    }
}


