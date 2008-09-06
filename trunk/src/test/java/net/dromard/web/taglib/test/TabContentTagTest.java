/**
 * 	File : TabContentTagTest.java 2 juil. 07
 */

package net.dromard.web.taglib.test;

import net.dromard.web.taglib.tags.TabContentTag;

public class TabContentTagTest extends AbstractTemplateTagTest {
    public void setUp() {
        super.setUp(new TabContentTag());
    }

    public void testTagTemplate() {
        String expectedResult = "<div id=\"tab2\" class=\"unselected\"></div>";
        try {
        ((TabContentTag) tag).setTabId("tab2");
        tag.doStartTag();
        tag.doEndTag();
        assertEquals(expectedResult, getLastOutput());
        } catch (Exception ex) {
            ex.printStackTrace();
            fail(ex.getMessage());
        }
    }
}


