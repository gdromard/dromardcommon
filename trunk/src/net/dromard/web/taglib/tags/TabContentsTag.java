/**
 * 	File : PageTemplateTag.java 8 juin 07
 */

package net.dromard.web.taglib.tags;

import java.io.IOException;

import net.dromard.common.templating.GString;
import net.dromard.web.taglib.AbstractTemplateTag;
import net.dromard.web.taglib.TemplateTagHelper;

/**
 * Tab container tag.
 * Example: <br>
 * <pre>
 *      <tag:tabcontainer>
 *          <tag:tabnames>
 *              <tag:tabname id="tab1" tabName="Tab 1" function="showTab(this)" selected="true" tabindex="1"/>
 *              <tag:tabname id="tab2" tabName="Tab 2" function="showTab(this)" selected="true" tabindex="2"/>
 *          </tag:tabnames>
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
 * @see TabContentTag#TabContentTag()
 * <br>
 * @author Gabriel Dromard
 */
public class TabContentsTag extends AbstractTemplateTag {
    /**
     * The serialVersionUID is a universal version identifier for a Serializable class. Deserialization uses this number to ensure that a loaded class
     * corresponds exactly to a serialized object. If no match is found, then an InvalidClassException is thrown.
     * @see Serializable
     */
    private static final long serialVersionUID = 290230341995116306L;
    /** The GString template instance. */
    private GString template = null;

    /**
     * Default constructor.
     */
    public TabContentsTag() {
        super();
    }

    /**
     * Retrieve the GString template instance.
     * @return the GString instance of the template.
     * @throws IOException If the template property file is not found or if an error occured during file read.
     */
    public final GString getTemplate() throws IOException {
        if (template != null) {
            return template;
        }
        template = TemplateTagHelper.buildGString(pageContext, "tabcontents");
        return template;
    }
}


