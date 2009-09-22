/*
 * Created on 21 juin 2005
 * By Gabriel DROMARD
 */
package net.dromard.common.ldap.passwordchecker.novel;

import java.io.IOException;
import java.lang.reflect.Constructor;

import net.dromard.common.util.system.Console;

import com.novell.ldap.LDAPException;

/**
 * @author Gabriel DROMARD
 *
 * TODO Change this comment
 */
public class PasswordChecker {
    /**
     * This check the 
     * @param user
     * @param password
     * @return
     */
    public static boolean check(String user, String password) {
        return check(user, password, LDAPProperties.getString("load.ldap.classname"));
    }
    
    public static boolean check(String user, String password, String ldapDirectoryClassName) {
        boolean correct = false;
        try {
            // Récupération de la Classe du module à charger
            Class ldapDirectoryClass = Class.forName(ldapDirectoryClassName);
			// Récupération du constructeur correspondant à la liste des parametres donnée
			Constructor moduleConstructor = ldapDirectoryClass.getConstructor(new Class[] {});
			// Création d'une instance avec le constructeur récupérés
			LDAPDirectory ldapDirectory = (LDAPDirectory) moduleConstructor.newInstance(new Object[]{});
            
			// Connect to LDAP directory
			ldapDirectory.connect();
			
			// Verify user password
            String dn = ldapDirectory.getUserDn(user);

            // disconnect with the server
            ldapDirectory.disconnect();
            
            // Try to log into LDAP with user/password
            if(dn != null) {
                try {
                    ldapDirectory.connect(dn, password.getBytes("UTF8"));
                    correct = ldapDirectory.isConnected();
                    ldapDirectory.disconnect();
                } catch (Exception ex) {
                    correct = false;
                    System.err.println("Error: Invalid Credentials");
                }
            }
            
            // 
            if(!correct) {
                ldapDirectory.connect();
                correct = ldapDirectory.verifyPassword(dn, password);
                ldapDirectory.disconnect();
            }
        } catch (LDAPException e) {
            e.printStackTrace();
            if (e.getResultCode() == LDAPException.NO_SUCH_OBJECT) {
                System.err.println("Error: No such entry");
            } else if (e.getResultCode() == LDAPException.NO_SUCH_ATTRIBUTE) {
                System.err.println("Error: No such attribute");
            } else {
                System.err.println("Error: " + e.toString());
                e.printStackTrace();
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.toString());
            e.printStackTrace();
        }
        return correct;
    }
    
    public static void main(String[] args) throws IOException {
        // Verify imputs
        if (args.length != 2) {
            System.out.println("Usage:   java LDAPUtil <login> <password>");
            System.out.println("Example: java LDAPUtil JSmith SecretPassword");
            String login = Console.ask("LDAP Login:", null);
            String password = Console.ask("LDAP Password:", null);
            System.out.println(check(login, password) ? "The password is correct.":"The password is incorrect.\n");
        } else {
            System.out.println(check(args[0], args[1]) ? "The password is correct.":"The password is incorrect.\n");
        }
    }
}
