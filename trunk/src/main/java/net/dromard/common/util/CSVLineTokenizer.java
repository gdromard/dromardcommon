package net.dromard.common.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;


/**
 * This class is design to parse a CSV line.
 * @author Gabriel Dromard
 * @version 1.0
 */
public class CSVLineTokenizer implements Enumeration {
    /** The string to handle. */
    private String string;

    /** The default token. */
    private char token = ',';

    /** The default quote. */
    private char quote = '\'';

    /** Tells if the quotes are keeped. */
    private boolean keepQuotes = false;

    /** The possible token. */
    private static final String POSSIBLE_TOKENS = ",;|";

    /** The possible quotes. */
    private static final String POSSIBLE_QUOTES = "'\"";

    /**
     * Constructs a string tokenizer for the specified string.
     * The default token is <code>","</code> and the default quote is <code>"'"</code>
     * But this constructor try to find the correct quote and token used. (by counting occurences)
     * By default the quote characters are trimed !!
     * @param string the string to be parsed
     */
    public CSVLineTokenizer(final String string) {
        this.string = string;

        // Find token used
        try {
            Character cToken = getTheMostOccurences(string, POSSIBLE_TOKENS);
            if (cToken != null) {
                token = cToken.charValue();
            }
            // System.out.println("Token found in <"+POSSIBLE_TOKENS+"> is: "+token);
        } catch (Exception e) {
            System.out.println("Token have not been found in <" + POSSIBLE_TOKENS + ">");
            e.printStackTrace();
        }
        // Find quote used
        try {
            Character cQuote = getTheMostOccurences(string, POSSIBLE_QUOTES);
            if (cQuote != null) {
                quote = cQuote.charValue();
            }
        } catch (Exception e) {
            System.out.println("Quote have not been found in <" + POSSIBLE_QUOTES + ">");
            e.printStackTrace();
        }
        this.keepQuotes = false;
    }

    /**
     * Constructs a string tokenizer for the specified string.
     * @param string the string to be parsed
     * @param token the character that delimits each fields
     * @param quote the fields delimiters
     * @param keepQuotes Do you want to view quotes in the result ?
     */
    public CSVLineTokenizer(final String string, final char token, final char quote, final boolean keepQuotes) {
        this.string = string;
        this.token = token;
        this.quote = quote;
        this.keepQuotes = keepQuotes;
    }

    /**
     * Is there more elements ?
     * @return true if there is still some elements
     */
    public final boolean hasMoreElements() {
        return (string != null && string.length() > 0);
    }

    /**
     * @return Returns the quote.
     */
    public final char getQuote() {
        return quote;
    }

    /**
     * @return Returns the token.
     */
    public final char getToken() {
        return token;
    }

    /**
     * Get the next element.
     * @return The next element.
     */
    public final Object nextElement() {
        // Get nextToken
        int index = getNextToken(0);
        // Update current index
        if (index == -1) {
            index = string.length();
        }
        // Get element value
        String toReturn = string.substring(0, index);
        // Is it the end of String
        if (index == string.length()) {
            string = null;
        // There is still some fields
        } else {
            string = string.substring(index + 1, string.length());
        }
        // Keep quotes ? so we can trim spaces
        if (keepQuotes) {
            toReturn = toReturn.trim();
        // Here we trim the first and last quotes
        } else if (toReturn.length() > 0 && toReturn.charAt(0) == quote && toReturn.charAt(toReturn.length() - 1) == quote) {
            toReturn = toReturn.substring(1, toReturn.length() - 1);
        }
        // No comments ...
        return toReturn;
    }

