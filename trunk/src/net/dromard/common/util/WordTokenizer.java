package net.dromard.common.util;

import java.util.ArrayList;
import java.util.Enumeration;


/**
 * @author Gabriel Dromard (gabriel.dromard@airbus.com)
 * @version 1.0
 */
public class WordTokenizer implements Enumeration {
    /** The string to handle. */
    private String string;

    /** The field delimiter. */
    private String delimiter;

    /**
     * Constructor.
     * @param string The string to be parsed
     * @param delimiter A word used as the delimiter
     */
    public WordTokenizer(final String string, final String delimiter) {
        this.string = string;
        this.delimiter = delimiter;
    }

    /**
     * Does the line has more elements ?
     * @return true if there is still some elements.
     */
    public final boolean hasMoreElements() {
        return (string != null);
    }

    /**
     * Private methods that parse the line token.
     * @param index of current position into the line
     * @return the next token position.
     */
    private int getNextToken(final int index) {
        int tmp = string.indexOf(delimiter.charAt(0), index);
        if (tmp == -1) {
            //System.out.println("[DEBUG] <getNextToken("+index+")> returning string.length, there is more delimiters.");
            return string.length();
        }
        if (string.substring(tmp, string.length()).startsWith(delimiter)) {
            //System.out.println("[DEBUG] <getNextToken("+index+")> next token found at "+tmp);
            return tmp;
        }
        return getNextToken(tmp + 1);
    }

    /**
     * Retreive the next element.
     * @return The next element as String
     */
    public final Object nextElement() {
        int index = getNextToken(-1);
        String toReturn = string.substring(0, index);

        if (string.substring(index, string.length()).startsWith(delimiter)) {
            string = string.substring(index + delimiter.length(), string.length());
        } else {
            string = null;
        }
        return toReturn;
    }

    /**
     * Split a line using a delimiter into an ArrayList.
     * @param line The line to be splitted
     * @param delemiter The delimiter
     * @return The splitted result.
     */
    public static ArrayList split(final String line, final String delemiter) {
        WordTokenizer tokenizer = new WordTokenizer(line, delemiter);
        ArrayList list = new ArrayList();
        while (tokenizer.hasMoreElements()) {
            list.add(tokenizer.nextElement());
        }
        return list;
    }

    /**
     * Util method that count occurences of a word in a string.
     * @param line      The string.
     * @param occurence The word.
     * @return The number of occurences.
     */
    public static int countOccurence(final String line, final String occurence) {
        WordTokenizer tokenizer = new WordTokenizer(line, occurence);
        int count = 0;
        while (tokenizer.hasMoreElements()) {
            ++count;
            tokenizer.nextElement();
        }
        return count;
    }
}
