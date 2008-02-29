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
 * 07/09/2007 by Gabriel Dromard
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
     * @param original The original string to parse.
     * @param pattern The pattern to match.
     * @param value The string that will replace the pattern.
     * @return The string with pattern replaced by value.
     * @since 11/03/2005
     */
    public static String replaceAll(final String original, final String pattern, final String value) {
        String copy = new String(original);
        int index = -1;
        while ((index = copy.indexOf(pattern, index)) > -1) {
            copy = copy.substring(0, index) + value + copy.substring(index + pattern.length());
            index += value.length();
        }
        return copy;
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
     * Permet de tester si une chaine de caractére est numérique ou pas !
     * @param source Chaine de caractére à tester
     * @return Un boolean disant si la chaine est numérique ou pas !
     */
    public static boolean isNumeric(final String source) {
        try {
            Double.parseDouble(source.trim());
        } catch (Exception ex) {
            return false;
        }
        return true;
    }

    /**
     * Methodes permettant de récupérer le début de la chaine jusqu'au mot donnée.
     * @param source Chaine de caractere 'source'
     * @param word Mot à rechercher dans la chaine 'source' (Recherche de la premiére occurence)
     * @return Chaine de caractere positionnée avant le mot à rechercher
     */
    public static String subStringBefore(final String source, final String word) {
        return subStringBefore(source, word, false);
    }

    /**
     * Retreive the sub string before a given word from the given source string.<br>
     * <b>Note:</b> If the word does not exists in it will return the original source.<br>
     * If the given source or word string is null it will return null also.
     * @param source Chaine de caractere 'source'
     * @param word Mot à rechercher dans la chaine 'source'
     * @param theLast Recherche de la derniere occurence du mot ou pas !
     * @return Chaine de caractere positionnée avant le mot à rechercher
     */
    public static String subStringBefore(final String source, final String word, final boolean theLast) {
        // Verification des parametres d'entrée
        if (source == null || word == null) {
            return null;
        }

        // Recherche de la premiére occurrence du mot dans la chaine
        int index;
        if (theLast) {
            index = source.lastIndexOf(word);
        } else {
            index = source.indexOf(word);
        }
        if (index >= 0) {
            return source.substring(0, index);
        }

        // debug("[subStringBefore] source ("+source+") ne contiend pas le mot : '"+word+"'");
        return source;
    }

    /**
     * Methodes permettant de récupérer la chaine de caractére se trouvant entre deux mots.
     * @param source Chaine de caractere 'source'
     * @param firstWord Premier mot de référence à rechercher dans la chaine 'source'
     * @param secondWord Second mot de référence à rechercher dans la chaine 'source'
     * @return
     * <LI>La chaine de caractére se trouvant entre deux mots</LI>
     * <LI>Chaine de caractere positionnée avant le second mot recherché si le premier n'existe pas</LI>
     * <LI>Chaine de caractere positionnée après le premier mot recherché si le second n'existe pas</LI>
     * <LI>'<b><font color='darkblue'>null</font></b>' si les deux mots n'ont pas été trouvés</LI>
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
     * Methodes permettant de savoir s'il existe un mot dans une chaine de caractére.
     * @param source Chaine de caractere 'source'
     * @param word Mot à rechercher dans la chaine 'source' (Recherche de la premiére occurence)
     * @return Chaine de caractere positionnée après le mot à rechercher
     */
    public static String subStringAfter(final String source, final String word) {
        return subStringAfter(source, word, false);
    }

    /**
     * Methodes permettant de savoir s'il existe un mot dans une chaine de caractére.
     * @param source Chaine de caractere 'source'
     * @param word Mot à rechercher dans la chaine 'source' (Recherche de la premiére occurence)
     * @param theLast Recherche de la derniere occurence du mot ou pas !
     * @return Chaine de caractere positionnée après le mot à rechercher
     */
    public static String subStringAfter(final String source, final String word, final boolean theLast) {
        // Verification des parametres d'entrée
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
     * This is a static tool method to split a string in an array.
     * @param string The original string to split.
     * @param delimiters The token to use to delimit the different fields, each characters are used as one token.
     * @return A string array.
     * @deprecated This implementation does not return an empty element if 2 delimiters are join
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
     * This function replace all 'fields' by its coresponding 'values' in the source string.
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
     * This function replace all 'word' by an other ('replace') in the source string.
     * @param source Source String.
     * @param word Word to be replaced.
     * @param replace Word replaced.
     * @return The String with <i>'word'</i> replaced by <i>'replace'</i>.
     * @deprecated use replaceAll public static String replace(String source, String word, String replace) { return replaceAll(source, word, replace); /* //
     * Take a char that is not the token, add it at the end so as to take in account the last word // if it's at the end of the string (because the
     * split() forget it if it is at the end) boolean removeLastChar = source.lastIndexOf(word) == source.length()-word.length(); if(removeLastChar)
     * source = source.substring(0, source.length()) + (char)(word.charAt(0)+1);
     *
     * Object[] split = split(source, word); String toReturn = ""; for(int i=0; i<split.length; i++) { toReturn += split[i]; if(i<split.length-1)
     * toReturn+=replace; } if(removeLastChar) toReturn = toReturn.substring(0, toReturn.length()-1); return toReturn;
     */
    // }
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
     *
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
     *
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
     *
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
     *
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
     *
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
     * @param separator The string separator to be used for convertion.
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
     * Capitalize a string by setting it to lowercase and upcasing the first characters.
     * The first character as well as all character following one of the defined delimiter.
     * are switched to upercase.
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
}
