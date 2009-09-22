package net.dromard.common.ldap;


import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.naming.directory.Attribute;


/**
 * This class is a test class that check if a given user belong to a given group.
 * 
 * @author Gabriel DROMARD
 *
 * TODO Change this comment
 */
public class Test {
    static String ldapHost="acdslan-vip-i";
    static String ldapPort="3890";
    static String ldapBase="";
    static String ldapConnectionUrl="ldap://"+ldapHost+":"+ldapPort+"/"+ldapBase;
    static String ldapAdminDn="cn=SA-F790-bind,OU=admin-ctx,OU=F790,OU=Apps,O=Airbus";
    static String ldapAdminPassword="*INTF790@j";
    //static String ldapGroup = "OU=admin-ctx,OU=F790,OU=Apps,O=Airbus"; 
    static String ldapGroup = "CN=F790-Admin,OU=groups,OU=F790,OU=Apps,O=Airbus";
    
    /**
     * Test entry.
     */
    public static void main(String[] args) throws Exception {
        // Direct check using LDAP connection
        System.out.println("Connecting to "+ldapConnectionUrl);
        LDAP ldap = new LDAP(ldapConnectionUrl);
        System.out.println("Connecting user "+ldapAdminDn);
        ldap.connect(ldapAdminDn, ldapAdminPassword);
        List memberAttributeValues = ldap.getAttributeValues(ldapGroup, "member");
        System.out.println("---------------");
        Iterator it = memberAttributeValues.iterator();
        while (it.hasNext()) {
            String member = (String) it.next();
            System.out.println("\tName: " + member);
            List memberAttributes = ldap.getAttributes(member, new String[] {"uid", "displayName"});
            Iterator it2 = memberAttributes.iterator();
            while (it2.hasNext()) {
                Attribute attribute = (Attribute) it2.next();
                for (int i = 0; i < attribute.size(); i++) {
                    System.out.print("\t" + attribute.getID() + ": " + attribute.get(i) + "\n");
                }
            }
        }
        ldap.disconnect();
        System.out.println("---------------");

        // --------------------------------------------

        String test = "st03691";
        
        Date start = new Date();
        // Direct check using LDAP connection
        ldap = new LDAP(ldapConnectionUrl);
        ldap.connect(ldapAdminDn, ldapAdminPassword);
        memberAttributeValues = ldap.getAttributeValues(ldapGroup, "member");
        it = memberAttributeValues.iterator();
        while (it.hasNext()) {
            String member = (String) it.next();
            List uidAttributes = ldap.getAttributeValues(member, "uid");
            Iterator it2 = uidAttributes.iterator();
            while (it2.hasNext()) {
                if ( test.equals(it2.next())) {
                    System.out.println("Yes " + test + " is in the group.");
                }
            }
        }
        ldap.disconnect();
        System.out.println("Done in " + (new Date().getTime() - start.getTime()) + "ms");
    }
}
