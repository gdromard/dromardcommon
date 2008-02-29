/**
 * 	File : GString.java 11 juin 07
 */

package net.dromard.common.templating;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import net.dromard.common.io.StreamHelper;
import net.dromard.common.util.StringHelper;
import net.dromard.common.util.WordTokenizer;

/**
 * <p>This class manage transformation with pattern ${parameter}.</p>
 * <p>Code Example</p>
 * <pre>
 *       HashMap params = new HashMap();
 *       params.put("id", new Integer(15));
 *       params.put("test", "TEST");
 *       GString gQuery = new GString("INSERT INTO table SET name = ${test}, other_table_id = ${id}");
 *       String query = gQuery.toString();
 * </pre>
 *
 * @author Gabriel DROMARD
 */
public class GString {
    /** The internal string. */
    private String string = null;

    /** The parameters to be replaced (key, value). */
    private Map parameters = null;

    /**
     * Instanciate a new GString using a given string.
     * GString parameters are represented as this: ${KEY}.
     * The parameters as to be set using {@link #addParameter(String, String)}
     * @param string     The GString representation.
     */
    public GString(final String string) {
        this.string = string;
    }

    /**
     * Instanciate a new GString using a given string.
     * GString parameters are represented as this: ${KEY}.
     * You can add parameter using {@link #addParameter(String, String)}.
     * @param string     The GString representation.
     * @param parameters The parameters map (key - value)
     */
    public GString(final String string, final Map parameters) {
        this.string = string;
        this.parameters = parameters;
    }

    /**
     * Clone a GString.
     * @return The GString cloned instance.
     */
    public final Object clone() {
        Map para = new Hashtable();
        para.putAll(parameters);
        GString clone = new GString(string, para);
        return clone;
    }

    /**
     * This method set the parameters. Existing parameters are override.
     * @param parameters The parameters map (key - value)
     */
    public final void setParameters(final Map parameters) {
        this.parameters = parameters;
    }

    /**
     * A parameter can be added or replaced using this method.
     * @param parameterName  The parameter name (key)
     * @param parameterValue The parameter value (value)
     */
    public final void addParameter(final String parameterName, final String parameterValue) {
        if (parameters == null) {
            parameters = new HashMap();
        }
        parameters.put(parameterName, parameterValue);
    }

    /**
     * Retreive the GString template.
     * @return the original string before transformation.
     */
    public final String getGString() {
        return string;
    }

    /**
     * Retreive the Gstring attributes.
     * @return the parameters
     */
    public final Map getParameters() {
        return parameters;
    }

    /**
     * Retreive the value of a given attribute.
     * @param name The attribute name.
     * @return the parameter value (or null if it does not exists)
     */
    public final String getParameter(final String name) {
        return (String) parameters.get(name);
    }

    /**
     * Return the string with parameters replaced.
     * Note: If a parameter is missing it will be conserved in the result.
     * @return The final string.
     */
    public final String toString() {
        if (parameters == null) {
            return string;
        }
        return transform(string, parameters);
    }

    /**
     * This transformation replace all the ${param}.
     * <p>Code Example</p>
     * <pre>
     *       String query = "INSERT INTO table SET name = ${test}, other_table_id = ${id}";
     *       HashMap params = new HashMap();
     *       params.put("id", new Integer(15));
     *       params.put("test", "TEST");
     *       query = transformQuery(query, params);
     * </pre>
     * @param string The string to be transformed
     * @param params The parameters to use for trensformations
     * @return the transformed template string representation.
     */
    public static final String transform(final String string, final Map params) {
        return transform(string, params, true);
    }

    /**
     * This transformation replace all the ${param}.
     * <p>Code Example</p>
     * <pre>
     *       String query = "INSERT INTO table SET name = ${test}, other_table_id = ${id}";
     *       HashMap params = new HashMap();
     *       params.put("id", new Integer(15));
     *       params.put("test", "TEST");
     *       query = transformQuery(query, params);
     * </pre>
     * @param string The string to be transformed
     * @param params The parameters to use for trensformations
     * @param loop   If true loop the transformation 1 time.
     * @return the transformed template string representation.
     */
    private static String transform(final String string, final Map params, final boolean loop) {
        String result = string;
        for (Iterator iter = params.keySet().iterator(); iter.hasNext();) {
            String key = (String) iter.next();
            String value = (String) params.get(key);
            result = StringHelper.replaceAll(result, "${" + key + "}", value);
            result = StringHelper.replaceAll(result, "$(" + key + ")", value);
        }
        // If some pattern has been found in values (and if it dos not yet been replaced)
        if (loop && (WordTokenizer.countOccurence(result, "${") > 0 || WordTokenizer.countOccurence(result, "$(") > 0)) {
            result = transform(result, params, false);
        }
        return result;
    }

    /**
     * Create a GString instance using a file sontent as string template.
     * @param template The template inputstream.
     * @return The new GString instance.
     * @throws IOException Occured if you did a mistake in the given parameters ...
     */
    public static GString load(final InputStream template) throws IOException {
        return new GString(StreamHelper.getStreamContent(template));
    }
}
