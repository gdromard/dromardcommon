package net.dromard.common.util;

import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.Vector;

/**
 * Utilities for String formatting, manipulation, and queries. <br>
 *
 * <pre>
 *   +---------+
 *   | History |
 *   +---------+
 *
 * 07/04/2008 by Cyril Ronseaux
 * Add method:
 *  - String join(final Object[] array, final String delimiter)
 *  - String getSimpleName(final Class c)
 * Changed boolean equals(final String s1, final String s2) to deprecated. It already
 *  exists in StringComparator.
 *
 * 16/08/2007 by Matthieu Salvador
 *  Add method:
 *  - String capitalize(String original, Char[] sperator)
 *
 * 20/04/2005 by Gabriel Dromard
 *  Add methods:
 *   - String replaceAll(String original, String pattern, String value)
 *   - String replaceFirst(String original, String pattern, String value)
 *  Change methods replace() to deprecated (now it call the replaceAll())
 *
 * 07/04/2005 by Gabriel Dromard
 *  Add methods:
 *   - String countOcurence()
 *
 * 22/11/2004 by Gabriel Dromard
 *  Resolve a bug in the 'replace' methods.
 *  When the word to replace is at the end of the string, it's skipped by the split() method.
 *
 * 08/09/2004 by Gabriel Dromard
 *  Add methods:
 *   - String escapeLiteral(String s, String escapeChars)
 *   - String unescapeLiteral(String s, String escapeChars)
 *
 * 04/03/2004 by Gabriel Dromard
 *  Created.
 *
 * </pre>
 *
 * <br>
 */
public final class StringHelper {
    /**
     * Empty private constructor for util class.
     */
    private StringHelper() {
        // Empty private constructor for util class.
    }

    /**
     * Replace all substring (pattern) found in string (original) with a given string (value).
     * All parameters are expected (must not be null)
     * @param original The original string to parse.
     * @param pattern The pattern to match.
     * @param value The string that will replace the pattern.
     * @return The string with pattern replaced by value.
     * @since 11/03/2005
     */
    public static String replaceAll(final String original, final String pattern, final String value) {
        StringBuffer copy = new StringBuffer(original);
        int index = -1;
        while ((index = copy.indexOf(pattern, index)) > -1) {
            copy.replace(index, index + pattern.length(), value);
            index += value.length();
        }
        return copy.toString();
    }

    /**
     * Design for display, it return the begining of the string.
     * @param string The string.
     * @param nbChar The number od characters to be kept.
     * @return The substring if it is bigger than the given number of characters.
     */
    public static String displayFirstCharacters(final String string, final int nbChar) {
        if (string.length() > Math.max(nbChar, " ...".length())) {
            return string.substring(0, nbChar - " ...".length()) + " ...";
        }
        return string;
    }

    /**
     * Replace first substring (pattern) found in string (original) with a given string (value).
     * @param original The original string to parse.
     * @param pattern The pattern to match.
     * @param value The string that will replace the pattern.
     * @return The string with pattern replaced by value.
     * @since 11/03/2005
     */
    public static String replaceFirst(final String original, final String pattern, final String value) {
        String copy = new String(original);
        int index = copy.indexOf(pattern);
        if (index > -1) {
            return copy.substring(0, index) + value + copy.substring(index + pattern.length());
        }
        return copy;
    }

    /**
     * Count the number of occurences of one characters into the string.
     * @param string the string where to count the charracter car occurences
     * @param car the character to count in the String.
     * @return the number of occurences
     * @since 11/03/2005
     */
    public static int countOccurences(final String string, final char car) {
        String copy = string;
        int count = 0;
        while (copy.indexOf(car) > -1) {
            ++count;
            copy = copy.substring(copy.indexOf(car) + 1);
        }
        return count;
    }

    /**
     * Count the number of occurences of one word into the source string.
     * @param source The string where to count the word occurences
     * @param word   The word to count in the String.
     * @return       The number of occurences
     */
    public static int countOccurences(final String source, final String word) {
        if (source == null || word == null) {
            return 0;
        }
        int count = 0;
        int index = source.indexOf(word);
        while (index > -1) {
            count++;
            index = source.indexOf(word, index + word.length());
        }
        return count;
    }

    /**
     * Test if the string is a number of type double.
     * @param source String to be tested
     * @return A boolean saying if the string has been successfully parsed
     */
    public static boolean isNumeric(final String source) {
        try {
            Double.parseDouble(source);
        } catch (Exception ex) {
            return false;
        }
        return true;
    }

