/*
 * Created on 8 févr. 2005
 * By Gabriel Dromard
 */
package net.dromard.common.util;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 * java.util.HashMap --> This implementation provides all of the optional map operations,
 * and permits null values and the null javaKey.
 * Hash table based implementation of the Map interface.
 * This implementation provides all of the optional map operations,
 * and permits null values and the null javaKey.
 *	(The HashMap class is roughly equivalent to Hashtable,
 *	except that it is unsynchronized and permits nulls.)
 * This class guarantees as to the order of the map; in particular,
 * it does guarantee that the order will remain constant over time.
 *
 * <br/>
 * <pre>
 *   +---------+
 *   | History |
 *   +---------+
 *
 * [8 févr. 2005] by Gabriel Dromard
 *   - Creation.
 * </pre><br/>
 * 
 * @author Gabriel Dromard
 */
public class OrderedHashMap extends HashMap {
	// internal
	private ArrayList keys;

    /**
     * @see #java.util.HashMap(Map)
     */
	public OrderedHashMap() { 
        super(); 
        keys = new ArrayList();
    }

    /**
     * @see #java.util.HashMap(Map)
     * @param map A map on which this instance will be based.  
     */
    @SuppressWarnings("unchecked")
    public OrderedHashMap(Map map) { 
		this();
        super.putAll(map);
    }

    /**
     * Retreive the keys as a list.
     * @return The keys
     */
    public final List keys() {
        return keys;
    }

    /**
     * Retreive the a HashMap element by its index.
     * @param columnIndex The HashMap element index
     * @return The HashMap Element
     */
    public final Object get(final int columnIndex) {
        return get(keys.get(columnIndex));
    }

	/**
	 * Associates the specified value with the specified javaKey in this map.
	 * If the map previously contained a mapping for this javaKey, the old value is replaced.
	 *
	 * @param key javaKey with which the specified value is to be associated.
	 * @param value value to be associated with the specified javaKey.
	 * @return previous value associated with specified javaKey, or <tt>null</tt>
	 *	       if there was no mapping for javaKey.  A <tt>null</tt> return can
	 *	       also indicate that the HashMap previously associated
	 *	       <tt>null</tt> with the specified javaKey.
	 */
    public final Object put(final Object key, final Object value) {
		if (!keys.contains(key)) {
            keys.add(key);
        }
		return super.put(key, value);
	}

    /**
     * Associates the specified value with the specified javaKey in this map adding it at the specified position.
     * If the map previously contained a mapping for this javaKey, the old value is replaced.
     *
     * @param position The index position (order).
     * @param key      javaKey with which the specified value is to be associated.
     * @param value    value to be associated with the specified javaKey.
     * @return previous value associated with specified javaKey, or <tt>null</tt>
     *         if there was no mapping for javaKey.  A <tt>null</tt> return can
     *         also indicate that the HashMap previously associated
     *         <tt>null</tt> with the specified javaKey.
     */
    public final Object put(final int position, final Object key, final Object value) {
        if (keys.size() <= position) {
            for (int i = keys.size(); i <= position; ++i) {
                keys.add(i, null);
            }
        }
        if (!keys.contains(key)) {
            keys.add(position, key);
        }
		return super.put(key, value);
	}

    /**
     * Copies all of the mappings from the specified Map to this Hashtable
     * These mappings will replace any mappings that this Hashtable had for any
     * of the keys currently in the specified Map. 
     *
     * @param map Mappings to be stored in this map.
     * @throws NullPointerException if the specified map is null.
     */
    public final void putAll(final Map map) {
        super.putAll(map);
        keys.addAll(keySet());
    }
    
    /**
     * Removes all of the elements from this Vector.  The Vector will
     * be empty after this call returns (unless it throws an exception).
     *
     * @see java.util.Hashtable#clear()
     */
    public final void clear() {
        keys.clear();
        super.clear();
    }
    
    /**
     * Returns a clone of this vector. The copy will contain a
     * reference to a clone of the internal data array, not a reference 
     * to the original internal data array of this <tt>Vector</tt> object. 
     *
     * @see java.util.Hashtable#clone()
     *
     * @return  a clone of this vector.
     */
    public final Object clone() {
        OrderedHashMap ret = (OrderedHashMap)super.clone();
        ret.keys = (ArrayList)keys.clone();
        return ret;
    }

	/**
	 * Remove one object from the list.
	 * @param key Remove the object with the given key
     * @return the removed object
	 */
	public Object remove(final Object key) {
	    keys.remove(key);
	    return super.remove(key);
	}
	
	/**
	 * @return	iterator view of the keys contained in this map.
	 * @see	java.util.Iterator
	 * @see ArrayList#iterator()
	 */
	public final Iterator iterator() {
		return keys.iterator();
	}

	/**
     * Sort keys with using a given comparator.
     * @param comparator The comparator.
     */
    public final void sortKeys(final Comparator comparator) {
        Collections.sort(keys, comparator);
    }

	/**
     * @param comparator An comparator to sort keys
	 * @return	iterator view of the keys contained in this map.
	 * @see	java.util.Iterator
	 * @see ArrayList#iterator()
	 */
    public final Iterator sortedIterator(final Comparator comparator) {
		sortKeys(comparator);
		return keys.iterator();
	}
}