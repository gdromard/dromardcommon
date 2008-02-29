/**
 * 	File : TestTagHttpServletRequest.java 18 juin 07
 */

package net.dromard.web.mock.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * ...
 * <br>
 * @author          st22085
 */

public class MockHttpServletRequest implements HttpServletRequest, ServletRequest {
    /** User locale. */
    private Locale locale = Locale.ENGLISH;
    /** Attributes. */
    private HashMap attributes = new HashMap();

    /**
     * Default constructor.
     */
    public MockHttpServletRequest() {
    }

    /**
     * @return ...
     * @see javax.servlet.http.HttpServletRequestWrapper#getServletPath()
     */
    public final String getContextPath() {
        return "/design2x";
    }

    /**
     * @param key   The attribute key
     * @param value The attribute value
     * @see javax.servlet.ServletRequest#setAttribute(java.lang.String, java.lang.Object)
     */
    public final void setAttribute(final String key, final Object value) {
        attributes.put(key, value);
    }

    /**
     * @param key The attribute key
     * @return The attribute value
     * @see javax.servlet.ServletRequest#getAttribute(java.lang.String)
     */
    public final Object getAttribute(final String key) {
        return attributes.get(key);
    }

    /**
     * Set the request locale.
     * @param locale the locale to be set.
     * @see javax.servlet.ServletRequest#getLocale()
     */
    public final void setLocale(final Locale locale) {
        this.locale = locale;
    }

    /**
     * Retreive the request locale.
     * @return the request locale.
     * @see javax.servlet.ServletRequest#getLocale()
     */
    public final Locale getLocale() {
        return locale;
    }

    /**
     * Empty implemenation.
     * @return null
     * @see javax.servlet.http.HttpServletRequest#getAuthType()
     */
    public final String getAuthType() {
        return null;
    }

    /**
     * Empty implemenation.
     * @return null
     * @see javax.servlet.http.HttpServletRequest#getCookies()
     */
    public final Cookie[] getCookies() {
        return null;
    }

    /**
     * Empty implemenation.
     * @param argument argument
     * @return 0
     * @see javax.servlet.http.HttpServletRequest#getDateHeader(java.lang.String)
     */
    public final long getDateHeader(final String argument) {
        return 0;
    }

    /**
     * Empty implemenation.
     * @param argument argument
     * @return null
     * @see javax.servlet.http.HttpServletRequest#getHeader(java.lang.String)
     */
    public final String getHeader(final String argument) {
        return null;
    }

    /**
     * Empty implemenation.
     * @return null
     * @see javax.servlet.http.HttpServletRequest#getHeaderNames()
     */
    public final Enumeration getHeaderNames() {
        return null;
    }

    /**
     * Empty implemenation.
     * @param argument argument
     * @return null
     * @see javax.servlet.http.HttpServletRequest#getHeaders(java.lang.String)
     */
    public final Enumeration getHeaders(final String argument) {
        return null;
    }

    /**
     * Empty implemenation.
     * @param argument argument
     * @return 0
     * @see javax.servlet.http.HttpServletRequest#getIntHeader(java.lang.String)
     */
    public final int getIntHeader(final String argument) {
        return 0;
    }

    /**
     * Empty implemenation.
     * @return null
     * @see javax.servlet.http.HttpServletRequest#getMethod()
     */
    public final String getMethod() {
        return null;
    }

    /**
     * Empty implemenation.
     * @return null
     * @see javax.servlet.http.HttpServletRequest#getPathInfo()
     */
    public final String getPathInfo() {
        return null;
    }

    /**
     * Empty implemenation.
     * @return null
     * @see javax.servlet.http.HttpServletRequest#getPathTranslated()
     */
    public final String getPathTranslated() {
        return null;
    }

    /**
     * Empty implemenation.
     * @return null
     * @see javax.servlet.http.HttpServletRequest#getQueryString()
     */
    public final String getQueryString() {
        return null;
    }

    /**
     * Empty implemenation.
     * @return null
     * @see javax.servlet.http.HttpServletRequest#getRemoteUser()
     */
    public final String getRemoteUser() {
        return null;
    }

    /**
     * Empty implemenation.
     * @return null
     * @see javax.servlet.http.HttpServletRequest#getRequestedSessionId()
     */
    public final String getRequestedSessionId() {
        return null;
    }

    /**
     * Empty implemenation.
     * @return null
     * @see javax.servlet.http.HttpServletRequest#getRequestURI()
     */
    public final String getRequestURI() {
        return null;
    }

    /**
     * Empty implemenation.
     * @return null
     * @see javax.servlet.http.HttpServletRequest#getRequestURL()
     */
    public final StringBuffer getRequestURL() {
        return null;
    }

    /**
     * Empty implemenation.
     * @return null
     * @see javax.servlet.http.HttpServletRequest#getServletPath()
     */
    public final String getServletPath() {
        return null;
    }

    /**
     * Empty implemenation.
     * @return null
     * @see javax.servlet.http.HttpServletRequest#getSession()
     */
    public final HttpSession getSession() {
        return null;
    }

    /**
     * Empty implemenation.
     * @param argument argument
     * @return null
     * @see javax.servlet.http.HttpServletRequest#getSession(boolean)
     */
    public final HttpSession getSession(final boolean argument) {
        return null;
    }

    /**
     * Empty implemenation.
     * @return null
     * @see javax.servlet.http.HttpServletRequest#getUserPrincipal()
     */
    public final Principal getUserPrincipal() {
        return null;
    }

    /**
     * Empty implemenation.
     * @return false
     * @see javax.servlet.http.HttpServletRequest#isRequestedSessionIdFromCookie()
     */
    public final boolean isRequestedSessionIdFromCookie() {
        return false;
    }