    /**
     * Retrieve the sub string starting at index 0 ending before the given word.
     * @param source The string to be parsed
     * @param word The token
     * @return The substring if the word exists else the entire source string
     */
    public static String subStringBefore(final String source, final String word) {
        return subStringBefore(source, word, false);
    }

    /**
     * Retrieve the sub string starting at index 0 ending before the given word.
     * <b>Note:</b> If the word does not exists in it will return the original source.<br>
     * If the given source or word string is null it will return null also.
     * @param source The string to be parsed
     * @param word The token
     * @param theLast true to get the last word as token (if it is present more than one time)
     * @return The substring if the word exists else the entire source string.
     */
    public static String subStringBefore(final String source, final String word, final boolean theLast) {
        int index;
        if (theLast) {
            index = source.lastIndexOf(word);
        } else {
            index = source.indexOf(word);
        }
        if (index >= 0) {
            return source.substring(0, index);
        }
        return source;
    }

    /**
     * Retrieve the substring which is placed between two word.
     * @param source The string to be parsed
     * @param firstWord The first token
     * @param secondWord The second token
     * @return the substring which is placed between two word,
     * or the substring before the second word if the first one is not present,
     * or the substring after the first word if the second one is not present,
     * or the source string if no words are founds.
     */
    public static String subStringBeetwen(final String source, final String firstWord, final String secondWord) {
        String src = source;
        String result = subStringAfter(src, firstWord);
        if (result != null) {
            src = result;
            result = subStringBefore(src, secondWord);
            if (result == null) {
                result = src;
            }
        } else {
            result = subStringBefore(src, secondWord);
        }
        return result;
    }

    /**
     * Retrieve the sub string starting before the given word ending at the end of the source string.
     * <b>Note:</b> If the word does not exists in it will return the original source.<br>
     * If the given source or word string is null it will return null also.
     * @param source The string to be parsed
     * @param word The token
     * @return The substring if the word exists else the entire source string.
     */
    public static String subStringAfter(final String source, final String word) {
        return subStringAfter(source, word, false);
    }

    /**
     * Retrieve the sub string starting before the given word ending at the end of the source string.
     * <b>Note:</b> If the word does not exists in it will return the original source.<br>
     * If the given source or word string is null it will return null also.
     * @param source The string to be parsed
     * @param word The token
     * @param theLast true to get the last word as token (if it is present more than one time)
     * @return The substring if the word exists else the entire source string.
     */
    public static String subStringAfter(final String source, final String word, final boolean theLast) {
        if (source == null || word == null) {
            return null;
        }

        int index;
        if (theLast) {
            index = source.lastIndexOf(word);
        } else {
            index = source.indexOf(word);
        }
        if (index >= 0) {
            return source.substring(index + word.length(), source.length());
        }
        return null;
    }

    /**
     * Pads out a string upto padLength with pad chars.
     * @param string String to be padded
     * @param padLength Length of pad (+ve = pad on right, -ve pad on left)
     * @param pad The pad character
     * @return the string padded
     * @since 11/03/2005
     */
    public static String pad(final String string, final int padLength, final String pad) {
        String padding = new String();
        int len = Math.abs(padLength) - string.length();
        if (len < 1) {
            return string;
        }
        for (int i = 0; i < len; ++i) {
            padding = padding + pad;
        }
        if (padLength < 0) {
            return padding + string;
        }
        return string + padding;
    }

    /**
     * This is a static tool method to split a string in an array. NOTA: This implementation does not return an empty element if 2 delimiters are join
     * @param string The original string to split.
     * @param delimiters The token to use to delimit the different fields, each characters are used as one token.
     * @return A string array.
     */
    public static String[] normalSplit(final String string, final String delimiters) {
        StringTokenizer tokenizer = new StringTokenizer(string, delimiters);
        Vector fields = new Vector();
        while (tokenizer.hasMoreElements()) {
            fields.add(tokenizer.nextElement());
        }
        String[] result = new String[fields.size()];
        for (int i = 0; i < result.length; ++i) {
            result[i] = (String) fields.get(i);
        }
        return result;
    }

    /**
     * This is a static tool method to split a string in an array.
     * @param string The original string to split.
     * @param delimiter The token to use to delimit the different fields the entire string is the delimiter.
     * @return A string array.
     */
    public static String[] split(final String string, final String delimiter) {
        WordTokenizer tokenizer = new WordTokenizer(string, delimiter);
        Vector fields = new Vector();
        while (tokenizer.hasMoreElements()) {
            fields.add(tokenizer.nextElement());
        }
        String[] result = new String[fields.size()];
        for (int i = 0; i < result.length; ++i) {
            result[i] = (String) fields.get(i);
        }
        return result;
    }

