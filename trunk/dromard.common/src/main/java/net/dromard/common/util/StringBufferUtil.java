/*
 * Created on 8 mars 2005
 * By Gabriel Dromard
 */
package net.dromard.common.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * A StringBuffer util class.
 * <br/>
 * <pre>
 *   +---------+
 *   | History |
 *   +---------+
 *
 * [14/04/2005] by Gabriel Dromard
 *   - Add method convertArrayListWithHTMLEscape().
 *
 * [08/03/2005] by Gabriel Dromard
 *   - Creation.
 * </pre><br/>
 * 
 * @author Gabriel Dromard
 */
public class StringBufferUtil {

	/**
	 * Perform the substitution between hashmap element and hashmap keys in the buffer.
	 * Hasmap keys must be prefixed and subfixed with an '%' character in the buffer.
	 * @param string     string to be substituted.
	 * @param substitute Substitution hasmap. 
	 */
	public static final StringBuffer performSubstitution(String string, HashMap substitute) {
		StringBuffer buffer = new StringBuffer(string);
		/*
		 * follow the frameBuffer character to find '%' char in order to perform
		 * substitution
		 */
		int bufferPosition = buffer.length() - 1;
		/* ------------ start / end of the substitution ---------- */
		int beginSub = -1;
		int endSub = -1;
		for(; bufferPosition > 0; bufferPosition--) {
			/* we find a substitution tag */
			if(buffer.charAt(bufferPosition) == '%' && buffer.charAt(bufferPosition - 1) != '\\') {
				/* two case, begin of a substitution or end of a substitution */
				if(endSub < 0) {
					/* start of a substitution */
					endSub = bufferPosition;
				} else {
					/* end of substitution */
					beginSub = bufferPosition;
					substituteBuffer(buffer, beginSub, endSub, substitute);
					beginSub = -1;
					endSub = -1;
				}
			}
		}
		return buffer;
	}

	/**
	 * Substitute the character between beginSub and endSub by the given value in the HashMap
	 *
	 * @param buffer
	 * @param beginSub
	 * @param endSub
	 * @param substitute
	 */
	private static final void substituteBuffer(StringBuffer buffer, int beginSub, int endSub, HashMap substitute) {
		if(substitute != null) {
			String key = buffer.substring(beginSub + 1, endSub);
			/* here, handle String and String table */
			Object value = substitute.get(key);
			String stringValue = null;
			if(value != null) {
				if(value instanceof String) stringValue = (String) value;
				if(value instanceof String[]) {
					stringValue = "(";
					String[] stringTable = (String[]) value;
					for(int i = 0; i < stringTable.length; i++) {
						stringValue += stringTable[i];
						if(i < (stringTable.length - 1))
							stringValue += ", ";
					}
					stringValue += ")";
				}
				buffer.replace(beginSub, endSub+1, stringValue);
			}
			// add null value substitution
			else buffer.replace(beginSub, endSub+1, "");
		}
	}

	/**
	 * Convert an arraylist into a string.
	 * The array [toto, titi, tutu] will be converted into string: toto#titi#tutu if you use '#' as separator. 
	 * @param list The list to convert
	 * @param elementSeparator The separator to use between each elements.
	 * @return A String buffer.
	 */
	public static final StringBuffer convertArrayList(ArrayList list, String elementSeparator) {
		if(list == null) return null;
		StringBuffer buffer = new StringBuffer();
		Iterator it = list.iterator();
		if(it.hasNext()) buffer.append(it.next());
		else return null;
		while(it.hasNext()) buffer.append(elementSeparator+it.next());
		return buffer;
	}

	/**
	 * Convert an arraylist into a string.
	 * The array [toto, titi, tutu] will be converted into string: toto#titi#tutu if you use '#' as separator. 
	 * @param list The list to convert
	 * @param elementSeparator The separator to use between each elements.
	 * @return A String buffer.
	 */
	public static final StringBuffer convertArrayListWithHTMLEscape(ArrayList list, String elementSeparator) {
		if(list == null) return null;
		StringBuffer buffer = new StringBuffer();
		Iterator it = list.iterator();
		if(it.hasNext()) buffer.append(StringEscapeHelper.escapeHTML((String)it.next()));
		else return null;
		while(it.hasNext()) buffer.append(elementSeparator+StringEscapeHelper.escapeHTML((String)it.next()));
		return buffer;
	}
}
