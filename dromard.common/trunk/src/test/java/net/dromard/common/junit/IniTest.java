package net.dromard.common.junit;

import java.io.File;
import java.io.IOException;

import junit.framework.TestCase;
import net.dromard.common.io.Ini;

/**
 * @author Gabriel Dromard
 */
public class IniTest extends TestCase {
    public void testLoad() throws IOException {
        try {
            Ini ini = new Ini("Cp1252");
            ini.load(new File(IniTest.class.getResource("initest.ini").toURI().getPath()));
            assertEquals("empty section key value.", ini.getValue("", "empty section key"));
            assertEquals("value", ini.getValue("TEST Section", "key"));
            assertEquals("in !!", ini.getValue("TEST Section", "test=of a key with"));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }
}
