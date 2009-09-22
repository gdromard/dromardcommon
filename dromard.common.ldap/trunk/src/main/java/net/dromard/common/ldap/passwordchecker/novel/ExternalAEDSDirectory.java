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
public class ExternalAEDSDirectory extends InternalAEDSDirectory {
    private static String key = "ldap.AEDS.external";
    protected String getPropertyKey() { return key; }
}
