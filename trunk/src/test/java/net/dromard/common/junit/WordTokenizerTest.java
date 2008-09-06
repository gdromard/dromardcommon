package net.dromard.common.junit;

import net.dromard.common.util.WordTokenizer;
import junit.framework.TestCase;


/**
 * WordTokenizerTest class Unit Test Case.
 * @author Gabriel Dromard (gabriel.dromard@airbus.com)
 */
public class WordTokenizerTest extends TestCase {
    /**
     * See tests attributes for more details.
     * @testId             PLT_MIS_4003
     * @testName           WordTokenizer tests
     * @testDescription    Test the WordTokenizer class
     * @testType           MIS
     */
    public final void testHasMoreElements() {
        assertTrue(new WordTokenizer("1|1|", "|").hasMoreElements());
        assertTrue(new WordTokenizer("1", "|").hasMoreElements());
    }

    /**
     * See {@link #testHasMoreElements()} for more details.
     * @testId             PLT_MIS_4003
     */
    public final void testNextElement() {
        assertNotNull(new WordTokenizer("1|1|", "|").nextElement());
        assertNotNull(new WordTokenizer("1", "|").nextElement());
    }

    /**
     * See {@link #testHasMoreElements()} for more details.
     * @testId             PLT_MIS_4003
     */
    public void testSplit() {
        assertEquals(1, WordTokenizer.split("1", "|").size());
        assertEquals(1, WordTokenizer.countOccurence("1|", "|"));
        assertEquals(1, WordTokenizer.split("1|", "|").size());
        assertEquals(2, WordTokenizer.split("1|2", "|").size());
        assertEquals(3, WordTokenizer.split("1|2|3", "|").size());
        assertEquals(3, WordTokenizer.split("1||3", "|").size());
    }
}
