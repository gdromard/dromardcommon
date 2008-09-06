package net.dromard.web.taglib;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.Tag;

import net.dromard.web.taglib.exception.InvalidAttributeValueTagException;
import net.dromard.web.taglib.exception.TemplateNotFoundTagException;

import net.dromard.common.util.StringHelper;

/**
 * Abstract Tag class.
 * <br>
 * @author Gabriel Dromard
 */
public abstract class AbstractTemplateTag extends BodyTagSupport implements ITemplateTag {
    /** The regexp used to verify template value. */
    private static final String REGEXP_ATTRIBUTE_SUFFIX = ".regexp";
    /** The error message to be thrown when an error occured while retreiving the template definition. */
    private static final String TEMPLATE_NOT_FOUND_MSG = "An error occured while retreiving template associated with taglib";
    /** The template content variable name. */
    private static final String TEMPLATE_CONTENT_VARIABLE = "CONTENT";
    /** The template content separator. */
    public static final String TEMPLATE_CONTENT_SEPARATOR = "${" + TEMPLATE_CONTENT_VARIABLE + "}";
    /**
     * The error message to be thrown when the template value does not match its regexp verifier.
     * This message need some parameters.
     * Use the following code: <code>MessageFormat.format(ATTRIBUTE_VALUE_NOT_MATCH_REGEXP, new Object[]{"AttributeName", "AttributeValue", "AttributeValueRegexp"})</code>
     */
    private static final String ATTRIBUTE_VALUE_NOT_MATCH_REGEXP = "Attribute \"{0}\" with value \"{1}\" must suit the regular expression: \"{2}\"";

    /**
     * Eval integer type.
     * Possible values are: Tag.EVAL_BODY_INCLUDE, EVAL_PAGE, SKIP_BODY, SKIP_PAGE
     */
    private int evalType = -1;

    /**
     * Default constructor.
     * It calls the implemented methods initializeTemplate().
     * @param evalType the Eval integer type. Possible values are: Tag.EVAL_BODY_INCLUDE, EVAL_PAGE, SKIP_BODY, SKIP_PAGE
     */
    public AbstractTemplateTag(final int evalType) {
        super();
        this.evalType = evalType;
    }

    /**
     * Default constructor.
     * It calls the implemented methods initializeTemplate().
     */
    public AbstractTemplateTag() {
        super();
    }

    /**
     * Add a parameter to template.
     * @param parameterName  The parameter name
     * @param parameterValue The parameter value
     * @throws JspException In the case of an invalid value is given or if template is not found.
     */
    protected final void addParameter(final String parameterName, final String parameterValue) throws JspException {
        try {
            String regexp = getTemplate().getParameter(parameterName + REGEXP_ATTRIBUTE_SUFFIX);
            addParameter(parameterName, parameterValue, regexp);
        } catch (IOException ex) {
            throw new TemplateNotFoundTagException(TEMPLATE_NOT_FOUND_MSG, ex);
        }
    }

    /**
     * Add a parameter to template.
     * @param parameterName  The parameter name
     * @param parameterValue The parameter value
     * @param regexp         The value format to be checked
     * @throws JspTagException In the case of an invalid value is given or if template is not found.
     */
    private void addParameter(final String parameterName, final String parameterValue, final String regexp) throws JspTagException {
        try {
            if (regexp == null || parameterValue.matches(regexp)) {
                getTemplate().addParameter(parameterName, parameterValue);
            } else {
                throw new InvalidAttributeValueTagException(MessageFormat.format(ATTRIBUTE_VALUE_NOT_MATCH_REGEXP, new Object[] {parameterName, parameterValue, regexp}));
            }
        } catch (IOException ex) {
            throw new TemplateNotFoundTagException(TEMPLATE_NOT_FOUND_MSG, ex);
        }
    }