    /**
     * This calculate the next token to get taking care of quotes.
     * @param index current index position
     * @return the next token
     */
    private int getNextToken(final int index) {
        // System.out.println("Token used: "+token);
        int nextToken = string.indexOf(token, index);

        // Token not found token is the entire of string
        if (nextToken == -1) {
            return string.length();
        }
        // Management of escapes caracters
        while (nextToken > 0 && string.charAt(nextToken - 1) == '\\') {
            nextToken = string.indexOf(token, nextToken + 1);
        }

        // Search next quote
        int nextQuote = string.indexOf(quote, index);

        // Management of escapes caracters
        while (nextQuote > 0 && string.charAt(nextQuote - 1) == '\\') {
            nextQuote = string.indexOf(quote, nextQuote + 1);
        }
        // Search of second quote
        int scndQuote = string.indexOf(quote, nextQuote + 1);

        // Management of escapes caracters
        while (scndQuote > 0 && string.charAt(scndQuote - 1) == '\\') {
            scndQuote = string.indexOf(quote, scndQuote + 1);
        }
        // Quote must be placed at first character
        if (nextQuote < nextToken && nextQuote > 0 && string.charAt(nextQuote - 1) != '\\') {
            nextQuote = -1;
        }
        // There's no scnd quote ... or scndQuote is before the first one
        if (nextToken > nextQuote && nextQuote > -1 && scndQuote > nextQuote) {
            // If we are here is because there is some quotes.
            // Now we have to verify that the scndQuotes is just before the token
            // int loop2=0;
            while (nextToken < scndQuote || removeSpaces(string.substring(scndQuote, nextToken + 1)).length() != 2) {
                // System.out.println("0 - string:"+string);
                // System.out.println("0 -nextQuote:"+nextQuote+" - scndQuote: "+scndQuote+" - nextToken: "+nextToken);
                int loop1 = 0;
                // If coma is long after end scndQuote, that means that this quote is not the valid one, so go on to take the next quote
                while (scndQuote > -1 && nextToken > scndQuote + 1 && removeSpaces(string.substring(scndQuote, nextToken + 1)).length() != 2) {
                    // System.out.println("1 - string:"+string);
                    // System.out.println("1 - nextQuote:"+nextQuote+" - scndQuote: "+scndQuote+" - nextToken: "+nextToken);
                    if (++loop1 > 25) {
                        System.err.println("WARNING (CSVLineTokenizer.getNextToken()): Loop goes up to 25 loop, this is not Good -> be ware");
                    }
                    scndQuote = string.indexOf(quote, scndQuote + 1);
                    // Management of escapes caracters
                    while (scndQuote > 0 && string.charAt(scndQuote - 1) == '\\') {
                        scndQuote = string.indexOf(quote, scndQuote + 1);
                    }
                }
                // All is done ?
                if (scndQuote == -1 || nextToken < nextQuote || nextToken > scndQuote && removeSpaces(string.substring(scndQuote, nextToken + 1)).length() == 2) {
                    return nextToken;
                }
                // No ! So continu ... and try to get the first token after
                nextToken = string.indexOf(token, scndQuote + 1);
                // Management of escapes caracters
                while (nextToken > 0 && string.charAt(nextToken - 1) == '\\') {
                    nextToken = string.indexOf(quote, nextToken + 1);
                }

                // System.out.println("2 - string:"+string);
                // System.out.println("2 - nextQuote:"+nextQuote+" - scndQuote: "+scndQuote+" - nextToken: "+nextToken);
                // if(nextToken > scndQuote) System.out.println("oops: "+removeSpaces(string.substring(scndQuote, nextToken+1)));
                if (nextToken == -1) {
                    return string.length();
                }
            }
        }
        // System.out.println("String: "+string.substring(index, nextToken));
        return nextToken;
    }

    /**
     * Remove spaces in a string.
     * @param string the string to be parsed
     * @return The string without spaces
     */
    private static String removeSpaces(final String string) {
        String toReturn = StringHelper.trim(string, " ");
        while (toReturn.indexOf(" ") > -1) {
            toReturn = toReturn.substring(0, toReturn.indexOf(" ")) + toReturn.substring(toReturn.indexOf(" ") + 1, toReturn.length());
        }
        return toReturn;
    }

