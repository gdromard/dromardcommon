/**
 * 	File : SoftHashtable.java 11 juin 07
 */

package net.dromard.common.util;

import java.lang.ref.SoftReference;
import java.util.Hashtable;

/**
 * Hash table of soft references.
 * <br>
 * @author Gabriel Dromard
 */
public class SoftHashtable<K, T> {
    /** Serial UID. */
    private static final long serialVersionUID = -5402199747809994434L;

    private final Hashtable<K, SoftReference<T>> softHashtable = new Hashtable<K, SoftReference<T>>();

    /**
     * Retrieve the original object, if exists.
     * @param key The object key to retrieve.
     * @return The original object, if exists.
     */
    public final T get(final K key) {
        T result = null;

        if (key != null) {
            SoftReference<T> softRef = softHashtable.get(key);
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
    public final void put(final K key, final T value) {
        softHashtable.put(key, new SoftReference<T>(value));
    }

    /**
     * Remove an object with the given key.
     * @param key The object key to remove from map.
     * @return The soft reference removed.
     */
    public final T remove(final K key) {
        T returnValue = null;
        SoftReference<T> softRef = softHashtable.get(key);
        if (softRef != null) {
            softRef.clear();
            returnValue = softHashtable.remove(key).get();
        }
        return returnValue;
    }

    /**
     * Clear this map.
     */
    public final void clear() {
        softHashtable.clear();
    }

    /**
     * Retrieve the number of SoftReference.
     * <font color="red"><b>Note:</b> An object store into a reference can be cleared, <b>but counted</b> because the reference can still exists even if the object has been cleared</font>
     * @return The number of SoftReference <font color="red"><b>Which can be different to the number of object</b></font>.
     */
    public final int size() {
        return softHashtable.size();
    }
}
