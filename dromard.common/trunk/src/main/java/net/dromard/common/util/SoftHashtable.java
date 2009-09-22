/**
 * 	File : SoftHashtable.java 11 juin 07
 */

package net.dromard.common.util;

import java.lang.ref.SoftReference;
import java.util.Hashtable;
import java.util.Set;

/**
 * Hash table of soft references.
 * <br>
 * @author Gabriel Dromard
 */
@SuppressWarnings("unchecked")
public class SoftHashtable extends Hashtable {
    /** Serial UID. */
    private static final long serialVersionUID = -5402199747809994434L;

    /**
     * Retrieve the original object, if exists.
     * @param key The object key to retrieve.
     * @return The original object, if exists.
     */
    @Override
    public final Object get(final Object key) {
        Object result = null;

        if (key != null) {
            SoftReference softRef = (SoftReference) super.get(key);
            if (softRef != null) {
                result = softRef.get();
                if (result == null) {
                    remove(key);
                }
            }
        }
        return result;
    }

    /**
     * Put an object as soft reference in the map.
     * @param key   The object key.
     * @param value The object.
     * @return The softReference instance.
     */
    @Override
    public final Object put(final Object key, final Object value) {
        super.put(key, new SoftReference<Object>(value));
        return value;
    }

    /**
     * Remove an object with the given key.
     * @param key The object key to remove from map.
     * @return The soft reference removed.
     */
    @Override
    public final Object remove(final Object key) {
        Object returnValue = null;
        SoftReference softRef = (SoftReference) super.get(key);
        if (softRef != null) {
            softRef.clear();
            returnValue = ((SoftReference) super.remove(key)).get();
        }
        return returnValue;
    }

    /**
     * Return the entryset of objects.
     * Note: not supported will throw an UnsupportedOperationException.
     * @return nothing.
     */
    @Override
    public final Set entrySet() {
        throw new UnsupportedOperationException();
    }
}
