/**
 * 	File : TabContentTagTest.java 2 juil. 07
 */

package net.dromard.web.taglib.test;

import net.dromard.web.taglib.tags.TabNameTag;

public class TabNameTagTest extends AbstractTemplateTagTest {
    public void setUp() {
        super.setUp(new TabNameTag());
    }

    public void testTagTemplate() {
        String expectedResult = "<a id=\"tab2\" href=\"javascript:empty()\" onClick=\"showTab(this); return false;\" class=\"unselected\" ><span>Tab Two </span></a>";
        try {
            ((TabNameTag) tag).setTabId("tab2");
            ((TabNameTag) tag).setTabName("Tab Two");
            tag.doStartTag();
            tag.doEndTag();
            assertEquals(expectedResult, getLastOutput());
        } catch (Exception ex) {
            ex.printStackTrace();
            fail(ex.getMessage());
        }
    }
}


