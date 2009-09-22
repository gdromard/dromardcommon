package net.dromard.common.junit;

import java.util.Set;

import junit.framework.Assert;
import junit.framework.TestCase;
import net.dromard.common.util.ReflectHelper;

public class ReflectHelperTest extends TestCase {

    public void testGetClasses() throws Exception {
        Set<Class<?>> list = ReflectHelper.getClasses("net.dromard.common.junit");
        Assert.assertTrue("Contains ReflectHelperTest class", list.contains(ReflectHelperTest.class));
        list.clear();
        list = ReflectHelper.getClasses("net.dromard.common.util");
        Assert.assertTrue("Contains ReflectHelper class", list.contains(net.dromard.common.util.ReflectHelper.class));
    }
}
