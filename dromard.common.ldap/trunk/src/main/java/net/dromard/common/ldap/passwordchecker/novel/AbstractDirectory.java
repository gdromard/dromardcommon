/*
 * Created on 21 juin 2005
 * By Gabriel DROMARD
 */
package net.dromard.common.ldap.passwordchecker.novel;


/**
 * This is a specific implementation of the interface 
 * 
 * @author Gabriel DROMARD
 */
public abstract class AbstractDirectory extends LDAPDirectory {
    private String name = LDAPProperties.getString(getPropertyKey()+".name"); 
    private String host = LDAPProperties.getString(getPropertyKey()+".host");
    private String user = LDAPProperties.getString(getPropertyKey()+".user.dn");
    private int    port = Integer.parseInt(LDAPProperties.getString(getPropertyKey()+".port"));
    private String searchBaseDn	= LDAPProperties.getString(getPropertyKey()+".search.base.dn");
    private String userEntry		= LDAPProperties.getString(getPropertyKey()+".user.entry.dn");
    
    // false because it is NOT encoded.
    private Credential pass = new Credential(LDAPProperties.getString(getPropertyKey()+".user.password"), Boolean.valueOf(LDAPProperties.getString(getPropertyKey()+".user.password.encoded")).booleanValue());
    
    protected abstract String getPropertyKey();
    
    public String getName() 			{ return name; }
    public String getHost() 			{ return host; }
    public int    getPort() 			{ return port; }
    public String getUserDn() 		    { return user; }
    public Credential getUserPassword() { return pass; }
    public String getSearchBaseDn()	    { return searchBaseDn; }
    public String getUserEntry()		{ return userEntry; }
}
