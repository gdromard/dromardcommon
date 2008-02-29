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
public class ActiveDirectory extends AbstractDirectory {
    private static String key = "ldap.active_directory";
    protected String getPropertyKey() { return key; }
}
