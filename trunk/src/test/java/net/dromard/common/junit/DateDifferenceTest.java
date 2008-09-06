/*
 * Created on 6 juin 07
 * By Gabriel DROMARD
 */
package net.dromard.common.junit;

import net.dromard.common.util.DateDifference;
import junit.framework.TestCase;

public class DateDifferenceTest extends TestCase {

    public static void main(final String[] args) {
        junit.swingui.TestRunner.run(DateDifferenceTest.class);
    }

    /**
     * @testId             DateDifference01
     * @testName           DateDifference Test
     * @testDesc           This test cover all possible used of the DateDifference class
     * @testType           Format
     * @testExpectedResult NA
     */
    public void testDateDifference() {
        long l = 1L; 
        int[] tests = { 1, 9, 10, 99, 100, 999, 1000, 1000*23, 1000*60, 1000*60*23, 1000*60*60, 1000*60*60*23, 1000*60*60*24, 1000*60*60*24*2  };
        String[] result = { "00:00:00.001", "00:00:00.009", "00:00:00.010", "00:00:00.099", "00:00:00.100", "00:00:00.999", "00:00:01.000", "00:00:23.000", "00:01:00.000", "00:23:00.000", "01:00:00.000", "23:00:00.000", "1 Day 00:00:00.000", "2 Days 00:00:00.000" };
        for (int i=0; i<tests.length; i++) {
            assertEquals(result[i], (new DateDifference(l*tests[i])).getDifference());
        }
    }
}
