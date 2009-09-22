package net.dromard.common.json;

/**
 * The JSONException is thrown by the JSON.org classes then things are amiss.
 * @author JSON.org
 * @version 2
 */
public class JSONException extends Exception {

    /**
     * The serialVersionUID is a universal version identifier for a Serializable class. Deserialization uses this number to ensure that a loaded class
     * corresponds exactly to a serialized object. If no match is found, then an InvalidClassException is thrown.
     * @see Serializable
     */
    private static final long serialVersionUID = 2781855644743780303L;

    /**
     * Cause exception.
     */
    private Throwable cause = null;

    /**
     * Constructs a JSONException with an explanatory message.
     * @param message Detail about the reason for the exception.
     */
    public JSONException(final String message) {
        super(message);
    }

    /**
     * Constructs a InvocationTargetException with a cause exception.
     * @param t cause exception.
     */
    public JSONException(final Throwable t) {
        super(t.getMessage());
        this.cause = t;
    }

    /**
     * Returns the the cause of this exception (the thrown target exception, which may be null).
     * @return the cause of this exception.
     */
    public final Throwable getCause() {
        return this.cause;
    }
}
