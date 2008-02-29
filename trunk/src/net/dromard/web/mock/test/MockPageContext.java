/**
 *  File : TestTagPageContext.java 12 juin 07
 */

package net.dromard.web.mock.test;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;

/**
 * This class is a very light implementation of Page context especialy for Testing the tag generation.
 * <br>
 * @author          st22085
 */

public class MockPageContext extends PageContext {
    /** Out. */
    private MockJspWriter out;
    /** Request. */
    private HttpServletRequest request = new MockHttpServletRequest();
    /** Servlet Context. */
    private ServletContext servletContext = new MockServletContext();

    /**
     * Default constructor.
     */
    public MockPageContext() {
        out = new MockJspWriter();
    }

    /**
     * Default constructor.
     * @param printToConsole true to redirect output to console.
     */
    public MockPageContext(final boolean printToConsole) {
        out = new MockJspWriter(printToConsole);
    }

    /**
     * .
     * @param arg argument
     * @return null
     * @see javax.servlet.jsp.PageContext#findAttribute(java.lang.String)
     */
    public final Object findAttribute(final String arg) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * .
     * @param arg argument
     * @see javax.servlet.jsp.PageContext#forward(java.lang.String)
     * @throws ServletException ...
     * @throws IOException ...
     */
    public final void forward(final String arg) throws ServletException, IOException {
        // TODO Auto-generated method stub

    }

    /**
     * .
     * @param arg argument
     * @return null
     * @see javax.servlet.jsp.PageContext#getAttribute(java.lang.String)
     */
    public final Object getAttribute(final String arg) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * .
     * @param arg argument
     * @param arg1 argument
     * @return null
     * @see javax.servlet.jsp.PageContext#getAttribute(java.lang.String, int)
     */
    public final Object getAttribute(final String arg, final int arg1) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * .
     * @param arg argument
     * @return null
     * @see javax.servlet.jsp.PageContext#getAttributeNamesInScope(int)
     */
    public final Enumeration getAttributeNamesInScope(final int arg) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * .
     * @param arg argument
     * @return 0
     * @see javax.servlet.jsp.PageContext#getAttributesScope(java.lang.String)
     */
    public final int getAttributesScope(final String arg) {
        // TODO Auto-generated method stub
        return 0;
    }

    /**
     * .
     * @return null
     * @see javax.servlet.jsp.PageContext#getException()
     */
    public final Exception getException() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @return ...
     * @see javax.servlet.jsp.PageContext#getOut()
     */
    public final JspWriter getOut() {
        return out;
    }

    /**
     * .
     * @return null
     * @see javax.servlet.jsp.PageContext#getPage()
     */
    public final Object getPage() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @return a TestTagHttpServletRequest instance
     * @see javax.servlet.jsp.PageContext#getRequest()
     */
    public final ServletRequest getRequest() {
        return request;
    }

    /**
     * .
     * @return null
     * @see javax.servlet.jsp.PageContext#getResponse()
     */
    public final ServletResponse getResponse() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * .
     * @return null
     * @see javax.servlet.jsp.PageContext#getServletConfig()
     */
    public final ServletConfig getServletConfig() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * This Implementation return a new TestTagServletContext instance.
     * @return a new TestTagServletContext;
     * @see javax.servlet.jsp.PageContext#getServletContext()
     */
    public final ServletContext getServletContext() {
        return servletContext;
    }

    /**
     * .
     * @return null
     * @see javax.servlet.jsp.PageContext#getSession()
     */
    public final HttpSession getSession() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * .
     * @param arg argument
     * @see javax.servlet.jsp.PageContext#handlePageException(java.lang.Exception)
     * @throws ServletException ...
     * @throws IOException ...
     */
    public final void handlePageException(final Exception arg) throws ServletException, IOException {
        // TODO Auto-generated method stub

    }

    /**
     * .
     * @param arg argument
     * @see javax.servlet.jsp.PageContext#handlePageException(java.lang.Throwable)
     * @throws ServletException ...
     * @throws IOException ...
     */
    public final void handlePageException(final Throwable arg) throws ServletException, IOException {
        // TODO Auto-generated method stub

    }

    /**
     * .
     * @param arg argument
     * @see javax.servlet.jsp.PageContext#include(java.lang.String)
     * @throws ServletException ...
     * @throws IOException ...
     */
    public void include(final String arg) throws ServletException, IOException {
        // TODO Auto-generated method stub

    }

    /**
     * .
     * @param arg argument
     * @param arg1 argument
     * @param arg2 argument
     * @param arg3 argument
     * @param arg4 argument
     * @param arg5 argument
     * @param arg6 argument
     * @see javax.servlet.jsp.PageContext#initialize(javax.servlet.Servlet, javax.servlet.ServletRequest, javax.servlet.ServletResponse, java.lang.String, boolean, int, boolean)
     * @throws IOException ...
     */
    public void initialize(final Servlet arg, final ServletRequest arg1, final ServletResponse arg2, final String arg3, final boolean arg4, final int arg5, final boolean arg6) throws IOException {
        // TODO Auto-generated method stub

    }

    /**
     * .
     * @see javax.servlet.jsp.PageContext#release()
     */
    public final void release() {
        // TODO Auto-generated method stub

    }

    /**
     * .
     * @param arg argument
     * @see javax.servlet.jsp.PageContext#removeAttribute(java.lang.String)
     */
    public final void removeAttribute(final String arg) {
        // TODO Auto-generated method stub

    }

    /**
     * .
     * @param arg argument
     * @param arg1 argument
     * @see javax.servlet.jsp.PageContext#removeAttribute(java.lang.String, int)
     */
    public final void removeAttribute(final String arg, final int arg1) {
        // TODO Auto-generated method stub

    }

    /**
     * .
     * @param arg argument
     * @param arg1 argument
     * @see javax.servlet.jsp.PageContext#setAttribute(java.lang.String, java.lang.Object)
     */
    public final void setAttribute(final String arg, final Object arg1) {
        // TODO Auto-generated method stub

    }

    /**
     * .
     * @param arg argument
     * @param arg1 argument
     * @param arg2 argument
     * @see javax.servlet.jsp.PageContext#setAttribute(java.lang.String, java.lang.Object, int)
     */
    public final void setAttribute(final String arg, final Object arg1, final int arg2) {
    }
}
