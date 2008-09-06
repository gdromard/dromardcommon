/**
 * 	File : MockHttpSession.java 11 sept. 07
 */
package net.dromard.web.mock.test;

import java.util.Enumeration;
import java.util.HashMap;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;

public class MockHttpSession implements HttpSession {
    /** Attributes. */
    private HashMap attributes = new HashMap();

    /**
     * @param arg0
     * @return
     */
    public Object getAttribute(final String arg0) {
        return attributes.get(arg0);
    }

    /**
     * @return
     */
    public Enumeration getAttributeNames() {
        return null;
    }

    /**
     * @return
     */
    public long getCreationTime() {
        return 0;
    }

    /**
     * @return
     */
    public String getId() {
        return null;
    }

    /**
     * @return
     */
    public long getLastAccessedTime() {
        return 0;
    }

    /**
     * @return
     */
    public int getMaxInactiveInterval() {
        return 0;
    }

    /**
     * @return
     */
    public ServletContext getServletContext() {
        return null;
    }

    /**
     * @return
     */
    public HttpSessionContext getSessionContext() {
        return null;
    }

    /**
     * @param arg0
     * @return
     */
    public Object getValue(final String arg0) {
        return null;
    }

    /**
     * @return
     */
    public String[] getValueNames() {
        return null;
    }

    public void invalidate() {
    }

    /**
     * @return
     */
    public boolean isNew() {
        return false;
    }

    /**
     * @param arg0
     * @param arg1 
     */
    public void putValue(final String arg0, final Object arg1) {
    }

    /**
     * @param arg0
     */
    public void removeAttribute(final String arg0) {
        attributes.remove(arg0);
    }

    /**
     * @param arg0
     */
    public void removeValue(final String arg0) {
    }

    /**
     * @param arg0
     * @param arg1
     */
    public void setAttribute(final String arg0, final Object arg1) {
        attributes.put(arg0, arg1);
    }

    /**
     * @param arg0
     */
    public void setMaxInactiveInterval(final int arg0) {
    }
}


