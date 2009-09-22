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
        String expectedResult = "<a id=\"tab2\" href=\"#\" onClick=\"showTab(this); return false;\" class=\"unselected\"><span>Tab 2</span></a><div id=\"tab2\"></div>";
        try {
            ((TabContentTag) tag).setTabId("tab2");
            ((TabContentTag) tag).setTabName("Tab 2");
	        tag.doStartTag();
	        tag.doEndTag();
	        assertEquals(expectedResult, getLastOutput());
        } catch (Exception ex) {
            ex.printStackTrace();
            fail(ex.getMessage());
        }
    }
}


