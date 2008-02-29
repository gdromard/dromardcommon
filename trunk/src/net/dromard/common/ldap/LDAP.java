/*
 * Created on 15 mars 2006
 * By Gabriel DROMARD
 */
package net.dromard.common.ldap;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import javax.naming.ldap.Control;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;

/**
 * This object is an LDAP connector, this implementation is on his early age, do not hesitate to extends it.
 * Typical use:
 * <pre>
 *    public void search() {
 *        LDAP ldap = new LDAP("localhost", "389", "c=fr,o=company");
 *        ldap.connect("cn=User", "User password");
 *        // or ldap.connect(); // Anonymous connection
 *        ldap.setSortableResult("cn");
 *        
 *        // Search for people
 *        NamingEnumeration results = ldap.search("", "(cn=*)");
 *        
 *        // Enumerate answers
 *        while (results != null && results.hasMore()) {
 *            NameClassPair item = (NameClassPair)results.next();
 *            process(item);
 *        }
 *        
 *        ldap.disconnect();
 *    }
 *    
 *    public void process(NameClassPair item) {
 *        BasicAttribute att = (BasicAttribute)item.getAttributes().get("givenname");
 *        System.out.println(item.getName()+" - "+((att!=null)?att.get():"null"));
 *    }
 *        
 * </pre> 
 *
 *
 * <br/>
 * <pre>
 *    +---------+
 *    | History |
 *    +---------+
 *
 *  [15/03/2006] by Gabriel Dromard
 *     - Creation.
 * </pre>
 * <br/>
 * @author Gabriel Dromard
 */
public class LDAP {
    protected String url;
    protected LdapContext ctx;
    protected boolean isConnected = false;
    
    /**
     * Instantiate the LDAP object.
     * @param url LDAP connection url (ex: ldap://localhost:386/c=fr,o=company).
     */
    public LDAP(String url) {
        // Initialize settings
        this.url = url;
    }
    
    /**
     * Instantiate the LDAP object.
     * @param host LDAP host.
     * @param port LDAP port.
     * @param base LDAP base on which to connect.
     */
    public LDAP(String host, String port, String base) {
        // Initialize settings
        url = "ldap://"+host+":"+port+"/"+base;
    }
    
    /**
     * Connect to the LDAP directory
     * @return true if connected false if not
     * @throws NamingException
     */
    public boolean connect() throws NamingException {
        Hashtable<String, String> env = new Hashtable<String, String>();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL, url);
        // Create context
        ctx = new InitialLdapContext(env, null);
        // Return connection status
        return isConnected = true;
    }
    
    public boolean connect(String userDN, String password) throws NamingException  {
        // Initialize settings
        Hashtable<String, String> env = new Hashtable<String, String>();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL, url);
        env.put(Context.SECURITY_AUTHENTICATION, "simple");
        env.put(Context.SECURITY_PRINCIPAL, userDN);
        env.put(Context.SECURITY_CREDENTIALS, password);
        // Create context
        ctx = new InitialLdapContext(env, null);
        // Return connection status
        return isConnected = true;
    }
    
    public void setSortableResult(String attribute) throws NamingException, IOException {
        // Create critical Sort that sorts based on CN
        Control[] ctxCtls = new Control[] {
            new SortControl(new String[]{attribute}, Control.CRITICAL)
        };
    
        // Sets context request controls; effect until unset
        ctx.setRequestControls(ctxCtls);
    }
    
    public NamingEnumeration search(String name, String filter, String[] attributes) throws NamingException {
        // Perform search
        SearchControls sCtrl = new SearchControls();
        sCtrl.setSearchScope(SearchControls.SUBTREE_SCOPE);
        sCtrl.setReturningAttributes(attributes);
        return ctx.search(name, filter, sCtrl);
    }
    
    public NamingEnumeration list(String name) throws NamingException {
        // Perform search
        return ctx.list(name);
    }
    
    /**
     * Retrieve a vaules of an attribute for a given element (name)
     * @param name      The element name query string
     * @param attribute The attribute to retrieve
     * @return The list of returned attributes
     * @throws NamingException 
     */
    public List getAttributeValues(String name, String attribute) throws NamingException {
        List list = new ArrayList();
        Attributes attrs = ctx.getAttributes(name, new String[] { attribute });
        NamingEnumeration members = attrs.getAll();
        while (members.hasMoreElements()) {
            Attribute attr = (Attribute) members.next();
            for (int i = 0; i < attr.size(); i++) {
                list.add(attr.get(i));
            }
        }
        return list;
    }
    
    /**
     * Retrieve a set of attributes with their values
     * @param name       The element name query string
     * @param attributes The attributes to retrieve
     * @return The list of returned attributes
     * @throws NamingException 
     */
    public List getAttributes(String name, String[] attributes) throws NamingException {
        List list = new ArrayList();
        ctx.list(name);
        Attributes attrs = ctx.getAttributes(name, attributes);
        NamingEnumeration members = attrs.getAll();
        while (members.hasMoreElements()) {
            list.add(members.next());
        }
        return list;
    }
    
    
    public void disconnect() throws NamingException {
        // Close the LDAP association
        ctx.close();
    }
}