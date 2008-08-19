/**
 * 	File : TestTagJspWriter.java 13 juin 07
 */

package net.dromard.web.mock.test;

import java.io.FileWriter;
import java.io.IOException;

import javax.servlet.jsp.JspWriter;

/**
 * An implemenation of JspWriter so as to test Tag classes.
 * <br>
 * @author Gabriel Dromard
 */
public class MockFileJspWriter extends JspWriter {
    FileWriter writer;

    /**
     * Default constructor.
     */
    public MockFileJspWriter(final FileWriter writer) {
        super(1024, true);
        this.writer = writer;
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
        writer.write(arg0);
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


