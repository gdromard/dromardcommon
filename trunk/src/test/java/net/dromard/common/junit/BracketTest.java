/*
 * Created on 6 juin 07
 * By Gabriel DROMARD
 */
package net.dromard.common.junit;

import junit.framework.TestCase;
import net.dromard.common.util.brackets.Bracket;

/**
 *
 * @author Gabriel Dromard
 */
public class BracketTest extends TestCase {
    /**
     * See tests attributes for more details.
     * @testId             Bracket001
     * @testName           Bracket test
     * @testDescription    Test the Bracket class
     * @testType           Implementation
     * @testExpectedResult All OK
     */
    public void testBracket() {
        String test = "(( 1 + ( 3 - 4 )) - (3 + 8)) * 12";
        Bracket bracket = new Bracket(test, Bracket.ROUND_BRACKET);
        assertEquals("("+test+")", bracket.toString());
    }
}
