/**
 * 	File : DefaultFormatter.java Apr 11, 2008
 */
package net.dromard.common.templating.reflect;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;

import net.dromard.common.templating.GString;

/**
 * Implementation of a formatter based on a template.
 * The template store the format of classes using there class name as key.
 * <br>
 * @author Gabriel Dromard
 */
public class TemplatedFormatter implements Formatter {
    /** The GString template to be used so as to know how to format a given class. */
    private GString template;

    /**
     * Construct a new Formatter based on a template.
     * @param template The GString template
     */
    public TemplatedFormatter(final GString template) {
        this.template = template;
    }

    /**
     * Format the given object using locale.
     * @param object The object to be formatted
     * @return The formated string
     * @throws Exception Any exception can occured while formatting.
     */
    public final String format(final Object object) throws Exception {
        if (object != null) {
            String className = object.getClass().getName();
            String attributesTemplate = template.getParameter(className);
            if (attributesTemplate != null) {
                return ReflectTemplateTransformer.transformUsingReflection(attributesTemplate, object, this);
            }
            // Else try with parent class
            String parentClassName = object.getClass().getSuperclass().getName();
            attributesTemplate = template.getParameter(parentClassName);
            if (attributesTemplate != null) {
                return ReflectTemplateTransformer.transformUsingReflection(attributesTemplate, object, this);
            }
            // Else try to format directly the object
            if (object instanceof Boolean) {
                if (((Boolean) object).booleanValue()) {
                    return "1";
                }
                return "0";
            } else if (object instanceof String) {
                return object.toString();
            } else if (object.getClass().getName().startsWith("java.lang.")) {
                return object.toString();
            } else if (object instanceof Collection) {
                String toReturn = "";
                for (Iterator it = ((Collection) object).iterator(); it.hasNext();) {
                    toReturn += format(it.next());
                }
                return toReturn;
            } else if (object.getClass().isArray()) {
                String toReturn = "";
                for (int i = 0; i < Array.getLength(object); ++i) {
                    toReturn += format(Array.get(object, i));
                }
                return toReturn;
            } else {
                // It's not normal :
                throw new Exception("FATAL ERROR: Attribute '" + className + "' and '" + parentClassName + "' does not exist in template");
            }
        }
        return "";
    }
}