    /**
     * doStartTag method is automatically called by JSP engine when the end tag is thrown.
     * Note: In this implementation, if a taglib variable exists in pageContext its value is taken from.
     * @return The EVAL integer type of the tag.
     * @throws JspException Can occured if something wrong append.
     */
    public int doStartTag() throws JspException {
        try {
            if (evalType == -1) {
                if (getTemplate().getGString().indexOf(TEMPLATE_CONTENT_SEPARATOR) == -1) {
                    evalType = Tag.SKIP_BODY;
                } else {
                    evalType = Tag.EVAL_BODY_INCLUDE;
                }
            }
            // Put servlet Context Path to context so as to be herited by Tag Lib
            getTemplate().addParameter("contextPath", ((HttpServletRequest) pageContext.getRequest()).getContextPath());
            String toPrint = StringHelper.subStringBefore(getTemplate().getGString(), TEMPLATE_CONTENT_SEPARATOR);
            if (toPrint == null) {
                toPrint = getTemplate().getGString();
            }
            if (toPrint != null) {
                toPrint = transform(toPrint);
                if (toPrint != null) {
                    printOut(toPrint);
                }
            }
        } catch (IOException ex) {
            throw new JspTagException(ex.getMessage());
        }
		return evalType;
	}

    /**
     * Transform the template GString by replacing varible with their corresponding values.
     * @param gstring The GString string representation.
     * @return The transformed String
     * @throws IOException can be thrown by sub methods when retieving GString template instance.
     */
    protected String transform(final String gstring) throws IOException {
        String result = new String(gstring);
        int index = -1;
        // Check if the value is a GString
        while (result != null && result.indexOf("${") > index) {
            index = result.indexOf("${");
            String var = extractVariable(result);
            String innerValue = getVariableValue(var);
            // If value is found (as parameter or in pageContext)
            if (innerValue != null) {
                // Check if the value is not a GString
                if (innerValue.indexOf("${") == -1) {
                    // ... if true update template parameter
                    result = StringHelper.replaceAll(result, "${" + var + "}", innerValue);
                } else {
                    // Handle second stage of GString
                    // name="${var1}"
                    // name="${var2}"
                    // var1="value"  --> OK
                    // var2="${var1} --> Not OK (in this loop) but if the var1 come from parameter it is OK
                    String varBis = extractVariable(innerValue);
                    String innerValueBis = getVariableValue(varBis);
                    if (innerValueBis != null && innerValueBis.indexOf("${") == -1) {
                        // ... if true update template parameter
                        innerValue = StringHelper.replaceAll(innerValue, "${" + varBis + "}", innerValueBis);
                        result = StringHelper.replaceAll(result, "${" + var + "}", innerValue);
                    }
                }
            } else {
                result = StringHelper.replaceAll(result, "${" + var + "}", "");
            }
        }
        return result;
    }

    /**
     * Util method that extract the first variable name (if exists).
     * @param source The GString
     * @return The first variable name or null if there is no variable
     */
    private String extractVariable(final String source) {
        String after = StringHelper.subStringAfter(source, "${");
        String between = StringHelper.subStringBefore(after, "}");
        return between;
    }

    /**
     * Util method to retreive the value of a variable from GString parameters or (if nothing was found) in ?ge context.
     * @param variable The variable name on which to retrieve the value
     * @return The variable value (or null if nothing found)
     * @throws IOException Can be thrown by the getTemplate() method.
     */
    private String getVariableValue(final String variable) throws IOException {
        String innerValue = null;
        if (variable != null) {
            innerValue = getTemplate().getParameter(variable);
            if (innerValue == null || innerValue.equals("${" + variable + "}")) {
                Object o = pageContext.getAttribute(variable);
                if (o == null) {
                    innerValue = "";
                } else if (o instanceof String) {
                    innerValue = (String) o;
                } else {
                    innerValue = o.toString();
                }
            }
        }
        return innerValue;
    }

    /**
     * doEndTag method is automatically called by JSP engine when the end tag is thrown.
     * @return The EVAL integer type of the tag.
     * @throws JspException Can occured if something wrong append.
     */
	public final int doEndTag() throws JspException {
        doAfterBody();
	    try {
            // Printing StartTag to output
	        //String toPrint = StringHelper.subStringAfter(getTemplate().toString(), TEMPLATE_CONTENT_SEPARATOR);
            String toPrint = StringHelper.subStringAfter(getTemplate().getGString(), TEMPLATE_CONTENT_SEPARATOR);
            if (toPrint != null) {
                toPrint = transform(toPrint);
                if (toPrint != null) {
                    printOut(toPrint);
                }
            }
		} catch (IOException ex) {
			throw new JspTagException(ex.getMessage());
		}
		return Tag.SKIP_BODY;
	}

    /**
     * Called when wanted to print something out to the page.
     * @param toPrint The string to be printed.
     * @throws IOException Can be thrown by PageContext while printing.
     */
    protected void printOut(final String toPrint) throws IOException {
        pageContext.getOut().print(toPrint);
    }
}
