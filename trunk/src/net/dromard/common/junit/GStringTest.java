package net.dromard.common.junit;

import java.util.Hashtable;
import java.util.Map;

import net.dromard.common.templating.GString;

import junit.framework.TestCase;

/**
 * GString class Unit Test Case.
 * @author Gabriel DROMARD
 */
public class GStringTest extends TestCase {
    /**
     * See tests attributes for more details.
     * @testId             PLT_MIS_0001
     * @testName           GString tests
     * @testDescription    Test the GString class
     * @testType           MIS
     */
    public void testConstructor() {
        // Empty constructor
        assertEquals("${TEST}", (new GString("${TEST}")).toString());
        // Other ...
        Map map = new Hashtable();
        map.put("TEST", "test");
        assertEquals("test", (new GString("${TEST}", map)).toString());
    }

    /**
     * See {@link #testConstructor()} for more details.
     * @testId PLT_MIS_0001
     */
    public void testSetParameters() {
        GString gstring = new GString("${TEST} ${TOTO}");
        Map map = new Hashtable();
        map.put("TEST", "test");
        map.put("TOTO", "toto");
        gstring.setParameters(map);
        assertEquals("test toto", gstring.toString());
    }

    /**
     * See {@link #testConstructor()} for more details.
     * @testId PLT_MIS_0001
     */
    public void testAddParameter() {
        GString gstring = new GString("${TEST} ${TOTO}", new Hashtable());
        gstring.addParameter("TEST", "test");
        gstring.addParameter("TOTO", "toto");
        assertEquals("test toto", gstring.toString());
    }
}
