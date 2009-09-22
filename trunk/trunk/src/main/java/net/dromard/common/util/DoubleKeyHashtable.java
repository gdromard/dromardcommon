/*
 * Created on 8 févr. 2005
 * By Gabriel Dromard
 */
package net.dromard.common.util;

/**
 * A Double key map so as to retreive object using one or an other key.
 * <br/>
 * <pre>
 *   +---------+
 *   | History |
 *   +---------+
 *
 * [09/02/2005] by Gabriel Dromard
 *   - Creation.
 * </pre><br/>
 * 
 * @author Gabriel Dromard
 */
public class DoubleKeyHashtable extends OrderedHashtable {
	// so as to clear warnings ...
	static final long serialVersionUID = 0;
	// internal
	private final OrderedHashtable doublekeys = new OrderedHashtable();

	public DoubleKeyHashtable() { super(); }


	/**
	 * Remove one object from the list.
	 * @param key Remove the object with the given key
	 */
	public Object remove(Object key) {
		doublekeys.remove(key);
		return super.remove(key);
	}

	/**
	 * Put an object in the map
	 * @param key1 The first key
	 * @param key2 The second key
	 * @param value The value
	 * @see java.util.Hashtable#put(java.lang.Object, java.lang.Object)
	 */
	public Object put(Object key1, Object key2, Object value) {
		doublekeys.put(key2, key1);
		return super.put(key1, value);
	}

	public Object get(Object key) {
		Object o = super.get(key);
		if(o == null) {
			Object scd = doublekeys.get(key);
			if(scd != null) o = super.get(scd);
		}
		return o;
	}
}