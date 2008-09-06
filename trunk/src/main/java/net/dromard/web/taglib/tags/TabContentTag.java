/**
 * 	File : PageTemplateTag.java 8 juin 07
 */

package net.dromard.web.taglib.tags;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import net.dromard.common.templating.GString;
import net.dromard.web.taglib.AbstractTemplateTag;
import net.dromard.web.taglib.TemplateTagHelper;

/**
 * Tab container tag.
 * Exemple: <br>
 * <pre>
 *      <tag:tabcontainer>
 *          <tag:tabcontent id="tab1" tabName="Tab 1" function="showTab(this)" selected="true">
 *              Tab 1 content
 *          </tag:tabcontent>
 *          <span class="separator">&nbsp;</span>
 *          <tag:tabcontent id="tab2" tabName="Tab 2" function="showTab(this)">
 *              Tab 2 content
 *          </tag:tabcontent>
 *      </tag:tabcontainer>
 * </pre>
 * @see TabContainerTag#TabContainerTag()
 * <br>
 * @author Gabriel Dromard
 */
public class TabContentTag extends AbstractTemplateTag {
    /** Serial UID. */
    private static final long serialVersionUID = -2313967213280146588L;
    /** The GString template instance. */
    private GString template = null;

    /**
     * Default constructor.
     */
    public TabContentTag() {
        super();
    }

    /**
     * Retreive the GString template instance.
     * @return the GString instance of the template.
     * @throws IOException If the template property file is not found or if an error occured during file read.
     */
    public final GString getTemplate() throws IOException {
        if (template != null) {
            return template;
        }
        template = TemplateTagHelper.buildGString(pageContext, "tabcontent");
        return template;
    }

    /**
     * @param tabId the id to set
     * @throws JspException In the case of an invalid value is given or template is not found.
     */
    public final void setTabId(final String tabId) throws JspException {
        addParameter("tabId", tabId);
    }

    /**
     * @param selected the selected to set
     * @throws JspException In the case of an invalid value is given or template is not found.
     */
    public final void setSelected(final String selected) throws JspException {
        try {
            setSelected(transform(selected).equalsIgnoreCase("true"));
        } catch (IOException ex) {
            throw new JspTagException(ex.getMessage());
        }
    }

    /**
     * @param selected the selected to set
     * @throws JspException In the case of an invalid value is given or template is not found.
     */
    private void setSelected(final boolean selected) throws JspException {
        if (selected) {
            addParameter("class", "selected");
        } else {
            addParameter("class", "unselected");
        }
    }
}


