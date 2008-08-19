/**
 * 	File : PageTemplateTag.java 8 juin 07
 */

package net.dromard.web.taglib.tags;

import java.io.IOException;

import javax.servlet.jsp.JspException;

import net.dromard.common.templating.GString;
import net.dromard.web.taglib.AbstractTemplateTag;
import net.dromard.web.taglib.TemplateTagHelper;

/**
 * Head tag.
 * Exemple:<br>
 * <pre>
 *    <tag:head title="DtX .:. Tag Lib test"/>
 *    <tag:menu>
 *        <%@include file="/WEB-INF/jspf/menu.jsp" %>
 *    </tag:menu>
 *    <tag:body lang="EN">
 *        <h1>Template Exemple</h1>
 *    </tag:body>
 * </pre>
 * <br>
 * @author Gabriel Dromard
 */
public class HeadTag extends AbstractTemplateTag {
    /** SerialUID. */
    private static final long serialVersionUID = 3195388677743295323L;
    /** The GString template instance. */
    private GString template = null;

    /**
     * Default constructor.
     */
    public HeadTag() {
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
        template = TemplateTagHelper.buildGString(pageContext, "head");
        return template;
    }

    /**
     * @param title the title to set
     * @throws JspException In the case of an invalid value is given or template is not found.
     */
    public final void setTitle(final String title) throws JspException {
        addParameter("title", title);
    }
}


