/*
 * Created on 22 mars 2006
 * By Gabriel DROMARD
 */
package net.dromard.common.io;

import java.io.IOException;
import java.io.OutputStream;
import javax.swing.JTextArea;

/**
 * An output stream that writes its output to a javax.swing.JTextArea
 * control.
 *
 * <pre>
 * // Create an instance of javax.swing.JTextArea control 
 * JTextArea txtConsole = new JTextArea();
 * 
 * // Now create a new TextAreaOutputStream to write to our JTextArea control and wrap a
 * // PrintStream around it to support the println/printf methods.
 * PrintStream out = new PrintStream( new TextAreaOutputStream( txtConsole ) );
 * 
 * // redirect standard output stream to the TextAreaOutputStream
 * System.setOut( out );
 * 
 * // redirect standard error stream to the TextAreaOutputStream
 * System.setErr( out );
 * 
 * // now test the mechanism
 * System.out.println( "Hello World" );
 * </pre>
 *
 * @author  Ranganath Kini
 * @see     javax.swing.JTextArea
 */
public class TextAreaOutputStream extends OutputStream {
    private JTextArea textControl;
    
    /**
     * Creates a new instance of TextAreaOutputStream which writes
     * to the specified instance of javax.swing.JTextArea control.
     *
     * @param control   A reference to the javax.swing.JTextArea
     *                  control to which the output must be redirected
     *                  to.
     */
    public TextAreaOutputStream( JTextArea control ) {
        textControl = control;
    }
    
    /**
     * Writes the specified byte as a character to the
     * javax.swing.JTextArea.
     *
     * @param   b   The byte to be written as character to the
     *              JTextArea.
     */
    public void write( int b ) throws IOException {
        // append the data as characters to the JTextArea control
        textControl.append( String.valueOf( ( char )b ) );
    }  
}