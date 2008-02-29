/*
 * Created on 6 juin 07
 * By Gabriel DROMARD
 */
package net.dromard.common.junit;

import java.util.ArrayList;

import junit.framework.TestCase;
import net.dromard.common.util.StringHelper;


/**
 * StringHelperTest class Unit Test Case.
 * @author Gabriel DROMARD
 */
public class StringHelperTest extends TestCase {

    /**
     * See tests attributes for more details.
     * @testId             PLT_MIS_4004
     * @testName           StringHelper tests
     * @testDescription    Test the StringHelper class
     * @testType           MIS
     */
    public void testCountOccurences() {
        assertEquals(2, StringHelper.countOccurences("11 11", "11"));
        assertEquals(3, StringHelper.countOccurences("111111", "11"));
        assertEquals(4, StringHelper.countOccurences("1111", "1"));
        assertEquals(5, StringHelper.countOccurences("1 1 1 1 1", "1"));
        assertEquals(6, StringHelper.countOccurences("|1|1|1|1|1|1", "1"));
        assertEquals(4, StringHelper.countOccurences("Mon papa s'appele Paul", "p"));
        assertEquals(2, StringHelper.countOccurences("Mon papa s'appele Paul", "pa"));
    }

    /**
     * See {@link #testCountOccurences()} for test details.
     * @testId             PLT_MIS_4004
     */
    public void testIteratorToString() {
        ArrayList list = new ArrayList();
        list.add("0");
        list.add("1");
        list.add("2");
        list.add("3");
        list.add("4");
        list.add("5");
        assertEquals("0.1.2.3.4.5", StringHelper.iteratorToString(list.iterator(), "."));
    }

    /**
     * See {@link #testCountOccurences()} for test details.
     * @testId             PLT_MIS_4004
     */
    public void testReplaceAll() {
        String original = "${original}", pattern = original, value = "original";
        assertEquals(value, StringHelper.replaceAll(original, pattern, value));
    }
}
