package net.dromard.common.util;

import java.util.StringTokenizer;

public class TextWrapper {
    /**
     * Wrap text at a certain line length.
     * @param string The String
     * @param lineSeparator Line break
     * @param lineLength The position to create a line break
     * @return String
     */
    public static String wrap(final String string, final String lineSeparator, final int lineLength) {
        if (string == null || string.trim().equals("")) {
            return "";
        }
        if (lineLength < 2) {
            return string;
        }

        final String DELIM = String.valueOf((char) Character.CONTROL);

        if (string.startsWith(DELIM) && string.endsWith(DELIM)) {
            return string;
        }

        StringBuffer sb = new StringBuffer();

        if (string.replaceAll(DELIM, "").length() > lineLength) {
            StringTokenizer st = new StringTokenizer(string.replaceAll(lineSeparator, DELIM), DELIM);
            int totalTokens = st.countTokens();
            int count = 0;

            while (st.hasMoreTokens()) {
                String s = st.nextToken();
                count++;

                while (s.length() > lineLength) {
                    sb.append(s.substring(0, lineLength));
                    sb.append(DELIM);
                    s = s.substring(lineLength, s.length());
                }
                sb.append(s);

                if (count != totalTokens) {
                    sb.append(DELIM);
                }
            }

            return sb.toString().replaceAll(DELIM, lineSeparator);
        } else {
            return string.replaceAll(DELIM, lineSeparator);
        }
    }

    /**
     * Wrap text at a certain line length without cutting words.
     * @param string The String
     * @param lineSeparator Line break
     * @param lineLength The position to create a line break
     * @return String
     */
    public static String wrapWord(final String string, final String lineSeparator, final int lineLength) {
        if (string == null || string.trim().equals("")) {
            return "";
        }
        if (lineLength < 2) {
            return string;
        }

        final String DELIM = String.valueOf((char) Character.CONTROL);
        final char WORD_SEPARATOR = ' ';

        if (string.startsWith(DELIM) && string.endsWith(DELIM)) {
            return string;
        }

        StringBuffer sb = new StringBuffer();

        if (string.replaceAll(DELIM, "").length() > lineLength) {
            StringTokenizer st = new StringTokenizer(string.replaceAll(lineSeparator, DELIM), DELIM);
            int totalTokens = st.countTokens();
            int count = 0;

            while (st.hasMoreTokens()) {
                String s = st.nextToken();
                count++;

                while (s.length() > lineLength) {
                    int localLineLength = s.substring(0, lineLength).lastIndexOf(WORD_SEPARATOR);
                    sb.append(s.substring(0, localLineLength));
                    sb.append(DELIM);
                    s = s.substring(localLineLength + 1, s.length());
                }
                sb.append(s);

                if (count != totalTokens) {
                    sb.append(DELIM);
                }
            }

            return sb.toString().replaceAll(DELIM, lineSeparator);
        } else {
            return string.replaceAll(DELIM, lineSeparator);
        }
    }

    public static void main(final String[] args) throws Exception {
        System.out.println(TextWrapper.wrap("TEST\nThis is a little test of text wrapping.", "\n", 10));
        System.out.println(TextWrapper.wrap("----------------------------------------", "\n", 10));
        System.out.println(TextWrapper.wrapWord("TEST\nThis is a little test of text wrapping.", "\n", 10));
    }
}