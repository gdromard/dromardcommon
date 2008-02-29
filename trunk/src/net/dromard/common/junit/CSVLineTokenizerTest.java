/*
 * Created on 6 mars 2006
 * By Gabriel DROMARD
 */
package net.dromard.common.junit;

import junit.framework.TestCase;
import net.dromard.common.util.CSVLineTokenizer;

public class CSVLineTokenizerTest extends TestCase {

    public static void main(final String[] args) {
        junit.swingui.TestRunner.run(CSVLineTokenizerTest.class);
    }

    /**
     * @testId             CSVLineTokenizer01
     * @testName           CSVLineTokenizer Test Quotes
     * @testDesc           Test management of the inner double quotes
     * @testType           Format
     * @testExpectedResult NA
     */
    public void test1() {
        String test1 = "\"test \"test, test\" , \"test\" , 20/09/2002 , t\"e\"s\"t , \"test, test : test\"";
        CSVLineTokenizer test = new CSVLineTokenizer(test1);

        int count = 1;
        if(test.hasMoreElements()) assertEquals(++count+"", "\"test \"test, test\" ", test.nextElement());
        if(test.hasMoreElements()) assertEquals(++count+"", " \"test\" ", test.nextElement());
        if(test.hasMoreElements()) assertEquals(++count+"", " 20/09/2002 ", test.nextElement());
        if(test.hasMoreElements()) assertEquals(++count+"", " t\"e\"s\"t ", test.nextElement());
        if(test.hasMoreElements()) assertEquals(++count+"", " \"test", test.nextElement());
        if(test.hasMoreElements()) assertEquals(++count+"", " test : test\"", test.nextElement());
    }

    /**
     * @testId             CSVLineTokenizer01
     */
    public void test2() {
        String test2 = "'test 'test, test' , 'test' , 20/09/2002 , t'e's't , 'test, test : test'";
        CSVLineTokenizer test = new CSVLineTokenizer(test2);

        int count = 1;
        if(test.hasMoreElements()) assertEquals(++count+"", "'test 'test, test' ", test.nextElement());
        if(test.hasMoreElements()) assertEquals(++count+"", " 'test' ", test.nextElement());
        if(test.hasMoreElements()) assertEquals(++count+"", " 20/09/2002 ", test.nextElement());
        if(test.hasMoreElements()) assertEquals(++count+"", " t'e's't ", test.nextElement());
        if(test.hasMoreElements()) assertEquals(++count+"", " 'test", test.nextElement());
        if(test.hasMoreElements()) assertEquals(++count+"", " test : test'", test.nextElement());
    }

    /**
     * @testId             CSVLineTokenizer01
     */
    public void test3() {
        String test3 = "'test 'test, test' ; 'test' ; 20/09/2002 ; t'e's't ; 'test; test : test'";
        CSVLineTokenizer test = new CSVLineTokenizer(test3);

        int count = 1;
        if(test.hasMoreElements()) assertEquals(++count+"", "'test 'test, test' ", test.nextElement());
        if(test.hasMoreElements()) assertEquals(++count+"", " 'test' ", test.nextElement());
        if(test.hasMoreElements()) assertEquals(++count+"", " 20/09/2002 ", test.nextElement());
        if(test.hasMoreElements()) assertEquals(++count+"", " t'e's't ", test.nextElement());
        if(test.hasMoreElements()) assertEquals(++count+"", " 'test", test.nextElement());
        if(test.hasMoreElements()) assertEquals(++count+"", " test : test'", test.nextElement());
    }

    /**
     * @testId             CSVLineTokenizer01
     */
    public void test4() {
        String test4 = "'test 'test, test' | 'test' | 20/09/2002 | t'e's't | 'test| test : test'";
        CSVLineTokenizer test = new CSVLineTokenizer(test4);

        int count = 1;
        if(test.hasMoreElements()) assertEquals(++count+"", "'test 'test, test' ", test.nextElement());
        if(test.hasMoreElements()) assertEquals(++count+"", " 'test' ", test.nextElement());
        if(test.hasMoreElements()) assertEquals(++count+"", " 20/09/2002 ", test.nextElement());
        if(test.hasMoreElements()) assertEquals(++count+"", " t'e's't ", test.nextElement());
        if(test.hasMoreElements()) assertEquals(++count+"", " 'test", test.nextElement());
        if(test.hasMoreElements()) assertEquals(++count+"", " test : test'", test.nextElement());
    }
    /*
    public void test2() {
        try {
            File fTest1 = new File("test_with_quote.csv");
            File fTest2 = new File("test_with_doublequote.csv");
            System.out.println(fTest1.getAbsolutePath());
            System.out.println(fTest2.getAbsolutePath());

            Vector data = CSVLineTokenizer.parse(new File("test.csv"));
            CSVLineTokenizer.store(data, fTest1, '\'', ';');
            CSVLineTokenizer.store(data, fTest2, '"', ';');
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**/
}