    /**
     * @param array array to merge into a string.
     * @param delimiter delimiter to place between two items of the array. if <code>null</code> no delimiter is used.
     * @return a string representation of the array.
     */
    public static String join(final Object[] array, final String delimiter) {
        if (array == null) {
            return null;
        }

        StringBuffer joined = new StringBuffer("");
        if (array.length > 0) { // has at least one element.
            joined.append(array[0]);
            for (int i = 1; i < array.length; i++) {
                if (delimiter != null) {
                    joined.append(delimiter);
                }
                joined.append(array[i]);
            }
        }
        return joined.toString();
    }

    /**
     * This function replace all 'fields' by its corresponding 'values' in the source string.
     * @param source Source String.
     * @param fields Fields to be replaced.
     * @param values Values of each fields.
     * @return The String with all <i>'fields'</i> replaced by its corresponding <i>'value'</i>.
     */
    public static String replace(final String source, final String[] fields, final String[] values) {
        String toReturn = source;
        for (int i = 0; i < fields.length; ++i) {
            toReturn = replaceAll(toReturn, fields[i], values[i]);
        }
        return toReturn;
    }

    /**
     * Trim any of the characters contained in the second string from the beginning and end of the first.
     *
     * @param s String to be trimmed.
     * @param c list of characters to trim from s.
     * @return trimmed String.
     */
    public static String trim(final String s, final String c) {
        int length = s.length();
        if (c == null) {
            return s;
        }
        int cLength = c.length();
        if (c.length() == 0) {
            return s;
        }
        int start = 0;
        int end = length;
        boolean found; // trim-able character found.
        int i;
        // Start from the beginning and find the
        // first non-trim-able character.
        found = false;
        for (i = 0; !found && i < length; ++i) {
            char ch = s.charAt(i);
            found = true;
            for (int j = 0; found && j < cLength; j++) {
                if (c.charAt(j) == ch) {
                    found = false;
                }
            }
        }
        // if all characters are trim-able.
        if (!found) {
            return "";
        }
        start = i - 1;
        // Start from the end and find the
        // last non-trim-able character.
        found = false;
        for (i = length - 1; !found && i >= 0; i--) {
            char ch = s.charAt(i);
            found = true;
            for (int j = 0; found && j < cLength; j++) {
                if (c.charAt(j) == ch) {
                    found = false;
                }
            }
        }
        end = i + 2;
        return s.substring(start, end);
    }

    /**
     * Returns whether the string holds no valuable data.
     * @param string string under test.
     * @return <code>true</code> if string is null, zero-length or can be trimmed to zero-length. <code>false</code> otherwise.
     */
    public static boolean isEmpty(final String string) {
        // Try to optimize by limiting the call to the trim() method !
        return string == null || string.length() == 0 || string.trim().length() == 0;
    }

    /**
     * Returns whether the string holds no valuable data.
     * @param strings strings under test.
     * @return <code>true</code> if string is null, zero-length or can be trimmed to zero-length. <code>false</code> otherwise.
     */
    public static boolean isEmpty(final String[] strings) {
        return strings == null || strings.length == 0;
    }

    /**
     * Pad the beginning of the given String with spaces untilthe String is of the given length.
     * <p>
     * If a String is longer than the desired length, it will not be truncated, however no padding will be added.
     *
     * @param s String to be padded.
     * @param length desired length of result.
     * @return padded String.
     */
    public static String prepad(final String s, final int length) {
        return prepad(s, length, ' ');
    }

    /**
     * Pre-pend the given character to the String until the result is the desired length.
     * <p>
     * If a String is longer than the desired length, it will not be truncated, however no padding will be added.
     * @param s String to be padded.
     * @param length desired length of result.
     * @param c padding character.
     * @return padded String.
     */
    public static String prepad(final String s, final int length, final char c) {
        int needed = length - s.length();
        if (needed <= 0) {
            return s;
        }
        StringBuffer sb = new StringBuffer(length);
        for (int i = 0; i < needed; ++i) {
            sb.append(c);
        }
        sb.append(s);
        return (sb.toString());
    }

    /**
     * Pad the end of the given String with spaces until the String is of the given length.
     * <p>
     * If a String is longer than the desired length, it will not be truncated, however no padding will be added.
     * @param s String to be padded.
     * @param length desired length of result.
     * @return padded String.
     */
    public static String postpad(final String s, final int length) {
        return postpad(s, length, ' ');
    }

