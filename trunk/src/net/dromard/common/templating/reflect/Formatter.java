package net.dromard.common.templating.reflect;




/**
 * formater of methods returned result.
 * <br>
 * @author Gabriel Dromard
 */
public interface Formatter {
    /**
     * Must format the object into string.
     * @param object The object
     * @return The object string representation
     * @throws Exception Any exception can occured while formatting.
     */
    String format(final Object object) throws Exception;
}


