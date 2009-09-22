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
 * Exemple: <br>
 * <pre>
 *      <tag:tabcontainer>
 *          <tag:tabcontent id="tab1" tabName="Tab 1" function="showTab(this)">
 *              Tab 1 content
 *          </tag:tabcontent>
 *          <span class="separator">&nbsp;</span>
 *          <tag:tabcontent id="tab2" tabName="Tab 2" function="showTab(this)">
 *              Tab 2 content
 *          </tag:tabcontent>
 *      </tag:tabcontainer>
 * </pre>
 * @see TabContentTag#TabContentTag()
 * <br>
 * @author Gabriel Dromard
 */
public class TabContainerTag extends AbstractTemplateTag {
    /** Serial UID. */
    private static final long serialVersionUID = 290230341995116306L;
    /** The GString template instance. */
    private GString template = null;

    /**
     * Default constructor.
     */
    public TabContainerTag() {
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
        template = TemplateTagHelper.buildGString(pageContext, "tabcontainer");
        return template;
    }
}


