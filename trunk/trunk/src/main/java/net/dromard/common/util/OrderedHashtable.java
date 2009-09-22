/*
 * Created on 8 févr. 2005
 * By Gabriel Dromard
 */
package net.dromard.common.util;

import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

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
public class OrderedHashtable extends Hashtable {

    /** Internal ordered keys. */
	private Vector keys;


    /**
     * @see java.util.Hashtable
     */
	public OrderedHashtable() { 
        super();
        keys = new Vector();
    }

    /**
     * @see java.util.Hashtable
     * @param map A existing map to fill the new one
     */
    @SuppressWarnings("unchecked")
    public OrderedHashtable(final Map map) { 
        super(map); 
        keys = new Vector(keySet());
    }
    
    /**
     * Retreive the a HashMap element by its index
     * @param columnIndex The HashMap element index
     * @return The HashMap Element
     */
    public final Object get(final int columnIndex) { 
        return get(keys.get(columnIndex));
    }


    /**
     * Returns an enumeration of the keys in this hashtable.
     *
     * @return  an enumeration of the keys in this hashtable.
     * @see java.util.Enumeration
     * @see java.util.Hashtable#elements()
     * @see java.util.Hashtable#keySet()
     * @see java.util.Map
     */
    public final synchronized Enumeration keys() {
        return keys.elements();
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
	@SuppressWarnings("unchecked")
    public final Object put(final Object key, final Object value) {
		if (!keys.contains(key)) {
            keys.add(key);
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
    @SuppressWarnings("unchecked")
    public final void putAll(final Map map) {
        System.out.println("putting all element from ");
        super.putAll(map);
        keys.addAll(keySet());
    }
    
	/**
	 * Remove one object from the list.
	 * @param key Remove the object with the given key
	 */
	public Object remove(final Object key) {
		keys.remove(key); //System.out.println("[OrderedHashtable] Remove of object with key "+key+" ... "+((keys.remove(key))?"[DONE]":"[FAILED]"));
		return super.remove(key);
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
        OrderedHashtable ret = (OrderedHashtable)super.clone();
        ret.keys = (Vector)keys.clone();
        return ret;
    }

	/**
	 * @return An iterator view of the keys contained in this map.
	 * @see	java.util.ArrayList#iterator()
	 */
	public final Iterator iterator() {
		return keys.iterator();
	}

	/**
     * @param comparator An comparator to sort keys
	 * @return	iterator view of the keys contained in this map.
	 * @see	java.util.Iterator
	 * @see java.util.ArrayList#iterator()
	 */
	@SuppressWarnings("unchecked")
    public final Iterator sortedIterator(final Comparator comparator) {
		Collections.sort(keys, comparator);
		return keys.iterator();
	}
}