    /**
     * Empty implemenation.
     * @return false
     * @see javax.servlet.http.HttpServletRequest#isRequestedSessionIdFromUrl()
     */
    public final boolean isRequestedSessionIdFromUrl() {
        return false;
    }

    /**
     * Empty implemenation.
     * @return false
     * @see javax.servlet.http.HttpServletRequest#isRequestedSessionIdFromURL()
     */
    public final boolean isRequestedSessionIdFromURL() {
        return false;
    }

    /**
     * Empty implemenation.
     * @return false
     * @see javax.servlet.http.HttpServletRequest#isRequestedSessionIdValid()
     */
    public final boolean isRequestedSessionIdValid() {
        return false;
    }

    /**
     * Empty implemenation.
     * @param argument argument
     * @return false
     * @see javax.servlet.http.HttpServletRequest#isUserInRole(java.lang.String)
     */
    public final boolean isUserInRole(final String argument) {
        return false;
    }

    /**
     * Empty implemenation.
     * @return null
     * @see javax.servlet.ServletRequest#getAttributeNames()
     */
    public final Enumeration getAttributeNames() {
        return null;
    }

    /**
     * Empty implemenation.
     * @return null
     * @see javax.servlet.ServletRequest#getCharacterEncoding()
     */
    public final String getCharacterEncoding() {
        return null;
    }

    /**
     * Empty implemenation.
     * @return 0
     * @see javax.servlet.ServletRequest#getContentLength()
     */
    public final int getContentLength() {
        return 0;
    }

    /**
     * Empty implemenation.
     * @return null
     * @see javax.servlet.ServletRequest#getContentType()
     */
    public final String getContentType() {
        return null;
    }

    /**
     * Empty implemenation.
     * @return null
     * @see javax.servlet.ServletRequest#getInputStream()
     * @throws IOException {@link javax.servlet.ServletRequest#getInputStream()}
     */
    public final ServletInputStream getInputStream() throws IOException {
        return null;
    }

    /**
     * Empty implemenation.
     * @return null
     * @see javax.servlet.ServletRequest#getLocales()
     */
    public final Enumeration getLocales() {
        return null;
    }

    /**
     * Empty implemenation.
     * @param argument argument
     * @return null
     * @see javax.servlet.ServletRequest#getParameter(java.lang.String)
     */
    public final String getParameter(final String argument) {
        return null;
    }

    /**
     * Empty implemenation.
     * @return null
     * @see javax.servlet.ServletRequest#getParameterMap()
     */
    public final Map getParameterMap() {
        return null;
    }

    /**
     * Empty implemenation.
     * @return null
     * @see javax.servlet.ServletRequest#getParameterNames()
     */
    public final Enumeration getParameterNames() {
        return null;
    }

    /**
     * Empty implemenation.
     * @param argument argument
     * @return null
     * @see javax.servlet.ServletRequest#getParameterValues(java.lang.String)
     */
    public final String[] getParameterValues(final String argument) {
        return null;
    }

    /**
     * Empty implemenation.
     * @return null
     * @see javax.servlet.ServletRequest#getProtocol()
     */
    public final String getProtocol() {
        return null;
    }

    /**
     * Empty implemenation.
     * @return null
     * @see javax.servlet.ServletRequest#getReader()
     * @throws IOException {@link javax.servlet.ServletRequest#getReader()}
     */
    public final BufferedReader getReader() throws IOException {
        return null;
    }

    /**
     * Empty implemenation.
     * @param argument argument
     * @return null
     * @see javax.servlet.ServletRequest#getRealPath(java.lang.String)
     */
    public final String getRealPath(final String argument) {
        return null;
    }

    /**
     * Empty implemenation.
     * @return null
     * @see javax.servlet.ServletRequest#getRemoteAddr()
     */
    public final String getRemoteAddr() {
        return null;
    }

    /**
     * Empty implemenation.
     * @return null
     * @see javax.servlet.ServletRequest#getRemoteHost()
     */
    public final String getRemoteHost() {
        return null;
    }

    /**
     * Empty implemenation.
     * @param argument argument
     * @return null
     * @see javax.servlet.ServletRequest#getRequestDispatcher(java.lang.String)
     */
    public final RequestDispatcher getRequestDispatcher(final String argument) {
        return null;
    }

    /**
     * Empty implemenation.
     * @return null
     * @see javax.servlet.ServletRequest#getScheme()
     */
    public final String getScheme() {
        return null;
    }

    /**
     * Empty implemenation.
     * @return null
     * @see javax.servlet.ServletRequest#getServerName()
     */
    public final String getServerName() {
        return null;
    }

    /**
     * Empty implemenation.
     * @return 0
     * @see javax.servlet.ServletRequest#getServerPort()
     */
    public final int getServerPort() {
        return 0;
    }

    /**
     * Empty implemenation.
     * @return null
     * @see javax.servlet.ServletRequest#isSecure()
     */
    public final boolean isSecure() {
        return false;
    }

    /**
     * Empty implemenation.
     * @param argument argument
     * @see javax.servlet.ServletRequest#removeAttribute(java.lang.String)
     */
    public final void removeAttribute(final String argument) {
    }

    /**
     * Empty implemenation.
     * @param argument argument
     * @see javax.servlet.ServletRequest#setCharacterEncoding(java.lang.String)
     * @throws UnsupportedEncodingException {@link javax.servlet.ServletRequest#setCharacterEncoding(java.lang.String)}
     */
    public final void setCharacterEncoding(final String argument) throws UnsupportedEncodingException {
    }
}