    /**
     * To parse a csv file.
     * @param csvFile The pointer to the CSV file to open.
     * @return A Vector of Vector. Corresponding to rows and columns
     * @throws Exception ...
     */
    public static Vector parse(final File csvFile) throws Exception {
        Vector<Vector<String>> vLines = new Vector<Vector<String>>();
        int columnCount = 0;

        FileReader fr = new FileReader(csvFile);
        BufferedReader br = new BufferedReader(fr);
        String line = br.readLine();
        if (line != null) {
            CSVLineTokenizer csvParser;
            do {
                csvParser = new CSVLineTokenizer(line);
                Vector<String> vCols = new Vector<String>();
                while (csvParser.hasMoreElements()) {
                    String columnValue = csvParser.nextElement().toString();
                    // columnValue = StringHelper.unescapeLiteral(columnValue, csvParser.getQuote()+""+csvParser.getToken());
                    vCols.add(columnValue);
                }
                if (columnCount < vCols.size()) {
                    columnCount = vCols.size();
                }
                vLines.add(vCols);

                line = br.readLine();
            } while (line != null);
        }

        // Parse vector and verify that the columns number are OK
        for (int r = 0; r < vLines.size(); ++r) {
            Vector<String> vCols = vLines.get(r);
            for (int c = vCols.size(); c < columnCount; ++c) {
                vCols.add("");
            }
        }

        return vLines;
    }

    /**
     * Get the char that occure the most in the String 'string'.
     * @param string     The string to be parsed
     * @param characters The characters to be count
     * @return The character that have the most occurences in string
     * @throws Exception if there's any of the characters in the string
     */
    private static Character getTheMostOccurences(final String string, final String characters) throws Exception {
        //string += " ";
        int[] occurences = new int[characters.length()];
        int theMost = 0;
        int index = -1;
        for (int i = 0; i < characters.length(); ++i) {
            // for JDK 1.3.1 and lower
            int pos = 0;
            occurences[i] = 0;
            while (pos != -1) {
                pos = string.indexOf(characters.charAt(i), pos + 1);
                if (pos > -1) {
                    ++occurences[i];
                }
            }
            if (occurences[i] > theMost) {
                theMost = occurences[i];
                index = i;
            }
        }
        if (index == -1) {
            return null;
        }
        return new Character(characters.charAt(index));
    }

    /**
     * To parse a csv file.
     * @param data The lines
     * @param csvFile The pointer to the CSV file to open.
     * @param quote Quote is the characters used by the file to encapsulate fields (ex: "toto","titi","tata" or 'toto','titi','tata')
     * @param token the character that delimits each fields
     * @throws IOException ...
     */
    public static void store(final Vector<Vector<String>> data, final File csvFile, final char quote, final char token) throws IOException {
        // Initialization
        FileWriter w = null;
        BufferedWriter bw = null;
        // Get number of lines & number of columns
        int rowCount = data.size();
        int colCount = data.get(0).size();

        // Generating CSV content
        StringBuffer content = new StringBuffer();
        // For each row ...
        for (int r = 0; r < rowCount; ++r) {
            // Verify number of rows ... (This code is never used Who is the one that does this blady code ?)
            // if(data.size() < rowCount) for(int i=data.size(); i<rowCount; ++i) data.add(new Vector());
            // Get current column
            Vector<String> col = data.get(r);
            for (int c = 0; c < colCount; ++c) {
                // Verify number of columns ... Add column if missing ...
                if (col.size() < colCount) {
                    for (int i = col.size(); i < colCount; ++i) {
                        col.add("");
                    }
                }
                //String columnValue = StringHelper.escapeLiteral(col.get(c).toString(), quote+""+token);
                content.append(quote + col.get(c).toString() + quote + token);
            }
            content.append("\n");
        }

        // Storing CSV content to file
        if (!csvFile.exists()) {
            csvFile.createNewFile();
        }
        w = new FileWriter(csvFile);
        bw = new BufferedWriter(w);
        bw.write(content.toString());
        bw.close();
        w.close();
    }
}
