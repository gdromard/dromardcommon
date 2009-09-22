/**
 * 	File : TestTagServletContext.java 12 juin 07
 */

package net.dromard.web.mock.test;

import java.io.File;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Set;

import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

/**
 * This is a mock implementation of ServletContext class.
 * It is design to be used for unit tests.
 * <br>
 * @author Gabriel Dromard
 */
public class MockServletContext implements ServletContext {

	private String rootPath;

	public MockServletContext(final String root) {
		this.rootPath = root;
	}

    /**
     * This implementation concat 'web/' with the given resource and return its absolute path.
     * @param resource The resource on which to get informations
     * @return The resource real path.
     * @see javax.servlet.ServletContext#getRealPath(java.lang.String)
     */
    public final String getRealPath(final String resource) {
        try {
            return new File(rootPath + resource).getAbsolutePath();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * Empty implementation.
     * @param argument argument
     * @return null
     * @see javax.servlet.ServletContext#getAttribute(java.lang.String)
     */
    public final Object getAttribute(final String argument) {
        return null;
    }

    /**
     * Empty implementation.
     * @return null
     * @see javax.servlet.ServletContext#getAttributeNames()
     */
    public final Enumeration getAttributeNames() {
        return null;
    }

    /**
     * Empty implementation.
     * @param argument argument
     * @return null
     * @see javax.servlet.ServletContext#getContext(java.lang.String)
     */
    public final ServletContext getContext(final String argument) {
        return null;
    }

    /**
     * Empty implementation.
     * @param argument argument
     * @return null
     * @see javax.servlet.ServletContext#getInitParameter(java.lang.String)
     */
    public final String getInitParameter(final String argument) {
        return null;
    }

    /**
     * Empty implementation.
     * @return null
     * @see javax.servlet.ServletContext#getInitParameterNames()
     */
    public final Enumeration getInitParameterNames() {
        return null;
    }

    /**
     * Empty implementation.
     * @return 0
     * @see javax.servlet.ServletContext#getMajorVersion()
     */
    public final int getMajorVersion() {
        return 0;
    }

    /**
     * Empty implementation.
     * @param argument argument
     * @return null
     * @see javax.servlet.ServletContext#getMimeType(java.lang.String)
     */
    public final String getMimeType(final String argument) {
        return null;
    }

    /**
     * Empty implementation.
     * @return 0
     * @see javax.servlet.ServletContext#getMinorVersion()
     */
    public final int getMinorVersion() {
        return 0;
    }

    /**
     * Empty implementation.
     * @param argument argument
     * @return null
     * @see javax.servlet.ServletContext#getNamedDispatcher(java.lang.String)
     */
    public final RequestDispatcher getNamedDispatcher(final String argument) {
        return null;
    }

    /**
     * Empty implementation.
     * @param argument argument
     * @return null
     * @see javax.servlet.ServletContext#getRequestDispatcher(java.lang.String)
     */
    public final RequestDispatcher getRequestDispatcher(final String argument) {
        return null;
    }

    /**
     * Empty implementation.
     * @param argument argument
     * @return null
     * @see javax.servlet.ServletContext#getResource(java.lang.String)
     * @throws MalformedURLException {@link javax.servlet.ServletContext#getResource(java.lang.String)}
     */
    public final URL getResource(final String argument) throws MalformedURLException {
        return null;
    }

    /**
     * Empty implementation.
     * @param argument argument
     * @return null
     * @see javax.servlet.ServletContext#getResourceAsStream(java.lang.String)
     */
    public final InputStream getResourceAsStream(final String argument) {
        return null;
    }

    /**
     * Empty implementation.
     * @param argument argument
     * @return null
     * @see javax.servlet.ServletContext#getResourcePaths(java.lang.String)
     */
    public final Set getResourcePaths(final String argument) {
        return null;
    }

    /**
     * Empty implementation.
     * @return null
     * @see javax.servlet.ServletContext#getServerInfo()
     */
    public final String getServerInfo() {
        return null;
    }

    /**
     * Empty implementation.
     * @param argument argument
     * @return null
     * @see javax.servlet.ServletContext#getServlet(java.lang.String)
     * @throws ServletException {@link javax.servlet.ServletContext#getServlet(java.lang.String)}
     */
    public final Servlet getServlet(final String argument) throws ServletException {
        return null;
    }

    /**
     * Empty implementation.
     * @return null
     * @see javax.servlet.ServletContext#getServletContextName()
     */
    public final String getServletContextName() {
        return null;
    }

    /**
     * Empty implementation.
     * @return null
     * @see javax.servlet.ServletContext#getServletNames()
     */
    public final Enumeration getServletNames() {
        return null;
    }

    /**
     * Empty implementation.
     * @return null
     * @see javax.servlet.ServletContext#getServlets()
     */
    public final Enumeration getServlets() {
        return null;
    }

    /**
     * Empty implementation.
     * @param argument argument
     * @see javax.servlet.ServletContext#log(java.lang.String)
     */
    public final void log(final String argument) {
    }

    /**
     * Empty implementation.
     * @param argument argument
     * @param argument1 argument1
     * @see javax.servlet.ServletContext#log(java.lang.Exception, java.lang.String)
     */
    public final void log(final Exception argument, final String argument1) {
    }

    /**
     * Empty implementation.
     * @param argument argument
     * @param argument1 argument1
     * @see javax.servlet.ServletContext#log(java.lang.String, java.lang.Throwable)
     */
    public final void log(final String argument, final Throwable argument1) {
    }

    /**
     * Empty implementation.
     * @param argument argument
     * @see javax.servlet.ServletContext#removeAttribute(java.lang.String)
     */
    public final void removeAttribute(final String argument) {
    }

    /**
     * Empty implementation.
     * @param argument argument
     * @param argument1 argument1
     * @see javax.servlet.ServletContext#setAttribute(java.lang.String, java.lang.Object)
     */
    public final void setAttribute(final String argument, final Object argument1) {
    }
}