    /**
     * Append the given character to the String until the result is the desired length.
     * <p>
     * If a String is longer than the desired length, it will not be truncated, however no padding will be added.
     * @param s String to be padded.
     * @param length desired length of result.
     * @param c padding character.
     * @return padded String.
     */
    public static String postpad(final String s, final int length, final char c) {
        int needed = length - s.length();
        if (needed <= 0) {
            return s;
        }
        StringBuffer sb = new StringBuffer(length);
        sb.append(s);
        for (int i = 0; i < needed; i++) {
            sb.append(c);
        }
        return (sb.toString());
    }

    /**
     * Pad the beginning and end of the given String with spaces until the String is of the given length. The result is that the original String is
     * centered in the middle of the new string.
     * <p>
     * If the number of characters to pad is even, then the padding will be split evenly between the beginning and end, otherwise, the extra character
     * will be added to the end.
     * <p>
     * If a String is longer than the desired length, it will not be truncated, however no padding will be added.
     * @param s String to be padded.
     * @param length desired length of result.
     * @return padded String.
     */
    public static String midpad(final String s, final int length) {
        return midpad(s, length, ' ');
    }

    /**
     * Pad the beginning and end of the given String with the given character until the result is the desired length. The result is that the original
     * String is centered in the middle of the new string.
     * <p>
     * If the number of characters to pad is even, then the padding will be split evenly between the beginning and end, otherwise, the extra character
     * will be added to the end.
     * <p>
     * If a String is longer than the desired length, it will not be truncated, however no padding will be added.
     * @param s String to be padded.
     * @param length desired length of result.
     * @param c padding character.
     * @return padded String.
     */
    public static String midpad(final String s, final int length, final char c) {
        int needed = length - s.length();
        if (needed <= 0) {
            return s;
        }
        int beginning = needed / 2;
        int end = beginning + needed % 2;
        StringBuffer sb = new StringBuffer(length);
        for (int i = 0; i < beginning; i++) {
            sb.append(c);
        }
        sb.append(s);
        for (int i = 0; i < end; i++) {
            sb.append(c);
        }
        return (sb.toString());
    }

    /**
     * Convert an array to a string using given separator.
     * @param array     The array to convert
     * @param separator The string separator to be used for conversion.
     * @return the coverage
     */
    public static String arrayToString(final Object[] array, final String separator) {
        String res = "";
        for (int i = 0; i < array.length; i++) {
            // Concat each element
            res += array[i];
            // Add separator if necessary
            if ((i < array.length - 1)) {
                res += separator;
            }
        }
        return res;
    }

    /**
     * Concat iterator values in a string to a string using given separator.
     * @param iterator  The iterator to be concat in a string
     * @param separator The string separator to be used for convertion.
     * @return the coverage
     */
    public static String iteratorToString(final Iterator iterator, final String separator) {
        StringBuffer res = new StringBuffer("");
        while (iterator.hasNext()) {
            // Concat each element
            res.append(iterator.next());
            res.append(separator);
        }
        if (res.length() > separator.length()) {
	        // remove last iterator
	        res.delete(res.length() - separator.length(), res.length());
	    }
        return res.toString();
    }

    /**
     * Capitalize a string by setting it to lower case and upcasing the first characters. The first character as well as all character following one of
     * the defined delimiter. are switched to upper case.
     * @param original string to capitalize
     * @param separator set of characters considered as separators to identify individual words.
     * @return Capitalized string.
     */
    public static String capitalize(final String original, final char[] separator) {
        String str = null;

        if (original == null || original.length() == 0) {
            return str;
        }
        str = original.toLowerCase();
        int strLen = str.length();
        StringBuffer buffer = new StringBuffer(strLen);

        int separatorLen = 0;
        if (separator != null) {
            separatorLen = separator.length;
        }

        boolean capitalizeNext = true;
        for (int i = 0; i < strLen; i++) {
            char ch = str.charAt(i);

            boolean isSeparator = false;
            if (separator == null) {
                isSeparator = Character.isWhitespace(ch);
            } else {
                for (int j = 0; j < separatorLen; j++) {
                    if (ch == separator[j]) {
                        isSeparator = true;
                        break;
                    }
                }
            }

            if (isSeparator) {
                buffer.append(ch);
                capitalizeNext = true;
            } else if (capitalizeNext) {
                buffer.append(Character.toTitleCase(ch));
                capitalizeNext = false;
            } else {
                buffer.append(ch);
            }
        }
        return buffer.toString();
    }

    /**
     * @param c Class we want the name of.
     * @return the simple name of a class, i.e. its name without package.
     */
    public static String getSimpleName(final Class c) {
        return c.getName().substring(c.getName().lastIndexOf('.') + 1);
    }

}
