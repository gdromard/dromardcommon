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
 * @author          st22085
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
     * @param tabName the tabName to set
     * @throws JspException In the case of an invalid value is given or template is not found.
     */
    public final void setTabName(final String tabName) throws JspException {
        addParameter("tabName", tabName);
    }

    /**
     * @param function the function to set
     * @throws JspException In the case of an invalid value is given or template is not found.
     */
    public final void setFunction(final String function) throws JspException {
        addParameter("function", function);
    }

    /**
     * @param selected the selected to set
     * @throws JspException In the case of an invalid value is given or template is not found.
     */
    public final void setSelected(final String selected) throws JspException {
        try {
            if (transform(selected).equalsIgnoreCase("true")) {
                addParameter("class", "selected");
            } else {
                addParameter("class", "unselected");
            }
        } catch (IOException ex) {
            throw new JspTagException(ex.getMessage());
        }
    }

    /**
     * @param selected the selected to set
     * @throws JspException In the case of an invalid value is given or template is not found.
     */
    public final void setSelected(final boolean selected) throws JspException {
        if (selected) {
            addParameter("class", "selected");
        }
    }

    /**
     * @param tabindex Tab index of the ahref html element.
     * @throws JspException In the case of an invalid value is given or template is not found.
     */
    public final void setTabindex(final String tabindex) throws JspException {
        if (tabindex != null && tabindex.length() > 0) {
            addParameter("tabindex", "tabindex=\"" + tabindex + "\"");
        }
    }
}


