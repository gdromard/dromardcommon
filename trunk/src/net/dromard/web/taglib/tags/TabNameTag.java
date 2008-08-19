/**
 * 	File : PageTemplateTag.java 8 juin 07
 */

package net.dromard.web.taglib.tags;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import net.dromard.common.templating.GString;
import net.dromard.common.util.StringHelper;
import net.dromard.web.taglib.AbstractTemplateTag;
import net.dromard.web.taglib.TemplateTagHelper;

/**
 * Tab container tag.
 * Exemple: <br>
 * <pre>
 *      <tag:tabcontainer>
 *          <tag:tabnames>
 *              <tag:tabname id="tab1" tabName="Tab 1" function="showTab(this)" selected="true" tabindex="1"/>
 *              <tag:tabname id="tab2" tabName="Tab 2" function="showTab(this)" selected="true" tabindex="2"/>
 *          </tagtabnames>
 *          <tag:tabcontents>
 *              <tag:tabcontent id="tab1" function="showTab(this)">
 *                  Tab 2 content
 *              </tag:tabcontent>
 *              <tag:tabcontent id="tab2" function="showTab(this)">
 *                  Tab 2 content
 *              </tag:tabcontent>
 *          </tag:tabcontents>
 *      </tag:tabcontainer>
 * </pre>
 * @see TabContainerTag#TabContainerTag()
 * <br>
 * @author Gabriel Dromard
 */
public class TabNameTag extends AbstractTemplateTag {
    /**
     * The serialVersionUID is a universal version identifier for a Serializable class. Deserialization uses this number to ensure that a loaded class
     * corresponds exactly to a serialized object. If no match is found, then an InvalidClassException is thrown.
     * @see Serializable
     */
    private static final long serialVersionUID = -2313967213280146588L;
    /** The GString template instance. */
    private GString template = null;

    /**
     * Default constructor.
     */
    public TabNameTag() {
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
        template = TemplateTagHelper.buildGString(pageContext, "tabname");
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

    /**
     * @param tabindex Tab index of the ahref html element.
     * @throws JspException In the case of an invalid value is given or template is not found.
     */
    public final void setTabindex(final String tabindex) throws JspException {
        if (!StringHelper.isEmpty(tabindex)) {
            addParameter("tabindex", "tabindex=\"" + tabindex + "\"");
        }
    }
}


