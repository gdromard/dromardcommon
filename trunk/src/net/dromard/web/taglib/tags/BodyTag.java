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
 * Body tag lib.
 * You migth use Menu tag lib just before this one so as to define the menu.
 * Exemple:<br>
 * <pre>
 *    <tag:head title="Tag Lib test"/>
 *    <c:set var="menu">
 *        <%@include file="/WEB-INF/jspf/menu.jsp" %>
 *    </c:set>
 *    <tag:body lang="EN" cssClass="main" menu="${menu}">
 *        <h1>Template Exemple</h1>
 *    </tag:body>
 * </pre>
 * <br>
 * @author Gabriel Dromard
 */
public class BodyTag extends AbstractTemplateTag {
    /** SerialUID. */
    private static final long serialVersionUID = -2111578288191666012L;
    /** The GString template instance. */
    private GString template = null;

    /**
     * Default constructor.
     */
    public BodyTag() {
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
        template = TemplateTagHelper.buildGString(pageContext, "body");
        return template;
    }

    /**
     * @param footer the footer to set
     * @throws JspException In the case of an invalid value is given or template is not found.
     */
    public final void setFooter(final String footer) throws JspException {
        addParameter("footer", footer);
    }

    /**
     * @param cssClass the CSS class to set
     * @throws JspException In the case of an invalid value is given or template is not found.
     */
    public final void setCssClass(final String cssClass) throws JspException {
        addParameter("cssClass", cssClass);
    }

    /**
     * @param logoHref the logoHref to set
     * @throws JspException In the case of an invalid value is given or template is not found.
     */
    public final void setLogoHref(final String logoHref) throws JspException {
        addParameter("logoHref", logoHref);
    }

    /**
     * @param headerContent the headerContent to set
     * @throws JspException In the case of an invalid value is given or template is not found.
     */
    public final void setHeaderContent(final String headerContent) throws JspException {
        addParameter("headerContent", headerContent);
    }

    /**
     * @param lang the lang to set
     * @throws JspException In the case of an invalid value is given or template is not found.
     */
    public final void setLang(final String lang) throws JspException {
        addParameter("lang", lang);
    }

    /**
     * @param menu the menu to set
     * @throws JspException In the case of an invalid value is given or template is not found.
     */
    public final void setMenu(final String menu) throws JspException {
        addParameter("menu", menu);
	}
}


