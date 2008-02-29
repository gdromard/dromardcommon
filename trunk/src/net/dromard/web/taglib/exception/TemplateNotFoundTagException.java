/**
 * 	File : TemplateNotFoundTagException.java 12 juin 07
 */

package net.dromard.web.taglib.exception;

import javax.servlet.jsp.JspTagException;

/**
 * Custom Exception that is thrown when the template is not found.
 * <br>
 * @author          st22085
 */
public class TemplateNotFoundTagException extends JspTagException {
    /** Serial UID. */
    private static final long serialVersionUID = -1210855586046584708L;

    /**
     * Default constructor.
     */
    public TemplateNotFoundTagException() {
        super();
    }

    /**
     * Constcructor with a throwable.
     * @param throwable The original error.
     */
    public TemplateNotFoundTagException(final Throwable throwable) {
        super();
        setStackTrace(throwable.getStackTrace());
    }

    /**
     * Constcructor with a message.
     * @param message The exception message.
     */
    public TemplateNotFoundTagException(final String message) {
        super(message);
    }

    /**
     * Constcructor with a message.
     * @param message   The exception message.
     * @param throwable The original error.
     */
    public TemplateNotFoundTagException(final String message, final Throwable throwable) {
        super(message);
        setStackTrace(throwable.getStackTrace());
    }
}


