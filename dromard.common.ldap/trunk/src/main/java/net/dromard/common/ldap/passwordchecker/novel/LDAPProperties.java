/*
 * Created on 16 juin 2005
 * By Gabriel DROMARD
 */
package net.dromard.common.ldap.passwordchecker.novel;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * This class is a util class that manage the application resources.
 * @author Gabriel DROMARD
 */
public class LDAPProperties {
    private static ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(LDAPProperties.class.getPackage().getName()+".ldap");

    private LDAPProperties() { /*Singleton*/ }

/** Since JDK 1.4
    public static String getString(Class requester, String key) {
        return getString(requester.getSimpleName()+"."+key);
    }
    
    public static String getString(Object requester, String key) {
        return getString(requester.getClass().getSimpleName()+"."+key);
    }
*/
    
    public static String getString(String key) throws MissingResourceException {
        return RESOURCE_BUNDLE.getString(key);
    }
}
