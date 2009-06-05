/*
 * Created on 15 mars 2006
 * By Gabriel DROMARD
 */
package net.dromard.common.ldap.passwordchecker;

import java.io.IOException;
import java.io.InputStream;

import net.dromard.common.properties.DynamicProperties;


public class LDAPConnectionParameters {
    
    private static String key_ldapConnectionUrl = "ldap.connection.url";
    private static String key_ldapUserEntryDn = "ldap.user.entry.dn";
    private static String key_ldapUserPassordAttribute = "ldap.user.password.attribute";
    private static String key_ldapAdminDn = "ldap.admin.dn";
    private static String key_ldapAdminPassword = "ldap.admin.password";

    private DynamicProperties properties = null;
    
    public LDAPConnectionParameters(InputStream inputStream) throws IOException {
        if(properties == null) properties = new DynamicProperties();
        // Chargement du fichier de config
        properties.load(inputStream);
    }

    /**
     * @return Returns the key_ldapHost.
     */
    public String getLdapConnectionUrl() {
        return properties.getProperty(key_ldapConnectionUrl);
    }
    
    /**
     * @return Returns the key_ldapUserEntryDn.
     */
    public String getLdapUserDnEntry(String user) {
        return properties.getFormattedProperty(key_ldapUserEntryDn, new String[] {user});
    }
    
    /**
     * @return Returns the key_ldapUserEntryDn.
     */
    public String getLdapUserPasswordAttribute() {
        return properties.getProperty(key_ldapUserPassordAttribute);
    }
    
    /**
     * @return Returns the key_ldapHost.
     */
    public String getLdapAdminDn() {
        return properties.getProperty(key_ldapAdminDn);
    }

    /**
     * @return Returns the key_ldapHost.
     */
    public String getLdapAdminPassword() {
        return properties.getProperty(key_ldapAdminPassword);
    }
}
