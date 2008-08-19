/**
 * 	File : TemplateTagFactory.java 8 juin 07
 */
package net.dromard.web.taglib;

import java.util.Map;

import net.dromard.common.util.SoftHashtable;

/**
 * Helper class for templates.
 * <br>
 * @author Gabriel Dromard
 */
public final class TemplateCacheManager {
    /** Cache that store templates. */
    private static final TemplateCacheManager INSTANCE = new TemplateCacheManager();

    /** The cache map. */
    private Map cache;

    /**
     * Retreive the singleton instance of manager.
     * @return The singleton instance of manager.
     */
    public static TemplateCacheManager getInstance() {
        return INSTANCE;
    }

    /**
     * Clone is not supported.
     * @return a CloneNotSupportedException.
     * @see java.lang.Object#clone()
     * @throws CloneNotSupportedException Always thrown (is is a singleton !)
     */
    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    /**
     * Private empty constructor. So as be a valid singleton.
     */
    private TemplateCacheManager() {
        cache = new SoftHashtable();
    }

    /**
     * Retreive template from cache.
     * @param templateName The template name.
     * @return The template instance or null if not in cache.
     */
    public Template get(final String templateName) {
        Template template = (Template) cache.get(templateName);
        if (template != null && !template.needToBeReloaded()) {
            return template;
        }
        return null;
    }

    /**
     * Add the template to cache.
     * @param template The template.
     */
    public void add(final Template template) {
        cache.put(template.getName(), template);
    }
}
