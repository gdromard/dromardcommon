/**
 * 	File : Template.java 8 juin 07
 */

package net.dromard.web.taglib;

import java.io.File;
import java.util.Date;

import net.dromard.common.templating.GString;


/**
 * Template definition class.
 * <br>
 * @author Gabriel Dromard
 */
public class Template {
    /** The template name. */
    private String name;
    /** The template in gstring format. */
    private GString template;
    /** Template file property file. */
    private File templateFile;
    /** Last modification of file. */
    private long lastModificationFileDate;

    /**
     * Default constructor.
     * @param name       The template name
     * @param template   The template
     */
    public Template(final String name, final GString template) {
        this.name = name;
        this.template = template;
    }

    /**
     * Ask if template needs to be reloaded.
     * @return True if the template poperty file has been updated externally.
     */
    public final boolean needToBeReloaded() {
        return (templateFile != null && templateFile.lastModified() > lastModificationFileDate);
    }

    /**
     * Default constructor.
     * @param name         The template name
     * @param template     The template
     * @param templateFile The template property file
     */
    public Template(final String name, final GString template, final File templateFile) {
        this.name = name;
        this.template = template;
        this.templateFile = templateFile;
        this.lastModificationFileDate = new Date().getTime();
    }

    /**
     * @return the name
     */
    public final String getName() {
        return name;
    }

    /**
     * @return the template
     */
    public final GString getTemplate() {
        return (GString) template.clone();
    }
}
