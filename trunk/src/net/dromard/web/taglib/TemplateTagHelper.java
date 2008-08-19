/**
 * 	File : TemplateTagFactory.java 8 juin 07
 */
package net.dromard.web.taglib;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import javax.servlet.jsp.PageContext;

import net.dromard.common.templating.GString;

/**
 * Helper class for templates.
 * <br>
 * @author Gabriel Dromard
 */
public final class TemplateTagHelper {
    /** The folder where templates are stored. */
    private static final String TEMPLATE_ROOT_FOLDER = "/WEB-INF/taglib/templates/";
    /** The extention used for template properties. */
    private static final String TEMPLATE_PARAMETER_FILE_EXTENTION = ".properties";
    /**
     * The property key used to retreive template content.
     * Note that we can put in this field alink to a relative file.
     */
    private static final String TEMPLATE_PROPERTY_KEY = "TEMPLATE";

    /**
     * Private empty constructor. So as be a valid util class.
     */
    private TemplateTagHelper() {
        /* Private empty constructor. So as be a valid util class. */
    }

    /**
     * Build the GString instance for a given template.
     * @param pageContext  The Page Context instance.
     * @param templateName The template name
     * @return The GString instance representing the template.
     * @throws IOException If the template property file is not found or if an error occured during file read.
     */
    public static GString buildGString(final PageContext pageContext, final String templateName) throws IOException {
        Template template = TemplateCacheManager.getInstance().get(templateName);
        if (template == null) {
            // Define template.
            GString gstring = null;
            // Retreive template property file
            File templatePropertyFile = new File(pageContext.getServletContext().getRealPath(TEMPLATE_ROOT_FOLDER + templateName + TEMPLATE_PARAMETER_FILE_EXTENTION));
            // Load template properties using a dynamic property
            Properties properties = new Properties();
            properties.load(new FileInputStream(templatePropertyFile));
            // Retreive the template value. (Note: it can be a file name)
            String templateValue = properties.getProperty(TEMPLATE_PROPERTY_KEY);
            // Check if the template content is estenalized into a file:
            File templateFile = new File(pageContext.getServletContext().getRealPath(TEMPLATE_ROOT_FOLDER + templateValue));
            if (templateFile.exists()) {
                // ... in this case we load the file content as template. Expecting an UTF-8 file.
                gstring = GString.load(new FileInputStream(templateFile), "UTF-8");
                gstring.setParameters(properties);
            } else {
                // ... in this case we keep the property value as template content.
                gstring = new GString(templateValue, properties);
            }
            template = new Template(templateName, gstring, templatePropertyFile);
            TemplateCacheManager.getInstance().add(template);
        }
        return template.getTemplate();
    }
}
