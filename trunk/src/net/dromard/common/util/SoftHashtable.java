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
public class SoftHashtable extends Hashtable {
    /** Serial UID. */
    private static final long serialVersionUID = -5402199747809994434L;

    /**
     * Retrieve the original object, if exists.
     * @param key The object key to retrieve.
     * @return The original object, if exists.
     */
    public final Object get(final Object key) {
        Object result = null;

        if (key != null) {
	        SoftReference softRef = (SoftReference) super.get(key);
	        if (softRef != null) {
	            result = softRef.get();
	            if (result == null) {
	                this.remove(key);
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
    public final Object put(final Object key, final Object value) {
        return (super.put(key, new SoftReference(value)));
    }

    /**
     * Remove an object with the given key.
     * @param key The object key to remove from map.
     * @return The soft reference removed.
     */
    public final Object remove(final Object key) {
        Object returnValue = null;
        SoftReference softRef = (SoftReference) super.get(key);

        if (softRef != null) {
            softRef.clear();
            returnValue = super.remove(key);
        }
        return returnValue;
    }

    /**
     * Clear this map.
     */
    public final void clear() {
        super.clear();
    }

    /**
     * Retrieve the number of SoftReference.
     * <font color="red"><b>Note:</b> An object store into a reference can be cleared, <b>but counted</b> because the reference can still exists even if the object has been cleared</font>
     * @return The number of SoftReference <font color="red"><b>Which can be different to the number of object</b></font>.
     */
    public final int size() {
        return super.size();
    }

    /**
     * Return the entryset of objects.
     * Note: not supported will throw an UnsupportedOperationException.
     * @return nothing.
     */
    public final Set entrySet() {
        throw new UnsupportedOperationException();
    }
}
