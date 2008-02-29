/**
 * 	File : TabContentTagTest.java 2 juil. 07
 */

package net.dromard.web.taglib.test;

import net.dromard.web.taglib.tags.TabContentTag;

public class TabContentTagTest extends AbstractTemplateTagTest {
    public void setUp() {
        super.setUp(new TabContentTag());
    }

    public void testTagTemplate() throws Exception {
        String expectedResult = "<a id=\"tab2\" href=\"#\" onClick=\"showTab(this); return false;\" class=\"unselected\"><span>Tab Two</span></a><div id=\"tab2\"></div>";
        ((TabContentTag) tag).setTabId("tab2");
        ((TabContentTag) tag).setTabName("Tab Two");
        tag.doStartTag();
        tag.doEndTag();
        assertEquals(expectedResult, getLastOutput());
    }
}


