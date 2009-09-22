package net.dromard.web.taglib;

import java.io.IOException;

import net.dromard.common.templating.GString;

/**
 * Abstract Tag class.
 * <br>
 * @author Gabriel Dromard
 */
public interface ITemplateTag  {
    /**
     * This method has to be implemented.
     * Aim is to build the GString template instance with its default values set.
     * If the template is already built return the existing instance.
     * @return The GString instance of the template.
     * @throws IOException If the template property file is not found or if an error occured during file read.
     */
    GString getTemplate() throws IOException;
}
