/*
 * Created on 6 juin 07
 * By Gabriel DROMARD
 */
package net.dromard.common.junit;

import junit.framework.Assert;
import junit.framework.TestCase;
import net.dromard.common.util.DateDifference;

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
        int[] tests = { 1, 9, 10, 99, 100, 999, 1000, 1000 * 23, 1000 * 60, 1000 * 60 * 23, 1000 * 60 * 60, 1000 * 60 * 60 * 23, 1000 * 60 * 60 * 24, 1000 * 60 * 60 * 24 * 2 };
        String[] result = { "1ms", "9ms", "10ms", "99ms", "100ms", "999ms", "1s", "23s", "1 min", "23 min", "1 hour", "23 hours", "1 day", "2 days" };
        for (int i = 0; i < tests.length; ++i) {
            Assert.assertEquals(result[i], new DateDifference(l * tests[i]).toString());
        }
    }
}
