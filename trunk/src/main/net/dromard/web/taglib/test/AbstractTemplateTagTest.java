/**
 * 	File : TreeGridTagTest.java 12 juin 07
 */
package net.dromard.web.taglib.test;

import java.util.Locale;

import junit.framework.TestCase;
import net.dromard.common.util.ReflectHelper;
import net.dromard.web.mock.test.MockHttpServletRequest;
import net.dromard.web.mock.test.MockJspWriter;
import net.dromard.web.mock.test.MockPageContext;
import net.dromard.web.taglib.AbstractTemplateTag;

/**
 * This class is a TestCase abstract implementation design to be extended in your junit tag lib tests.
 * <br>
 * @author Gabriel Dromard
 */
public abstract class AbstractTemplateTagTest extends TestCase {
    /** The tag instance to be tested. */
    protected AbstractTemplateTag tag;
    /** Turn on when setUp is called so as to prevent direct use. */
    private boolean extended = false;

    /**
     * Initialize the tag instance.
     * @param tagInstance The tag instance on which to run default tests.
     */
    public final void setUp(final AbstractTemplateTag tagInstance) {
        setUp(tagInstance, false);
    }

    /**
     * Set up the test case by initializing the tag instance.
     * @param tagInstance    The tag instance on which to run default tests.
     * @param printToConsole True if you wish to enable output redirection to console
     */
    public final void setUp(final AbstractTemplateTag tagInstance, final boolean printToConsole) {
        this.tag = tagInstance;
        MockPageContext pageContext = new MockPageContext(printToConsole);
        tag.setPageContext(pageContext);
        extended = true;
    }

    /**
     * Call this method in your test to test that printed template by doStartTag() method is not null.
     * @return the printed string.
     */
    public String getDoStartTagResult() throws Exception {
        if (extended) {
            tag.doStartTag();
            return getLastOutput();
        }
        return null;
    }

    /**
     * Call this method in your test to test that printed template by doAfterBody() method is not null.
     * @return the printed string.
     */
    public String getDoAfterBodyResult() throws Exception {
        if (extended) {
            tag.doAfterBody();
            return getLastOutput();
        }
        return null;
    }

    /**
     * Call this method in your test to test that printed template by doStartTag() method is not null.
     * @return the printed string.
     */
    public String getDoEndTagResult() throws Exception {
        if (extended) {
            tag.doEndTag();
            return getLastOutput();
        }
        return null;
    }

    /**
     * Retreive the last ouput of JspWriter.
     * @return the last ouput of JspWriter.
     * @throws Exception As reflection is used ... exception relatied to reflection can be thrown
     */
    public String getLastOutput() throws Exception {
        MockPageContext pageContext = (MockPageContext) ReflectHelper.getFieldValue(tag, "pageContext");
        MockJspWriter writer = (MockJspWriter) pageContext.getOut();
        return writer.getLastOutput();
    }

    /**
     * Set the request locale.
     * @param locale the request locale.
     * @throws Exception As reflection is used ... exception relatied to reflection can be thrown
     */
    public void setRequestLocale(final Locale locale) throws Exception {
        MockPageContext pageContext = (MockPageContext) ReflectHelper.getFieldValue(tag, "pageContext");
        MockHttpServletRequest request = (MockHttpServletRequest) pageContext.getRequest();
        request.setLocale(locale);
    }
}


