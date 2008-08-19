/**
 * 	File : Tags.java 21 juin 07
 */

package net.dromard.common.templating.reflect;


import net.dromard.common.util.ReflectHelper;
import net.dromard.common.util.StringHelper;

/**
 * Tag lib helper class.
 * <br>
 * @author Gabriel Dromard
 */
public final class ReflectTemplateTransformer {
    /**
     * Has this class is a tool class, the constructor is private so as to disable instantiation.
     */
    private ReflectTemplateTransformer() {
    }

    /**
     * This transformation replace all the ${param}.
     * <p>Code Example</p>
     * <pre>
     *       String query = "INSERT INTO table SET name = ${test}, other_table_id = ${id}";
     *       Object obj = new Object() {
     *          public String getTest() {
     *              return "test";
     *          }
     *          public String getId() {
     *              return "id";
     *          }
     *       }
     *       query = transformUsingReflection(query, obj, new Formatter() { public String format(final Object object) { return object.toString(); }});
     * </pre>
     * @param string    The string to be transformed
     * @param bean      The JavaBean object used to retreive (by reflection) the fields values
     * @param formatter A formatter that is able to format field returned objects into strings
     * @return the transformed template string representation.
     * @throws Exception Thrown if you try to retreive a method that does not exist or while trying to access illegaly the instance
     */
    public static String transformUsingReflection(final String string, final Object bean, final Formatter formatter) throws Exception {
        String result = string;
        String[] splitted = StringHelper.split(result, "$(");
        for (int i = 1; i < splitted.length; i++) {
            String field = StringHelper.subStringBefore(splitted[i], ")", true);
            Object objValue = ReflectHelper.invoke(bean, field);
            String value = "";
            if (objValue != null) {
                if (formatter != null) {
                value = formatter.format(objValue);
                } else {
                    value = objValue.toString();
                }
            }
            result = StringHelper.replaceAll(result, "$(" + field + ")", value);
        }
        return result;
    }
}
