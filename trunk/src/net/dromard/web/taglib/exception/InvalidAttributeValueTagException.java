/**
 * 	File : InvalidAttributeValueTagException.java 12 juin 07
 */

package net.dromard.web.taglib.exception;

import javax.servlet.jsp.JspTagException;

/**
 * Custom Exception.
 * <br>
 * @author Gabriel Dromard
 */
public class InvalidAttributeValueTagException extends JspTagException {
    /** Serial UID. */
    private static final long serialVersionUID = -7157839683603655719L;

    /**
     * Default constructor.
     */
    public InvalidAttributeValueTagException() {
        super();
    }

    /**
     * Constcructor with a message.
     * @param message The exception message.
     */
    public InvalidAttributeValueTagException(final String message) {
        super(message);
    }
}


