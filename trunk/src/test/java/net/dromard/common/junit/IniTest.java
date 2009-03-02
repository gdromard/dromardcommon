package net.dromard.common.junit;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;

import net.dromard.common.io.Ini;

import junit.framework.TestCase;

/**
 * @author Gabriel Dromard
 */
public class IniTest extends TestCase {
	public void testLoad() throws IOException {
		try {
			Ini ini = new Ini();
			ini.load(new File(URLDecoder.decode((IniTest.class.getResource("initest.ini").getFile()))));
			assertEquals("empty section key value.", ini.getValue("", "empty section key"));
			assertEquals("value", ini.getValue("TEST Section", "key"));
			assertEquals("in !!", ini.getValue("TEST Section", "test=of a key with"));
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
}
